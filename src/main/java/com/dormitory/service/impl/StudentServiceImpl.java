package com.dormitory.service.impl;

import com.dormitory.common.PageResult;
import com.dormitory.dto.StudentWithRoomDTO;
import com.dormitory.entity.Room;
import com.dormitory.entity.SysUser;
import com.dormitory.mapper.RoomMapper;
import com.dormitory.mapper.SysUserMapper;
import com.dormitory.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生入住管理业务实现
 *
 * @author dormitory-system
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public PageResult<StudentWithRoomDTO> list(int pageNum, int pageSize) {
        int size = pageSize <= 0 ? 10 : pageSize;
        int page = Math.max(pageNum, 1);
        int offset = (page - 1) * size;
        long total = sysUserMapper.countStudents();
        List<StudentWithRoomDTO> records = sysUserMapper.selectStudentPage(offset, size);
        return new PageResult<>(total, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(SysUser student) {
        validateStudentBasic(student);
        Assert.notNull(student.getRoomId(), "room_id 不能为空");

        // 查重：username / stu_number
        if (sysUserMapper.selectByUsername(student.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (StringUtils.hasText(student.getStuNumber()) && sysUserMapper.selectByStuNumber(student.getStuNumber()) != null) {
            throw new IllegalArgumentException("学号已存在");
        }

        // 容量检查
        Room room = roomMapper.selectById(student.getRoomId());
        Assert.notNull(room, "宿舍不存在");
        if (room.getOccupied() >= room.getCapacity()) {
            throw new IllegalArgumentException("该房间已满，无法入住");
        }

        // 落库
        LocalDateTime now = LocalDateTime.now();
        student.setRole(1);
        student.setCreateTime(now);
        student.setUpdateTime(now);
        try {
            sysUserMapper.insert(student);
        } catch (DataIntegrityViolationException e) {
            // 再保险：唯一约束
            throw new IllegalArgumentException("用户名或学号已存在");
        }

        // 更新房间占用
        Room updateRoom = new Room();
        updateRoom.setId(room.getId());
        updateRoom.setOccupied(room.getOccupied() + 1);
        updateRoom.setUpdateTime(now);
        roomMapper.update(updateRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Long id) {
        Assert.notNull(id, "学生ID不能为空");
        SysUser student = sysUserMapper.selectById(id);
        Assert.notNull(student, "学生不存在");
        Assert.isTrue(student.getRole() != null && student.getRole() == 1, "非学生角色无法删除");

        Long roomId = student.getRoomId();
        sysUserMapper.deleteById(id);

        if (roomId != null) {
            Room room = roomMapper.selectById(roomId);
            if (room != null && room.getOccupied() != null && room.getOccupied() > 0) {
                Room updateRoom = new Room();
                updateRoom.setId(room.getId());
                updateRoom.setOccupied(room.getOccupied() - 1);
                updateRoom.setUpdateTime(LocalDateTime.now());
                roomMapper.update(updateRoom);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(SysUser student) {
        Assert.notNull(student.getId(), "学生ID不能为空");
        SysUser existing = sysUserMapper.selectById(student.getId());
        Assert.notNull(existing, "学生不存在");
        Assert.isTrue(existing.getRole() != null && existing.getRole() == 1, "非学生角色无法修改");

        // 如果修改了用户名或学号，需要查重
        if (StringUtils.hasText(student.getUsername()) && !student.getUsername().equals(existing.getUsername())) {
            if (sysUserMapper.selectByUsername(student.getUsername()) != null) {
                throw new IllegalArgumentException("用户名已存在");
            }
        }
        if (StringUtils.hasText(student.getStuNumber()) && (existing.getStuNumber() == null || !student.getStuNumber().equals(existing.getStuNumber()))) {
            if (sysUserMapper.selectByStuNumber(student.getStuNumber()) != null) {
                throw new IllegalArgumentException("学号已存在");
            }
        }

        Long oldRoomId = existing.getRoomId();
        Long newRoomId = student.getRoomId() != null ? student.getRoomId() : existing.getRoomId();

        // 如果更换宿舍，需要容量校验
        if (newRoomId != null && !newRoomId.equals(oldRoomId)) {
            Room newRoom = roomMapper.selectById(newRoomId);
            Assert.notNull(newRoom, "新宿舍不存在");
            if (newRoom.getOccupied() >= newRoom.getCapacity()) {
                throw new IllegalArgumentException("该房间已满，无法入住");
            }
        }

        // 校验床位数/occupied与学生无关，这里只处理更换宿舍的占用变更
        LocalDateTime now = LocalDateTime.now();
        student.setUpdateTime(now);
        sysUserMapper.update(student);

        // 调整宿舍占用
        if (newRoomId != null && !newRoomId.equals(oldRoomId)) {
            // 新增宿舍 +1
            Room newRoom = roomMapper.selectById(newRoomId);
            Room updateNew = new Room();
            updateNew.setId(newRoomId);
            updateNew.setOccupied(newRoom.getOccupied() + 1);
            updateNew.setUpdateTime(now);
            roomMapper.update(updateNew);

            // 原宿舍 -1
            if (oldRoomId != null) {
                Room oldRoom = roomMapper.selectById(oldRoomId);
                if (oldRoom != null && oldRoom.getOccupied() != null && oldRoom.getOccupied() > 0) {
                    Room updateOld = new Room();
                    updateOld.setId(oldRoomId);
                    updateOld.setOccupied(oldRoom.getOccupied() - 1);
                    updateOld.setUpdateTime(now);
                    roomMapper.update(updateOld);
                }
            }
        }
    }

    private void validateStudentBasic(SysUser student) {
        Assert.hasText(student.getUsername(), "用户名不能为空");
        Assert.hasText(student.getPassword(), "密码不能为空");
        Assert.hasText(student.getName(), "姓名不能为空");
        Assert.hasText(student.getStuNumber(), "学号不能为空");
    }
}

