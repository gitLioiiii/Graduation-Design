package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    // 分页查询用户列表（使用 MyBatis-Plus 分页）
    IPage<UserEntity> page(Integer page, Integer pageSize, String keywords);

    // 根据ID获取用户
    Optional<UserEntity> fetch(Integer id);

    // 根据用户名获取用户
    Optional<UserEntity> fetch(String username);

    // 创建用户
    Integer create(UserEntity user);

    // 更新用户
    Integer update(UserEntity user);

    // 移除用户（软删除）
    Integer remove(UserEntity user);
}

