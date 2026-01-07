package com.dormitory.controller;

import com.dormitory.common.PageResult;
import com.dormitory.common.Result;
import com.dormitory.dto.RepairAddDTO;
import com.dormitory.dto.RepairListDTO;
import com.dormitory.service.RepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报修管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 报修列表（分页，多角色区分）
     */
    @GetMapping("/list")
    public Result<PageResult<RepairListDTO>> list(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam Long userId,
                                                  @RequestParam Integer role) {
        PageResult<RepairListDTO> result = repairService.list(page, size, userId, role);
        return Result.success(result);
    }

    /**
     * 提交报修（学生）
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody RepairAddDTO dto) {
        try {
            repairService.addRepair(dto.getStudentId(), dto.getContent());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("提交成功", null);
    }

    /**
     * 处理报修（管理员）
     */
    @PostMapping("/finish/{id}")
    public Result<Void> finish(@PathVariable Long id) {
        try {
            repairService.finish(id);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
        return Result.success("处理成功", null);
    }
}








