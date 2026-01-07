package com.dormitory.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍房间实体类
 * 对应数据库表：room
 *
 * @author dormitory-system
 */
@Data
public class Room {

    /**
     * 宿舍ID，主键自增
     */
    private Long id;

    /**
     * 所属楼宇ID，外键关联building表
     */
    private Long buildingId;

    /**
     * 房间号，如：101、201等
     */
    private String roomNum;

    /**
     * 床位容量（可住人数）
     */
    private Integer capacity;

    /**
     * 已住人数
     */
    private Integer occupied;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

