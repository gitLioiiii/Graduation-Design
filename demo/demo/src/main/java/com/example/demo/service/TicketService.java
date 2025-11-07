package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.TicketEntity;

import java.util.Optional;

// 门票服务接口
public interface TicketService {

    // 分页查询门票列表
    IPage<TicketEntity> page(Integer page, Integer pageSize, String ticketName, String ticketType);

    // 创建门票
    Integer create(TicketEntity ticket);

    // 根据ID获取门票
    Optional<TicketEntity> fetch(Integer id);

    // 更新门票
    Integer update(TicketEntity ticket);

    // 移除门票（软删除）
    Integer remove(TicketEntity ticket);
}
