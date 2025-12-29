package com.dispatch.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalaryReport {
    private Integer month;
    private Long employeeCount;
    private BigDecimal totalSalary;
    private BigDecimal avgSalary;
}
