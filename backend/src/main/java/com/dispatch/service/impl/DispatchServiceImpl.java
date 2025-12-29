package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Dispatch;
import com.dispatch.entity.Employee;
import com.dispatch.mapper.DispatchMapper;
import com.dispatch.mapper.EmployeeMapper;
import com.dispatch.service.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {

    private final EmployeeMapper employeeMapper;

    @Override
    public Page<Dispatch> pageList(int current, int size, Integer status) {
        LambdaQueryWrapper<Dispatch> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Dispatch::getStatus, status);
        }
        wrapper.orderByDesc(Dispatch::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Dispatch dispatch = getById(id);
        if (dispatch == null) {
            throw new RuntimeException("派遣记录不存在");
        }
        dispatch.setStatus(status);
        updateById(dispatch);
    }

    @Override
    public List<Dispatch> getByUserId(Long userId) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        if (employee == null) return Collections.emptyList();
        return list(new LambdaQueryWrapper<Dispatch>()
                .eq(Dispatch::getEmployeeId, employee.getId())
                .orderByDesc(Dispatch::getCreateTime));
    }

    @Override
    public void apply(Long userId, Long jobId, Long enterpriseId) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        if (employee == null) throw new RuntimeException("请先完善简历信息");

        Dispatch dispatch = new Dispatch();
        dispatch.setEmployeeId(employee.getId());
        dispatch.setJobId(jobId);
        dispatch.setEnterpriseId(enterpriseId);
        dispatch.setStatus(1);
        save(dispatch);
    }
}
