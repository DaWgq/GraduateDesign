package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.entity.Employee;
import com.dispatch.mapper.EmployeeMapper;
import com.dispatch.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public Page<Employee> pageList(int current, int size, String keyword) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Employee::getName, keyword)
                    .or().like(Employee::getSkills, keyword);
        }
        wrapper.orderByDesc(Employee::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public Employee getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
    }
}
