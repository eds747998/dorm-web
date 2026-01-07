package com.dormitory.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修管理实体类
 * 对应数据库表：repair
 *
 * @author dormitory-system
 */
@Data
public class Repair {

    /**
     * 报修ID，主键自增
     */
    private Long id;

    /**
     * 报修学生ID，外键关联sys_user表
     */
    private Long studentId;

    /**
     * 报修宿舍ID，外键关联room表
     */
    private Long roomId;

    /**
     * 报修内容描述
     */
    private String content;

    /**
     * 处理状态：0-待处理，1-已修复
     */
    private Integer status;

    /**
     * 报修创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

