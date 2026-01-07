package com.dormitory.dto;

import lombok.Data;

/**
 * 登录请求 DTO
 *
 * @author dormitory-system
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色：0-管理员，1-学生
     */
    private Integer role;
}







