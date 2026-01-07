package com.dormitory.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体类
 * 对应数据库表：sys_user
 *
 * @author dormitory-system
 */
@Data
public class SysUser {

    /**
     * 用户ID，主键自增
     */
    private Long id;

    /**
     * 用户名/学号，用于登录，唯一
     */
    private String username;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 角色：0-管理员，1-学生
     */
    private Integer role;

    /**
     * 所属宿舍ID（仅学生有效）
     */
    private Long roomId;

    /**
     * 学号
     */
    private String stuNumber;

    /**
     * 性别
     */
    private String gender;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String className;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

