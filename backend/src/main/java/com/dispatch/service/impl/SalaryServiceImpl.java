package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Attendance;
import com.dispatch.entity.Dispatch;
import com.dispatch.entity.Employee;
import com.dispatch.entity.Job;
import com.dispatch.entity.Salary;
import com.dispatch.mapper.AttendanceMapper;
import com.dispatch.mapper.DispatchMapper;
import com.dispatch.mapper.EmployeeMapper;
import com.dispatch.mapper.JobMapper;
import com.dispatch.mapper.SalaryMapper;
import com.dispatch.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {

    private final AttendanceMapper attendanceMapper;
    private final DispatchMapper dispatchMapper;
    private final JobMapper jobMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<Salary> pageList(int current, int size, Long employeeId, Integer year, Integer month) {
        LambdaQueryWrapper<Salary> wrapper = new LambdaQueryWrapper<>();
        if (employeeId != null) wrapper.eq(Salary::getEmployeeId, employeeId);
        if (year != null) wrapper.eq(Salary::getYear, year);
        if (month != null) wrapper.eq(Salary::getMonth, month);
        wrapper.orderByDesc(Salary::getYear).orderByDesc(Salary::getMonth);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public void calculate(Long dispatchId, Integer year, Integer month) {
        Dispatch dispatch = dispatchMapper.selectById(dispatchId);
        if (dispatch == null) throw new RuntimeException("派遣记录不存在");

        Job job = jobMapper.selectById(dispatch.getJobId());
        List<Attendance> attendances = attendanceMapper.selectList(
                new LambdaQueryWrapper<Attendance>()
                        .eq(Attendance::getDispatchId, dispatchId)
                        .apply("YEAR(work_date) = {0}", year)
                        .apply("MONTH(work_date) = {0}", month));

        BigDecimal totalHours = attendances.stream()
                .map(Attendance::getWorkHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal baseSalary = job.getSalaryMin().multiply(totalHours);

        Salary salary = new Salary();
        salary.setDispatchId(dispatchId);
        salary.setEmployeeId(dispatch.getEmployeeId());
        salary.setYear(year);
        salary.setMonth(month);
        salary.setBaseSalary(baseSalary);
        salary.setOvertime(BigDecimal.ZERO);
        salary.setDeduction(BigDecimal.ZERO);
        salary.setTotalAmount(baseSalary);
        salary.setStatus(1);
        save(salary);
    }

    @Override
    public List<Salary> getByUserId(Long userId, Integer year) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        if (employee == null) return Collections.emptyList();
        return list(new LambdaQueryWrapper<Salary>()
                .eq(Salary::getEmployeeId, employee.getId())
                .eq(Salary::getYear, year)
                .orderByAsc(Salary::getMonth));
    }
}
