package org.example.myjinxiaocunclaude.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.entity.SysUser;
import org.example.myjinxiaocunclaude.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "登录模块")
public class LoginController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<SysUser> login(@RequestBody SysUser loginUser, HttpSession session) {
        // 1. 构建查询条件: WHERE username = ?
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());

        // 2. 查询数据库
        SysUser user = sysUserMapper.selectOne(queryWrapper);

        // 3. 校验密码
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            // 登录成功, 将用户信息存入 Session
            session.setAttribute("user", user);
            return Result.success("登录成功", user);
        }
        return Result.fail(401, "登录失败：用户名或密码错误");
    }

    @GetMapping("/logout")
    @Operation(summary = "用户登出")
    public Result<Object> logout(HttpSession session) {
        session.invalidate(); // 销毁 Session
        return Result.success("退出成功", null);
    }
}
