package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.StockEntity;

import java.util.Optional;

// 库存服务接口
public interface StockService {


    // 分页
    IPage<StockEntity> page(Integer page, Integer pageSize);

    // 创建库存
    Integer create(StockEntity stock);

    // 根据ID获取库存（关联门票信息）
    Optional<StockEntity> fetch(Integer id);

    // 更新的库存ID
    Integer update(StockEntity stock);

    // 移除库存
    Integer remove(StockEntity stock);
}
