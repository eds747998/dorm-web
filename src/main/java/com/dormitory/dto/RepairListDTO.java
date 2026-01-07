package com.dormitory.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修列表展示 DTO
 */
@Data
public class RepairListDTO {

    /** 报修ID */
    private Long id;

    /** 报修人姓名 */
    private String studentName;

    /** 楼宇名称 */
    private String buildingName;

    /** 房间号 */
    private String roomNum;

    /** 宿舍位置（楼名+房号） */
    private String location;

    /** 报修内容 */
    private String content;

    /** 状态：0-待处理，1-已修复 */
    private Integer status;

    /** 申请时间 */
    private LocalDateTime createTime;
}








