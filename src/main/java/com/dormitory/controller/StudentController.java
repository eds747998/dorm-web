package com.dormitory.controller;

import com.dormitory.common.PageResult;
import com.dormitory.common.Result;
import com.dormitory.dto.StudentWithRoomDTO;
import com.dormitory.entity.SysUser;
import com.dormitory.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

/**
 * 学生入住管理控制器
 *
 * @author dormitory-system
 */
@Slf4j
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询学生列表（含楼宇名称、房间号）
     */
    @GetMapping("/list")
    public Result<PageResult<StudentWithRoomDTO>> list(@RequestParam(defaultValue = "1") int pageNum,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(studentService.list(pageNum, pageSize));
    }

    /**
     * 学生入住 / 添加学生
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysUser student) {
        try {
            studentService.addStudent(student);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return Result.error("用户名或学号已存在");
        }
        return Result.success("新增成功", null);
    }

    /**
     * 退宿 / 删除学生
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("删除成功", null);
    }

    /**
     * 修改学生入住信息（含换宿舍）
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysUser student) {
        student.setId(id);
        try {
            studentService.updateStudent(student);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return Result.error("更新失败，可能存在唯一约束冲突");
        }
        return Result.success("修改成功", null);
    }
}







