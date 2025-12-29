package com.dispatch.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Employee;
import com.dispatch.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/page")
    public Result<Page<Employee>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(employeeService.pageList(current, size, keyword));
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        return Result.success(employeeService.getById(id));
    }

    @GetMapping("/my")
    public Result<Employee> getMy() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(employeeService.getByUserId(userId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Employee employee) {
        employeeService.save(employee);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Employee employee) {
        employeeService.updateById(employee);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.removeById(id);
        return Result.success();
    }
}
