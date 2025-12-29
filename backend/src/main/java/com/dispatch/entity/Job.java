package com.dispatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("job")
public class Job {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long enterpriseId;
    private String title;
    private String description;
    private String skills;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Integer salaryType; // 1-时薪 2-日薪 3-月薪
    private String location;
    private Integer headcount;
    private Integer status; // 0-关闭 1-招聘中
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
