package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Page<Employee> pageList(int current, int size, String keyword);
    Employee getByUserId(Long userId);
}
