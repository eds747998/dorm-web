-- ============================================
-- 高校宿舍管理系统数据库初始化脚本
-- ============================================
/*
 Navicat Premium Data Transfer
 Source Database       : dormitory_management
 Target Server Type    : MySQL
 Target Server Version : 8.0 (Recommend)
 File Encoding         : 65001

 Date: 2024-01-01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0; -- 临时关闭外键检查，防止删除表时报错

-- ============================================
-- 1. 数据库初始化
-- ============================================
DROP DATABASE IF EXISTS `dormitory_management`;
CREATE DATABASE `dormitory_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `dormitory_management`;

-- ============================================
-- 2. 楼宇表 (building)
-- 说明：基础数据，无外键依赖，最先创建
-- ============================================
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '楼宇ID，主键自增',
  `name` VARCHAR(101) NOT NULL COMMENT '楼宇名称，如：南区1号楼',
  `admin_name` VARCHAR(51) DEFAULT NULL COMMENT '宿管员姓名',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) COMMENT '楼宇名称唯一',
  KEY `idx_admin_name` (`admin_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍楼宇表';

-- ============================================
-- 3. 宿舍表 (room)
-- 说明：依赖 building 表
-- ============================================
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '宿舍ID，主键自增',
  `building_id` BIGINT NOT NULL COMMENT '所属楼宇ID，外键关联building表',
  `room_num` VARCHAR(20) NOT NULL COMMENT '房间号，如：101',
  `capacity` INT NOT NULL DEFAULT 4 COMMENT '床位容量（可住人数）',
  `occupied` INT NOT NULL DEFAULT 0 COMMENT '已住人数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_room` (`building_id`, `room_num`) COMMENT '同一栋楼内房间号唯一',
  KEY `idx_building_id` (`building_id`),
  KEY `idx_room_num` (`room_num`),
  CONSTRAINT `fk_room_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍房间表';

-- ============================================
-- 4. 用户表 (sys_user)
-- 说明：整合了学生信息字段和宿舍关联字段
-- ============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
  `username` VARCHAR(51) NOT NULL COMMENT '用户名/学号，用于登录，唯一',
  `password` VARCHAR(256) NOT NULL COMMENT '密码',
  `name` VARCHAR(51) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(21) DEFAULT NULL COMMENT '联系电话',
  `role` TINYINT NOT NULL DEFAULT 1 COMMENT '角色：0-管理员，1-学生',
  
  -- 以下是整合进来的学生专属字段
  `room_id` BIGINT DEFAULT NULL COMMENT '所属宿舍ID（仅学生有效）',
  `stu_number` VARCHAR(30) DEFAULT NULL COMMENT '学号',
  `gender` VARCHAR(10) DEFAULT '男' COMMENT '性别',
  `college` VARCHAR(50) DEFAULT NULL COMMENT '学院',
  `major` VARCHAR(50) DEFAULT NULL COMMENT '专业',
  `class_name` VARCHAR(50) DEFAULT NULL COMMENT '班级',
  
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_stu_number` (`stu_number`) COMMENT '学号必须唯一',
  KEY `idx_role` (`role`),
  KEY `idx_room_id` (`room_id`),
  -- 外键约束：确保学生分配的宿舍是存在的
  CONSTRAINT `fk_user_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表（含学生信息）';

-- ============================================
-- 5. 报修表 (repair)
-- 说明：依赖 sys_user 和 room 表
-- ============================================
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报修ID，主键自增',
  `student_id` BIGINT NOT NULL COMMENT '报修学生ID',
  `room_id` BIGINT NOT NULL COMMENT '报修宿舍ID',
  `content` TEXT NOT NULL COMMENT '报修内容描述',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0-待处理，1-已修复',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_repair_student` FOREIGN KEY (`student_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_repair_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修管理表';

SET FOREIGN_KEY_CHECKS = 1; -- 恢复外键检查




-- ============================================
-- 1. 插入楼宇数据 (时间：2025年9月开学前)
-- ============================================
INSERT INTO `building` (`id`, `name`, `admin_name`, `create_time`) VALUES
                                                                       (1, '南区1号楼', '王宿管', '2025-09-01 09:00:00'),
                                                                       (2, '北区2号楼', '李宿管', '2025-09-03 14:30:00');

-- ============================================
-- 2. 插入宿舍数据 (时间：楼宇建成后)
-- 说明：
-- 101房 (4人间) -> 住了2人 (occupied=2)
-- 102房 (4人间) -> 没住人 (occupied=0)
-- 201房 (6人间) -> 住了1人 (occupied=1)
-- ============================================
INSERT INTO `room` (`id`, `building_id`, `room_num`, `capacity`, `occupied`, `create_time`) VALUES
                                                                                                (1, 1, '101', 4, 2, '2025-09-05 10:00:00'),
                                                                                                (2, 1, '102', 4, 0, '2025-09-05 10:15:00'),
                                                                                                (3, 2, '201', 6, 1, '2025-09-06 11:20:00');

-- ============================================
-- 3. 插入用户数据 (管理员 + 学生)
-- 时间：错开的学生报到时间
-- 密码：统一为 123456 (注意：实际项目如果用了加密，这里需存密文)
-- ============================================

-- 3.1 插入管理员 (ID=1)
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `role`, `create_time`) VALUES
    (1, 'admin', '123456', '超级管理员', 0, '2025-08-20 08:00:00');

-- 3.2 插入学生 (ID=2, 张三, 住101)
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `phone`, `role`, `room_id`, `stu_number`, `gender`, `college`, `major`, `class_name`, `create_time`) VALUES
    (2, '2023001', '123456', '张三', '13800138001', 1, 1, '2023001', '男', '计算机学院', '软件工程', '软工1班', '2025-09-10 09:30:00');

-- 3.3 插入学生 (ID=3, 李四, 住101, 张三的室友)
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `phone`, `role`, `room_id`, `stu_number`, `gender`, `college`, `major`, `class_name`, `create_time`) VALUES
    (3, '2023002', '123456', '李四', '13800138002', 1, 1, '2023002', '男', '计算机学院', '软件工程', '软工1班', '2025-09-11 15:45:00');

-- 3.4 插入学生 (ID=4, 王小红, 住201)
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `phone`, `role`, `room_id`, `stu_number`, `gender`, `college`, `major`, `class_name`, `create_time`) VALUES
    (4, '2023003', '123456', '王小红', '13800138003', 1, 3, '2023003', '女', '外语学院', '英语', '英语2班', '2025-09-12 11:00:00');

-- ============================================
-- 4. 插入报修数据 (时间：学期中途)
-- ============================================

-- 记录1：张三报修了101的灯，状态：已修复 (status=1)
INSERT INTO `repair` (`student_id`, `room_id`, `content`, `status`, `create_time`, `update_time`) VALUES
    (2, 1, '宿舍阳台灯不亮了，麻烦来看下', 1, '2025-10-15 19:20:00', '2025-10-16 10:00:00');

-- 记录2：王小红报修了201的空调，状态：待处理 (status=0)
INSERT INTO `repair` (`student_id`, `room_id`, `content`, `status`, `create_time`) VALUES
    (4, 3, '空调制冷效果很差，还漏水', 0, '2025-12-20 14:30:00');






