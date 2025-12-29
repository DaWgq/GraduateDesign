package com.dispatch.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MatchResult {
    private Long id;
    private String name;
    private String skills;
    private BigDecimal salary;
    private String location;
    private Integer matchScore; // 匹配度 0-100
    private String matchReason;
}
