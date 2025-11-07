package com.example.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.AdminEntity;
import com.example.demo.exception.LoginFailedException;
import com.example.demo.exception.ValidateFailedException;
import com.example.demo.service.UserService;
import com.example.demo.service.AdminService;
import com.example.demo.utils.JwtUtils;
import com.example.demo.utils.ResultTemplate;
import com.example.demo.validator.UserValidateGroup;
import com.example.demo.validator.AdminValidateGroup;

@RestController
public class SecurityController {

    private final UserService userService;

    private final AdminService adminService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    public SecurityController(
        UserService userService,
        AdminService adminService,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils
    ) {
        this.userService        = userService;
        this.adminService       = adminService;
        this.jwtUtils           = jwtUtils;
        this.passwordEncoder    = passwordEncoder;
    }

    // 用户登录认证
    @PostMapping("/login")
    public ResultTemplate login(
        @RequestBody @Validated({ UserValidateGroup.Login.class }) UserEntity fields,
        BindingResult bindingResult
    ) {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 验证用户名和密码
        UserEntity user = this.userService.fetch(fields.getUsername()).orElseThrow(
            () -> new LoginFailedException()
        );
        // 用于加密后的密码进行校验
        if (!this.passwordEncoder.matches(fields.getPassword(), user.getPassword())) {
            throw new LoginFailedException();
        }

        // 生成JWT token
        String jwtToken = this.jwtUtils.generateToken(user);

        result.putPayload("user", user);
        result.putPayload("token", jwtToken);

        return result;
    }

    // 管理员登录认证
    @PostMapping("/admin/login")
    public ResultTemplate adminLogin(
        @RequestBody @Validated({ AdminValidateGroup.Login.class }) AdminEntity fields,
        BindingResult bindingResult
    ) {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 验证管理员用户名和密码
        AdminEntity admin = this.adminService.fetch(fields.getUsername()).orElseThrow(
            () -> new LoginFailedException()
        );
        // 校验加密后的密码
        if (!this.passwordEncoder.matches(fields.getPassword(), admin.getPassword())) {
            throw new LoginFailedException();
        }

        // 生成JWT token（可以使用相同的 JwtUtils，或创建专门的管理员 token）
        String jwtToken = this.jwtUtils.generateToken(admin);

        result.putPayload("user", admin);  // 前端统一使用 user 字段
        result.putPayload("token", jwtToken);

        return result;
    }

    @PostMapping("/logout")
    public ResultTemplate logout() {
        ResultTemplate result = new ResultTemplate();

        // 退出登录的逻辑可以在这里处理
        // 比如清除服务端的session、记录日志等
        // 由于使用了token认证，前端只需要清除本地存储的token即可

        result.putPayload("message", "退出登录成功");

        return result;
    }

}
