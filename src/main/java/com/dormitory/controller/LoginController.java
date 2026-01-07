package com.dormitory.controller;

import com.dormitory.common.Result;
import com.dormitory.dto.LoginDTO;
import com.dormitory.entity.SysUser;
import com.dormitory.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 *
 * @author dormitory-system
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 用户登录接口
     *
     * @param loginDTO 登录参数（username, password, role）
     * @return 登录结果
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello Dormitory System!";
    }

    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO) {
        // 参数校验
        if (!StringUtils.hasText(loginDTO.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(loginDTO.getPassword())) {
            return Result.error("密码不能为空");
        }
        if (loginDTO.getRole() == null) {
            return Result.error("角色不能为空");
        }

        // 查询用户
        SysUser user = sysUserMapper.selectByUsername(loginDTO.getUsername());

        // 验证用户是否存在
        if (user == null) {
            return Result.error("用户名不存在");
        }

        // 验证密码
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        // 验证角色
        if (!user.getRole().equals(loginDTO.getRole())) {
            return Result.error("角色不匹配");
        }

        // 登录成功，清除密码信息后返回
        user.setPassword(null);
        log.info("用户登录成功：{}", user.getUsername());
        return Result.success("登录成功", user);
    }
}

