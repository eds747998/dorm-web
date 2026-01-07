package com.dormitory.mapper;

import com.dormitory.dto.StudentWithRoomDTO;
import com.dormitory.entity.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 系统用户 Mapper 接口
 * 对应数据库表：sys_user
 *
 * @author dormitory-system
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT id, username, password, name, phone, role, room_id, stu_number, gender, college, major, class_name, create_time, update_time " +
            "FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据学号查询用户
     */
    @Select("SELECT id, username, password, name, phone, role, room_id, stu_number, gender, college, major, class_name, create_time, update_time " +
            "FROM sys_user WHERE stu_number = #{stuNumber}")
    SysUser selectByStuNumber(@Param("stuNumber") String stuNumber);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Select("SELECT id, username, password, name, phone, role, room_id, stu_number, gender, college, major, class_name, create_time, update_time " +
            "FROM sys_user WHERE id = #{id}")
    SysUser selectById(@Param("id") Long id);

    /**
     * 插入用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO sys_user (username, password, name, phone, role, room_id, stu_number, gender, college, major, class_name, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{name}, #{phone}, #{role}, #{roomId}, #{stuNumber}, #{gender}, #{college}, #{major}, #{className}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    /**
     * 更新用户（动态SQL，使用XML）
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int update(SysUser user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 分页查询学生列表，关联宿舍和楼宇
     */
    @Select("SELECT u.id, u.username, u.name, u.phone, u.role, u.room_id, u.stu_number, u.gender, u.college, u.major, u.class_name, u.create_time, u.update_time, " +
            "r.room_num AS roomNum, b.name AS buildingName " +
            "FROM sys_user u " +
            "LEFT JOIN room r ON u.room_id = r.id " +
            "LEFT JOIN building b ON r.building_id = b.id " +
            "WHERE u.role = 1 " +
            "ORDER BY u.id " +
            "LIMIT #{offset}, #{size}")
    @Results(id = "studentWithRoomMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "name", property = "name"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "role", property = "role"),
            @Result(column = "room_id", property = "roomId"),
            @Result(column = "stu_number", property = "stuNumber"),
            @Result(column = "gender", property = "gender"),
            @Result(column = "college", property = "college"),
            @Result(column = "major", property = "major"),
            @Result(column = "class_name", property = "className"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "roomNum", property = "roomNum"),
            @Result(column = "buildingName", property = "buildingName")
    })
    List<StudentWithRoomDTO> selectStudentPage(@Param("offset") int offset, @Param("size") int size);

    /**
     * 统计学生总数
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE role = 1")
    long countStudents();
}

