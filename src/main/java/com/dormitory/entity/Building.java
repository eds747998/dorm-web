package com.dormitory.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍楼宇实体类
 * 对应数据库表：building
 *
 * @author dormitory-system
 */
@Data
public class Building {

    /**
     * 楼宇ID，主键自增
     */
    private Long id;

    /**
     * 楼宇名称，如：1号楼、A栋等
     */
    private String name;

    /**
     * 宿管员姓名
     */
    private String adminName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

