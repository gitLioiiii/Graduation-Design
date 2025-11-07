package com.example.demo.service.implementation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public IPage<UserEntity> page(Integer page, Integer pageSize, String keywords) {
        // 设置分页参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        // 创建分页对象（Page 对象会自动处理分页逻辑）
        Page<UserEntity> pageObj = new Page<>(page, pageSize);
        
        // 创建查询条件包装器
        LambdaQueryWrapper<UserEntity> queryWrapper = buildQueryWrapper(keywords);
        
        // 执行分页查询
        // selectPage 方法会自动：
        // 执行 COUNT 查询获取总记录数
        // 执行分页 SQL 查询数据
        // 将结果封装到 IPage 对象中
        IPage<UserEntity> result = this.userMapper.selectPage(pageObj, queryWrapper);
        
        return result;
    }

    @Override
    public Optional<UserEntity> fetch(Integer id) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getId, id);
        queryWrapper.isNull(UserEntity::getDeletedAt);
        
        UserEntity user = this.userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserEntity> fetch(String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, username);
        queryWrapper.isNull(UserEntity::getDeletedAt);
        
        UserEntity user = this.userMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }

    @Override
    public Integer create(UserEntity user) {
        user.setRegisteredAt(LocalDateTime.now());
        this.userMapper.insert(user);
        return user.getId();
    }

    @Override
    public Integer update(UserEntity user) {
        this.userMapper.updateById(user);
        return user.getId();
    }

    @Override
    public Integer remove(UserEntity user) {
        // 软删除：只更新 deletedAt 字段，不物理删除记录
        this.userMapper.updateById(user);
        return user.getId();
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<UserEntity> buildQueryWrapper(String keywords) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键字搜索（搜索用户名或姓名）
        if (StringUtils.hasText(keywords)) {
            queryWrapper.and(wrapper -> 
                wrapper.like(UserEntity::getUsername, keywords)
                       .or()
                       .like(UserEntity::getName, keywords)
            );
        }
        
        // 只查询未删除的记录
        queryWrapper.isNull(UserEntity::getDeletedAt);
        
        // 按ID升序排序
        queryWrapper.orderByAsc(UserEntity::getId);
        
        return queryWrapper;
    }
}

