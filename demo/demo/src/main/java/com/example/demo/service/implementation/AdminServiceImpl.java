package com.example.demo.service.implementation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.AdminEntity;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public IPage<AdminEntity> page(Integer page, Integer pageSize, String keywords) {
        // 设置分页参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        // 创建分页对象
        Page<AdminEntity> pageObj = new Page<>(page, pageSize);

        // 创建查询条件包装器
        LambdaQueryWrapper<AdminEntity> queryWrapper = buildQueryWrapper(keywords);

        // 执行分页查询
        IPage<AdminEntity> result = this.adminMapper.selectPage(pageObj, queryWrapper);

        return result;
    }

    @Override
    public Optional<AdminEntity> fetch(Integer id) {
        LambdaQueryWrapper<AdminEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminEntity::getId, id);
        queryWrapper.isNull(AdminEntity::getDeletedAt);

        AdminEntity admin = this.adminMapper.selectOne(queryWrapper);
        return Optional.ofNullable(admin);
    }

    @Override
    public Optional<AdminEntity> fetch(String username) {
        LambdaQueryWrapper<AdminEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminEntity::getUsername, username);
        queryWrapper.isNull(AdminEntity::getDeletedAt);

        AdminEntity admin = this.adminMapper.selectOne(queryWrapper);
        return Optional.ofNullable(admin);
    }

    @Override
    public Integer create(AdminEntity admin) {
        admin.setRegisteredAt(LocalDateTime.now());
        this.adminMapper.insert(admin);
        return admin.getId();
    }

    @Override
    public Integer update(AdminEntity admin) {
        this.adminMapper.updateById(admin);
        return admin.getId();
    }

    @Override
    public Integer remove(AdminEntity admin) {
        // 软删除：只更新 deletedAt 字段
        this.adminMapper.updateById(admin);
        return admin.getId();
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<AdminEntity> buildQueryWrapper(String keywords) {
        LambdaQueryWrapper<AdminEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 关键字搜索（搜索用户名或姓名）
        if (StringUtils.hasText(keywords)) {
            queryWrapper.and(wrapper ->
                wrapper.like(AdminEntity::getUsername, keywords)
                       .or()
                       .like(AdminEntity::getName, keywords)
            );
        }

        // 只查询未删除的记录
        queryWrapper.isNull(AdminEntity::getDeletedAt);

        // 按ID升序排序
        queryWrapper.orderByAsc(AdminEntity::getId);

        return queryWrapper;
    }
}
