package com.dormitory.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍列表返回 DTO，包含楼宇名称
 *
 * @author dormitory-system
 */
@Data
public class RoomWithBuilding {

    private Long id;

    private Long buildingId;

    private String buildingName;

    private String roomNum;

    private Integer capacity;

    private Integer occupied;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}







