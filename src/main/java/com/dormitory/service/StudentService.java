package com.dormitory.service;

import com.dormitory.common.PageResult;
import com.dormitory.dto.StudentWithRoomDTO;
import com.dormitory.entity.SysUser;

/**
 * 学生入住管理业务接口
 *
 * @author dormitory-system
 */
public interface StudentService {

    /**
     * 分页查询学生列表（含楼宇名称、房间号）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<StudentWithRoomDTO> list(int pageNum, int pageSize);

    /**
     * 学生入住 / 添加学生
     */
    void addStudent(SysUser student);

    /**
     * 删除学生（退宿）
     */
    void deleteStudent(Long id);

    /**
     * 修改学生入住信息（可换宿舍）
     */
    void updateStudent(SysUser student);
}







