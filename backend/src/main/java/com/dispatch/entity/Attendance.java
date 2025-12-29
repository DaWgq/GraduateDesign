package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("attendance")
public class Attendance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dispatchId;
    private Long employeeId;
    private LocalDate workDate;
    private BigDecimal workHours;
    private Integer status; // 1-正常 2-迟到 3-早退 4-缺勤
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
