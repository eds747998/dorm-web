package com.dormitory.controller;

import com.dormitory.common.PageResult;
import com.dormitory.common.Result;
import com.dormitory.dto.RoomWithBuilding;
import com.dormitory.entity.Room;
import com.dormitory.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 宿舍管理控制器
 *
 * @author dormitory-system
 */
@Slf4j
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 分页查询宿舍列表（包含楼宇名称）
     */
    @GetMapping("/list")
    public Result<PageResult<RoomWithBuilding>> list(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<RoomWithBuilding> page = roomService.list(pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 新增宿舍
     */
    @PostMapping
    public Result<Void> add(@RequestBody Room room) {
        if (room.getBuildingId() == null) {
            return Result.error("楼宇ID不能为空");
        }
        if (!StringUtils.hasText(room.getRoomNum())) {
            return Result.error("房号不能为空");
        }
        if (room.getCapacity() == null || room.getCapacity() <= 0) {
            return Result.error("床位数需大于0");
        }

        try {
            roomService.addRoom(room);
        } catch (DataIntegrityViolationException e) {
            // 可能违反唯一约束 (building_id, room_num)
            log.warn("新增宿舍失败，可能重复: buildingId={}, roomNum={}", room.getBuildingId(), room.getRoomNum());
            return Result.error("该楼宇下房号已存在");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("新增成功", null);
    }

    /**
     * 修改宿舍
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        
        if (room.getCapacity() != null && room.getCapacity() <= 0) {
            return Result.error("床位数需大于0");
        }

        try {
            roomService.updateRoom(room);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.warn("修改宿舍失败，可能违反约束: id={}", id);
            return Result.error("修改失败，可能违反唯一约束");
        }
        return Result.success("修改成功", null);
    }

    /**
     * 删除宿舍
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("删除成功", null);
    }
}

