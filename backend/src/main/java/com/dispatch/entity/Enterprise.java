package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("enterprise")
public class Enterprise {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String creditCode;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private Integer auditStatus; // 0-待审核 1-通过 2-拒绝
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
