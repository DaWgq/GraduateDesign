package com.dispatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.entity.Salary;

import java.util.List;

public interface SalaryService extends IService<Salary> {
    Page<Salary> pageList(int current, int size, Long employeeId, Integer year, Integer month);
    void calculate(Long dispatchId, Integer year, Integer month);
    List<Salary> getByUserId(Long userId, Integer year);
}
