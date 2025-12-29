package com.dispatch.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStats {
    private Long employeeCount;
    private Long enterpriseCount;
    private Long jobCount;
    private Long dispatchCount;
    private Long onJobCount;
    private BigDecimal monthSalaryTotal;
}
