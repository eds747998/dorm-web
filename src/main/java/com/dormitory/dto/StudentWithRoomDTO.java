package com.dormitory.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生列表返回 DTO，包含房间号与楼宇名称
 *
 * @author dormitory-system
 */
@Data
public class StudentWithRoomDTO {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private Integer role;

    private Long roomId;

    private String stuNumber;

    private String gender;

    private String college;

    private String major;

    private String className;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String roomNum;

    private String buildingName;
}







