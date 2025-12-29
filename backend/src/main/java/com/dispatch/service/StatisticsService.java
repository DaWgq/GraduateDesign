package com.dispatch.service;

import com.dispatch.dto.DashboardStats;
import com.dispatch.dto.SalaryReport;
import java.util.List;

public interface StatisticsService {
    DashboardStats getDashboardStats();
    List<SalaryReport> getSalaryReport(Integer year, Long enterpriseId);
    List<Object> getDispatchTrend(Integer year);
}
