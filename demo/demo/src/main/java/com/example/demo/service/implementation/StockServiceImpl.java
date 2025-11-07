package com.example.demo.service.implementation;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.StockEntity;
import com.example.demo.mapper.StockMapper;
import com.example.demo.service.StockService;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 库存服务实现类
@Service
public class StockServiceImpl implements StockService {

    private final StockMapper stockMapper;

    public StockServiceImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Override
    public IPage<StockEntity> page(Integer page, Integer pageSize) {
        // 设置分页参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        // 创建分页对象（Page 对象会自动处理分页逻辑）
        // 参数：当前页码，每页大小
        Page<StockEntity> pageObj = new Page<>(page, pageSize);
        
        // 执行分页查询（关联查询门票信息）
        // selectWithTicketName 方法会自动：
        // 执行 COUNT 查询获取总记录数
        // 执行分页 SQL 查询数据（添加 LIMIT 和 OFFSET）
        // 将结果封装到 IPage 对象中
        IPage<StockEntity> result = this.stockMapper.selectWithTicketName(pageObj);
        
        return result;
    }

    @Override
    public Integer create(StockEntity stock) {
        // 使用 MyBatis-Plus 插入数据
        this.stockMapper.insert(stock);
        
        // 返回创建的库存ID
        return stock.getId();
    }

    @Override
    public Optional<StockEntity> fetch(Integer id) {
        // 使用关联查询获取库存信息和门票名称
        StockEntity stock = this.stockMapper.selectByIdWithTicketName(id);
        
        return Optional.ofNullable(stock);
    }

    @Override
    public Integer update(StockEntity stock) {
        // 使用 MyBatis-Plus 更新数据
        this.stockMapper.updateById(stock);
        
        // 返回更新的库存ID
        return stock.getId();
    }

    @Override
    public Integer remove(StockEntity stock) {
        // 物理删除库存记录
        this.stockMapper.deleteById(stock.getId());
        
        // 返回移除的库存ID
        return stock.getId();
    }
}

