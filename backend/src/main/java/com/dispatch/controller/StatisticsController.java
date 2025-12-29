package com.dispatch.controller;

import com.dispatch.common.Result;
import com.dispatch.dto.DashboardStats;
import com.dispatch.dto.SalaryReport;
import com.dispatch.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public Result<DashboardStats> getDashboardStats() {
        return Result.success(statisticsService.getDashboardStats());
    }

    @GetMapping("/salary/report")
    public Result<List<SalaryReport>> getSalaryReport(
            @RequestParam Integer year,
            @RequestParam(required = false) Long enterpriseId) {
        return Result.success(statisticsService.getSalaryReport(year, enterpriseId));
    }

    @GetMapping("/dispatch/trend")
    public Result<List<Object>> getDispatchTrend(@RequestParam Integer year) {
        return Result.success(statisticsService.getDispatchTrend(year));
    }
}
