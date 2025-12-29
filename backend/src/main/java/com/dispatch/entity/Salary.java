package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("salary")
public class Salary {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dispatchId;
    private Long employeeId;
    private Integer year;
    private Integer month;
    private BigDecimal baseSalary;
    private BigDecimal overtime;
    private BigDecimal deduction;
    private BigDecimal totalAmount;
    private Integer status; // 0-待核算 1-已核算 2-已发放
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
