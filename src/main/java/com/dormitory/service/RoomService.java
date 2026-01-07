package com.dormitory.service;

import com.dormitory.common.PageResult;
import com.dormitory.dto.RoomWithBuilding;
import com.dormitory.entity.Room;

/**
 * 宿舍业务接口
 *
 * @author dormitory-system
 */
public interface RoomService {

    /**
     * 分页查询宿舍，附带楼宇名称
     *
     * @param pageNum  页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页结果
     */
    PageResult<RoomWithBuilding> list(int pageNum, int pageSize);

    /**
     * 新增宿舍
     *
     * @param room 宿舍信息
     */
    void addRoom(Room room);

    /**
     * 修改宿舍
     *
     * @param room 宿舍信息（必须包含id）
     */
    void updateRoom(Room room);

    /**
     * 删除宿舍
     *
     * @param id 宿舍ID
     */
    void deleteRoom(Long id);
}

