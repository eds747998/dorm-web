package com.dormitory.service;

import com.dormitory.common.PageResult;
import com.dormitory.dto.RepairListDTO;

/**
 * 报修管理业务接口
 */
public interface RepairService {

    /**
     * 报修列表分页
     *
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param userId 当前用户ID
     * @param role 当前角色 0管理员/1学生
     */
    PageResult<RepairListDTO> list(int pageNum, int pageSize, Long userId, Integer role);

    /**
     * 提交报修（学生）
     */
    void addRepair(Long studentId, String content);

    /**
     * 处理报修（管理员）
     */
    void finish(Long id);
}








