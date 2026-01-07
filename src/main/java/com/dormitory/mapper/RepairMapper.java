package com.dormitory.mapper;

import com.dormitory.dto.RepairListDTO;
import com.dormitory.entity.Repair;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 报修管理 Mapper 接口
 * 对应数据库表：repair
 *
 * @author dormitory-system
 */
@Mapper
public interface RepairMapper {

    /**
     * 根据ID查询报修
     *
     * @param id 报修ID
     * @return 报修信息
     */
    @Select("SELECT id, student_id, room_id, content, status, create_time, update_time " +
            "FROM repair WHERE id = #{id}")
    Repair selectById(@Param("id") Long id);

    /**
     * 根据学生ID查询报修列表
     *
     * @param studentId 学生ID
     * @return 报修列表
     */
    @Select("SELECT id, student_id, room_id, content, status, create_time, update_time " +
            "FROM repair WHERE student_id = #{studentId} ORDER BY create_time DESC")
    List<Repair> selectByStudentId(@Param("studentId") Long studentId);

    /**
     * 根据宿舍ID查询报修列表
     *
     * @param roomId 宿舍ID
     * @return 报修列表
     */
    @Select("SELECT id, student_id, room_id, content, status, create_time, update_time " +
            "FROM repair WHERE room_id = #{roomId} ORDER BY create_time DESC")
    List<Repair> selectByRoomId(@Param("roomId") Long roomId);

    /**
     * 根据状态查询报修列表
     *
     * @param status 状态：0-待处理，1-已修复
     * @return 报修列表
     */
    @Select("SELECT id, student_id, room_id, content, status, create_time, update_time " +
            "FROM repair WHERE status = #{status} ORDER BY create_time DESC")
    List<Repair> selectByStatus(@Param("status") Integer status);

    /**
     * 查询所有报修
     *
     * @return 报修列表
     */
    @Select("SELECT id, student_id, room_id, content, status, create_time, update_time " +
            "FROM repair ORDER BY create_time DESC")
    List<Repair> selectAll();

    /**
     * 插入报修
     *
     * @param repair 报修信息
     * @return 影响行数
     */
    @Insert("INSERT INTO repair (student_id, room_id, content, status, create_time, update_time) " +
            "VALUES (#{studentId}, #{roomId}, #{content}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Repair repair);

    /**
     * 更新报修（动态SQL，使用XML）
     *
     * @param repair 报修信息
     * @return 影响行数
     */
    int update(Repair repair);

    /**
     * 根据ID删除报修
     *
     * @param id 报修ID
     * @return 影响行数
     */
    @Delete("DELETE FROM repair WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 分页查询报修列表，关联学生、宿舍、楼宇（复杂SQL使用XML）
     */
    List<RepairListDTO> selectPageWithUserAndRoom(@Param("offset") int offset,
                                                  @Param("size") int size,
                                                  @Param("role") Integer role,
                                                  @Param("userId") Long userId);

    /**
     * 统计报修总数
     */
    long countWithUserAndRoom(@Param("role") Integer role,
                              @Param("userId") Long userId);

    /**
     * 管理员处理报修：将状态置为已修复
     */
    @Update("UPDATE repair SET status = 1, update_time = NOW() WHERE id = #{id}")
    int finish(@Param("id") Long id);
}

