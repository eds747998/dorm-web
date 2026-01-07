package com.dormitory.service.impl;

import com.dormitory.common.PageResult;
import com.dormitory.dto.RepairListDTO;
import com.dormitory.entity.Repair;
import com.dormitory.entity.SysUser;
import com.dormitory.mapper.RepairMapper;
import com.dormitory.mapper.SysUserMapper;
import com.dormitory.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修管理业务实现
 */
@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageResult<RepairListDTO> list(int pageNum, int pageSize, Long userId, Integer role) {
        Assert.notNull(role, "角色不能为空");
        int size = pageSize <= 0 ? 10 : pageSize;
        int page = Math.max(pageNum, 1);
        int offset = (page - 1) * size;

        long total = repairMapper.countWithUserAndRoom(role, userId);
        List<RepairListDTO> records = repairMapper.selectPageWithUserAndRoom(offset, size, role, userId);
        return new PageResult<>(total, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRepair(Long studentId, String content) {
        Assert.notNull(studentId, "student_id 不能为空");
        Assert.isTrue(StringUtils.hasText(content), "报修内容不能为空");

        SysUser student = sysUserMapper.selectById(studentId);
        Assert.notNull(student, "学生不存在");
        Assert.isTrue(student.getRole() != null && student.getRole() == 1, "只有学生才能报修");

        Long roomId = student.getRoomId();
        Assert.notNull(roomId, "未分配宿舍，无法报修");

        LocalDateTime now = LocalDateTime.now();
        Repair repair = new Repair();
        repair.setStudentId(studentId);
        repair.setRoomId(roomId);
        repair.setContent(content);
        repair.setStatus(0);
        repair.setCreateTime(now);
        repair.setUpdateTime(now);
        repairMapper.insert(repair);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finish(Long id) {
        Assert.notNull(id, "报修ID不能为空");
        Repair repair = repairMapper.selectById(id);
        Assert.notNull(repair, "报修记录不存在");

        // 已修复则直接返回
        if (repair.getStatus() != null && repair.getStatus() == 1) {
            return;
        }

        repairMapper.finish(id);
    }
}








