package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("dispatch")
public class Dispatch {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private Long jobId;
    private Long enterpriseId;
    private Integer status; // 1-面试邀约 2-录用 3-签约 4-入职 5-在职 6-离职 7-退回
    private LocalDate startDate;
    private LocalDate endDate;
    private String remark;
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
