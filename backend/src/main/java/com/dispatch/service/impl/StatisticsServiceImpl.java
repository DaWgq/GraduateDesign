package com.dispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dispatch.dto.DashboardStats;
import com.dispatch.dto.SalaryReport;
import com.dispatch.entity.*;
import com.dispatch.mapper.*;
import com.dispatch.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final EmployeeMapper employeeMapper;
    private final EnterpriseMapper enterpriseMapper;
    private final JobMapper jobMapper;
    private final DispatchMapper dispatchMapper;
    private final SalaryMapper salaryMapper;

    @Override
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        stats.setEmployeeCount(employeeMapper.selectCount(null));
        stats.setEnterpriseCount(enterpriseMapper.selectCount(
                new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getAuditStatus, 1)));
        stats.setJobCount(jobMapper.selectCount(
                new LambdaQueryWrapper<Job>().eq(Job::getStatus, 1)));
        stats.setDispatchCount(dispatchMapper.selectCount(null));
        stats.setOnJobCount(dispatchMapper.selectCount(
                new LambdaQueryWrapper<Dispatch>().eq(Dispatch::getStatus, 5)));

        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        List<Salary> monthSalaries = salaryMapper.selectList(
                new LambdaQueryWrapper<Salary>()
                        .eq(Salary::getYear, currentYear)
                        .eq(Salary::getMonth, currentMonth));
        BigDecimal total = monthSalaries.stream()
                .map(Salary::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setMonthSalaryTotal(total);

        return stats;
    }

    @Override
    public List<SalaryReport> getSalaryReport(Integer year, Long enterpriseId) {
        LambdaQueryWrapper<Salary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Salary::getYear, year);

        List<Salary> salaries = salaryMapper.selectList(wrapper);

        Map<Integer, List<Salary>> byMonth = salaries.stream()
                .collect(Collectors.groupingBy(Salary::getMonth));

        List<SalaryReport> reports = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            SalaryReport report = new SalaryReport();
            report.setMonth(month);
            List<Salary> monthData = byMonth.getOrDefault(month, Collections.emptyList());
            report.setEmployeeCount((long) monthData.size());
            BigDecimal total = monthData.stream()
                    .map(Salary::getTotalAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            report.setTotalSalary(total);
            report.setAvgSalary(monthData.isEmpty() ? BigDecimal.ZERO :
                    total.divide(BigDecimal.valueOf(monthData.size()), 2, RoundingMode.HALF_UP));
            reports.add(report);
        }
        return reports;
    }

    @Override
    public List<Object> getDispatchTrend(Integer year) {
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            int m = month;
            long count = dispatchMapper.selectCount(
                    new LambdaQueryWrapper<Dispatch>()
                            .apply("YEAR(create_time) = {0}", year)
                            .apply("MONTH(create_time) = {0}", m));
            Map<String, Object> item = new HashMap<>();
            item.put("month", month);
            item.put("count", count);
            trend.add(item);
        }
        return new ArrayList<>(trend);
    }
}
