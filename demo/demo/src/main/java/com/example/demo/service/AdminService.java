package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.AdminEntity;

import java.util.Optional;

public interface AdminService {

    // 分页查询管理员列表
    IPage<AdminEntity> page(Integer page, Integer pageSize, String keywords);

    // 根据ID获取管理员
    Optional<AdminEntity> fetch(Integer id);

    // 根据用户名获取管理员
    Optional<AdminEntity> fetch(String username);

    // 创建管理员
    Integer create(AdminEntity admin);

    // 更新管理员
    Integer update(AdminEntity admin);

    // 移除管理员（软删除）
    Integer remove(AdminEntity admin);
}
