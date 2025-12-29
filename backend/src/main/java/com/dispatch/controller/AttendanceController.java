package com.dispatch.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.entity.Attendance;
import com.dispatch.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/page")
    public Result<Page<Attendance>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(attendanceService.pageList(current, size, employeeId, startDate, endDate));
    }

    @GetMapping("/my")
    public Result<Page<Attendance>> getMy(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(attendanceService.getByUserId(userId, current, size, startDate, endDate));
    }

    @GetMapping("/{id}")
    public Result<Attendance> getById(@PathVariable Long id) {
        return Result.success(attendanceService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Attendance attendance) {
        attendanceService.save(attendance);
        return Result.success();
    }

    @PostMapping("/batch")
    public Result<Void> batchSave(@RequestBody List<Attendance> attendances) {
        attendanceService.batchSave(attendances);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Attendance attendance) {
        attendanceService.updateById(attendance);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        attendanceService.removeById(id);
        return Result.success();
    }

    @GetMapping("/monthly")
    public Result<List<Attendance>> getMonthly(
            @RequestParam Long dispatchId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return Result.success(attendanceService.getMonthlyAttendance(dispatchId, year, month));
    }
}
