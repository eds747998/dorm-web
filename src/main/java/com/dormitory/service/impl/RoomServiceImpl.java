package com.dormitory.service.impl;

import com.dormitory.common.PageResult;
import com.dormitory.dto.RoomWithBuilding;
import com.dormitory.entity.Room;
import com.dormitory.mapper.RoomMapper;
import com.dormitory.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍业务实现
 *
 * @author dormitory-system
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public PageResult<RoomWithBuilding> list(int pageNum, int pageSize) {
        int size = pageSize <= 0 ? 10 : pageSize;
        int page = Math.max(pageNum, 1);
        int offset = (page - 1) * size;

        long total = roomMapper.countAll();
        List<RoomWithBuilding> records = roomMapper.selectPageWithBuilding(offset, size);
        return new PageResult<>(total, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoom(Room room) {
        Assert.notNull(room.getBuildingId(), "楼宇ID不能为空");
        Assert.hasText(room.getRoomNum(), "房号不能为空");
        Assert.isTrue(room.getCapacity() != null && room.getCapacity() > 0, "床位数需大于0");

        room.setOccupied(room.getOccupied() == null ? 0 : room.getOccupied());
        LocalDateTime now = LocalDateTime.now();
        room.setCreateTime(now);
        room.setUpdateTime(now);

        try {
            roomMapper.insert(room);
        } catch (DataIntegrityViolationException e) {
            // 可能违反唯一约束（building_id, room_num）
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(Room room) {
        Assert.notNull(room.getId(), "宿舍ID不能为空");
        
        // 检查宿舍是否存在
        Room existing = roomMapper.selectById(room.getId());
        Assert.notNull(existing, "宿舍不存在");

        // 如果修改了楼宇ID或房号，需要检查唯一约束
        if (room.getBuildingId() != null && !room.getBuildingId().equals(existing.getBuildingId()) ||
            (room.getRoomNum() != null && !room.getRoomNum().equals(existing.getRoomNum()))) {
            // 检查新楼宇ID和房号组合是否已存在
            Long checkBuildingId = room.getBuildingId() != null ? room.getBuildingId() : existing.getBuildingId();
            String newRoomNum = room.getRoomNum() != null ? room.getRoomNum() : existing.getRoomNum();
            List<Room> rooms = roomMapper.selectByBuildingId(checkBuildingId);
            
            for (Room r : rooms) {
                if (r.getRoomNum().equals(newRoomNum) && !r.getId().equals(room.getId())) {
                    throw new IllegalArgumentException("该楼宇下房号已存在");
                }
            }
        }

        // 校验床位数
        if (room.getCapacity() != null && room.getCapacity() <= 0) {
            throw new IllegalArgumentException("床位数需大于0");
        }

        // 校验已住人数不能超过床位数
        int finalCapacity = room.getCapacity() != null ? room.getCapacity() : existing.getCapacity();
        int finalOccupied = room.getOccupied() != null ? room.getOccupied() : existing.getOccupied();
        if (finalOccupied > finalCapacity) {
            throw new IllegalArgumentException("已住人数不能超过床位数");
        }

        room.setUpdateTime(LocalDateTime.now());
        roomMapper.update(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Long id) {
        Assert.notNull(id, "宿舍ID不能为空");
        
        // 检查宿舍是否存在
        Room room = roomMapper.selectById(id);
        Assert.notNull(room, "宿舍不存在");

        // 检查是否还有学生居住（已住人数大于0）
        if (room.getOccupied() != null && room.getOccupied() > 0) {
            throw new IllegalArgumentException("该宿舍仍有学生居住，无法删除");
        }

        roomMapper.deleteById(id);
    }
}

