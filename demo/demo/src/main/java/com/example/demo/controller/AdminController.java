package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.AdminEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidateFailedException;
import com.example.demo.service.AdminService;
import com.example.demo.utils.ResultTemplate;
import com.example.demo.validator.AdminValidateGroup;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final PasswordEncoder passwordEncoder;

    public AdminController(
        AdminService adminService,
        PasswordEncoder passwordEncoder
    ) {
        this.adminService       = adminService;
        this.passwordEncoder    = passwordEncoder;
    }

    @GetMapping("")
    public ResultTemplate index(
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "keywords", required = false) String keywords
    ) {
        ResultTemplate result = new ResultTemplate();

        // 使用 MyBatis-Plus 分页查询
        IPage<AdminEntity> pageResult = this.adminService.page(page, pageSize, keywords);

        // 将分页结果放入响应
        result.putPayload("admins", pageResult.getRecords());  // 当前页的数据列表

        // 构建分页信息
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", pageResult.getTotal());
        pagination.put("pageSize", pageResult.getSize());
        pagination.put("currentPage", pageResult.getCurrent());
        pagination.put("pageCount", pageResult.getPages());
        result.putPayload("pagination", pagination);

        return result;
    }

    @PostMapping("/create")
    public ResultTemplate create(
        @RequestBody @Validated({AdminValidateGroup.Create.class}) AdminEntity admin,
        BindingResult bindingResult
    ) {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 检查管理员用户名是否已存在
        if (this.adminService.fetch(admin.getUsername()).isPresent()) {
            result.setStatus(false);
            result.setMessage("管理员用户名已存在，请选择其他用户名");
            return result;
        }

        // 加密密码
        admin.setPassword(
            this.passwordEncoder.encode(admin.getPassword())
        );
        admin.setRegisteredAt(LocalDateTime.now());
        this.adminService.create(admin);

        return result;
    }

    @GetMapping("/{id}")
    public ResultTemplate fetch(
        @PathVariable("id") Integer id
    ) throws NotFoundException {
        ResultTemplate result = new ResultTemplate();

        AdminEntity admin = this.adminService.fetch(id).orElseThrow(
            () -> new NotFoundException()
        );

        result.putPayload("admin", admin);

        return result;
    }

    @PostMapping("/update")
    public ResultTemplate update(
        @RequestBody @Validated(AdminValidateGroup.Update.class) AdminEntity admin,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 验证管理员是否存在
        AdminEntity existingAdmin = this.adminService.fetch(admin.getId()).orElseThrow(
            () -> new NotFoundException()
        );

        // 更新管理员信息
        existingAdmin.setName(admin.getName());

        // 如果提供了新密码，则更新密码
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            existingAdmin.setPassword(this.passwordEncoder.encode(admin.getPassword()));
        }

        // 如果提供了新头像，则更新头像
        if (admin.getAvatar() != null) {
            existingAdmin.setAvatar(admin.getAvatar());
        }

        this.adminService.update(existingAdmin);

        return result;
    }

    @PostMapping("/remove")
    public ResultTemplate remove(
        @RequestBody @Validated(AdminValidateGroup.Remove.class) AdminEntity fields,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        AdminEntity admin = this.adminService.fetch(fields.getId()).orElseThrow(
            () -> new NotFoundException()
        );

        admin.setDeletedAt(LocalDateTime.now());
        this.adminService.remove(admin);

        return result;
    }

}
