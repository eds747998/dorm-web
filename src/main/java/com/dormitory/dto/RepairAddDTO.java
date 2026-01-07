package com.dormitory.dto;

import lombok.Data;

/**
 * 提交报修请求 DTO
 */
@Data
public class RepairAddDTO {

    /** 报修学生ID */
    private Long studentId;

    /** 报修内容 */
    private String content;
}








