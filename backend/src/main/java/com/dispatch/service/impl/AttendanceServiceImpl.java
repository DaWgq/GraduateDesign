package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Attendance;
import com.dispatch.entity.Employee;
import com.dispatch.mapper.AttendanceMapper;
import com.dispatch.mapper.EmployeeMapper;
import com.dispatch.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    private final EmployeeMapper employeeMapper;

    @Override
    public Page<Attendance> pageList(int current, int size, Long employeeId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        if (employeeId != null) wrapper.eq(Attendance::getEmployeeId, employeeId);
        if (startDate != null) wrapper.ge(Attendance::getWorkDate, startDate);
        if (endDate != null) wrapper.le(Attendance::getWorkDate, endDate);
        wrapper.orderByDesc(Attendance::getWorkDate);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public void batchSave(List<Attendance> attendances) {
        saveBatch(attendances);
    }

    @Override
    public List<Attendance> getMonthlyAttendance(Long dispatchId, Integer year, Integer month) {
        return list(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getDispatchId, dispatchId)
                .apply("YEAR(work_date) = {0}", year)
                .apply("MONTH(work_date) = {0}", month));
    }

    @Override
    public Page<Attendance> getByUserId(Long userId, int current, int size, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        if (employee == null) return new Page<>(current, size);
        return pageList(current, size, employee.getId(), startDate, endDate);
    }
}
