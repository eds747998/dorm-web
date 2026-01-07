package com.dormitory.mapper;

import com.dormitory.dto.RoomWithBuilding;
import com.dormitory.entity.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 宿舍房间 Mapper 接口
 * 对应数据库表：room
 *
 * @author dormitory-system
 */
@Mapper
public interface RoomMapper {

    /**
     * 根据ID查询宿舍
     *
     * @param id 宿舍ID
     * @return 宿舍信息
     */
    @Select("SELECT id, building_id, room_num, capacity, occupied, create_time, update_time " +
            "FROM room WHERE id = #{id}")
    Room selectById(@Param("id") Long id);

    /**
     * 根据楼宇ID查询宿舍列表
     *
     * @param buildingId 楼宇ID
     * @return 宿舍列表
     */
    @Select("SELECT id, building_id, room_num, capacity, occupied, create_time, update_time " +
            "FROM room WHERE building_id = #{buildingId} ORDER BY room_num")
    List<Room> selectByBuildingId(@Param("buildingId") Long buildingId);

    /**
     * 查询所有宿舍
     *
     * @return 宿舍列表
     */
    @Select("SELECT id, building_id, room_num, capacity, occupied, create_time, update_time " +
            "FROM room ORDER BY building_id, room_num")
    List<Room> selectAll();

    /**
     * 分页查询宿舍并关联楼宇名称
     *
     * @param offset 偏移量
     * @param size   数量
     * @return 宿舍列表（含楼宇名）
     */
    @Select("SELECT r.id, r.building_id, r.room_num, r.capacity, r.occupied, r.create_time, r.update_time, b.name AS building_name " +
            "FROM room r LEFT JOIN building b ON r.building_id = b.id " +
            "ORDER BY r.building_id, r.room_num " +
            "LIMIT #{offset}, #{size}")
    @Results(id = "roomWithBuildingMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "building_id", property = "buildingId"),
            @Result(column = "building_name", property = "buildingName"),
            @Result(column = "room_num", property = "roomNum"),
            @Result(column = "capacity", property = "capacity"),
            @Result(column = "occupied", property = "occupied"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<RoomWithBuilding> selectPageWithBuilding(@Param("offset") int offset, @Param("size") int size);

    /**
     * 统计宿舍总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(1) FROM room")
    long countAll();

    /**
     * 插入宿舍
     *
     * @param room 宿舍信息
     * @return 影响行数
     */
    @Insert("INSERT INTO room (building_id, room_num, capacity, occupied, create_time, update_time) " +
            "VALUES (#{buildingId}, #{roomNum}, #{capacity}, #{occupied}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);

    /**
     * 更新宿舍（动态SQL，使用XML）
     *
     * @param room 宿舍信息
     * @return 影响行数
     */
    int update(Room room);

    /**
     * 根据ID删除宿舍
     *
     * @param id 宿舍ID
     * @return 影响行数
     */
    @Delete("DELETE FROM room WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}

