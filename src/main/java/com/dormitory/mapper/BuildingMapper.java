package com.dormitory.mapper;

import com.dormitory.entity.Building;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 宿舍楼宇 Mapper 接口
 * 对应数据库表：building
 *
 * @author dormitory-system
 */
@Mapper
public interface BuildingMapper {

    /**
     * 根据ID查询楼宇
     *
     * @param id 楼宇ID
     * @return 楼宇信息
     */
    @Select("SELECT id, name, admin_name, create_time, update_time " +
            "FROM building WHERE id = #{id}")
    Building selectById(@Param("id") Long id);

    /**
     * 查询所有楼宇
     *
     * @return 楼宇列表
     */
    @Select("SELECT id, name, admin_name, create_time, update_time " +
            "FROM building ORDER BY id")
    List<Building> selectAll();

    /**
     * 插入楼宇
     *
     * @param building 楼宇信息
     * @return 影响行数
     */
    @Insert("INSERT INTO building (name, admin_name, create_time, update_time) " +
            "VALUES (#{name}, #{adminName}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Building building);

    /**
     * 更新楼宇（动态SQL，使用XML）
     *
     * @param building 楼宇信息
     * @return 影响行数
     */
    int update(Building building);

    /**
     * 根据ID删除楼宇
     *
     * @param id 楼宇ID
     * @return 影响行数
     */
    @Delete("DELETE FROM building WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}

