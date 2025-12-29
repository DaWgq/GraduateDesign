package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("contract")
public class Contract {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dispatchId;
    private Long employeeId;
    private Long enterpriseId;
    private String contractNo;
    private Integer contractType; // 1-劳务派遣合同 2-用工协议
    private LocalDate startDate;
    private LocalDate endDate;
    private String fileUrl;
    private Integer status; // 0-待签署 1-已签署 2-已终止
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
