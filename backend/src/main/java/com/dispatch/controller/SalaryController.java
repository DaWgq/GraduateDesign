package com.dispatch.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Salary;
import com.dispatch.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping("/page")
    public Result<Page<Salary>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return Result.success(salaryService.pageList(current, size, employeeId, year, month));
    }

    @GetMapping("/my")
    public Result<List<Salary>> getMy(@RequestParam Integer year) {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(salaryService.getByUserId(userId, year));
    }

    @PostMapping("/calculate")
    public Result<Void> calculate(@RequestParam Long dispatchId,
                                  @RequestParam Integer year,
                                  @RequestParam Integer month) {
        salaryService.calculate(dispatchId, year, month);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Salary> getById(@PathVariable Long id) {
        return Result.success(salaryService.getById(id));
    }
}
