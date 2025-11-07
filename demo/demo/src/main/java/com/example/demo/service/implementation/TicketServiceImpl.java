package com.example.demo.service.implementation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.TicketEntity;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

// 门票服务实现类
// 使用 MyBatis-Plus 分页功能
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Override
    public IPage<TicketEntity> page(Integer page, Integer pageSize, String ticketName, String ticketType) {
        // 设置分页参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        // 创建分页对象（Page 对象会自动处理分页逻辑）
        // 参数：当前页码，每页大小
        Page<TicketEntity> pageObj = new Page<>(page, pageSize);
        
        // 创建查询条件包装器（LambdaQueryWrapper 支持链式调用，类型安全）
        LambdaQueryWrapper<TicketEntity> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加搜索条件（分别按门票名称和类型筛选）
        if (StringUtils.hasText(ticketName)) {
            queryWrapper.like(TicketEntity::getTicketName, ticketName);
        }
        if (StringUtils.hasText(ticketType)) {
            queryWrapper.like(TicketEntity::getTicketType, ticketType);
        }
        
        // 添加删除标记过滤（只查询未删除的记录）
        queryWrapper.isNull(TicketEntity::getDeletedAt);
        
        // 添加排序（按 id 升序）
        queryWrapper.orderByAsc(TicketEntity::getId);
        
        // 执行分页查询
        // selectPage 方法会自动：
        // 执行 COUNT 查询获取总记录数
        // 执行分页 SQL 查询数据
        // 将结果封装到 IPage 对象中
        IPage<TicketEntity> result = this.ticketMapper.selectPage(pageObj, queryWrapper);
        
        return result;
    }

    @Override
    public Integer create(TicketEntity ticket) {
        // 设置创建时间
        ticket.setRegisteredAt(LocalDateTime.now());
        
        // insert 方法会自动处理自增ID，并将生成的ID设置到实体对象中
        this.ticketMapper.insert(ticket);
        
        // 返回创建的门票ID
        return ticket.getId();
    }

    @Override
    public Optional<TicketEntity> fetch(Integer id) {
        // 添加删除标记过滤（只查询未删除的记录）
        LambdaQueryWrapper<TicketEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TicketEntity::getId, id);
        queryWrapper.isNull(TicketEntity::getDeletedAt);
        
        // selectOne 可能返回 null，使用 Optional.ofNullable 包装
        // 这样可以明确表示返回值可能为空，避免空指针异常
        TicketEntity ticket = this.ticketMapper.selectOne(queryWrapper);
        return Optional.ofNullable(ticket);
    }

    @Override
    public Integer update(TicketEntity ticket) {
        // 设置更新时间
        ticket.setUpdatedAt(LocalDateTime.now());
        
        // updateById 方法会根据主键ID更新记录
        this.ticketMapper.updateById(ticket);
        
        // 返回更新的门票ID
        return ticket.getId();
    }

    @Override
    public Integer remove(TicketEntity ticket) {
        // 软删除：只更新 deletedAt 字段，不物理删除记录
        this.ticketMapper.updateById(ticket);
        
        // 返回移除的门票ID
        return ticket.getId();
    }

}

