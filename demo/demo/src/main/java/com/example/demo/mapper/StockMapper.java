package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.StockEntity;

@Mapper
public interface StockMapper extends BaseMapper<StockEntity> {
    
    // 关联查询库存和门票信息（分页）
    // 使用 MyBatis-Plus 分页插件，Page 对象会自动处理分页逻辑
    @Select("SELECT s.id, s.total_stock, s.day_stock, s.ticket_id, t.ticket_name as ticketName, t.ticket_type as ticketType " +
            "FROM stock s " +
            "LEFT JOIN ticket t ON s.ticket_id = t.id " +
            "WHERE (t.deletedAt IS NULL OR t.id IS NULL) " +
            "ORDER BY s.id ASC")
    IPage<StockEntity> selectWithTicketName(IPage<StockEntity> page);
    
    // 根据ID关联查询库存和门票信息
    @Select("SELECT s.id, s.total_stock, s.day_stock, s.ticket_id, t.ticket_name as ticketName, t.ticket_type as ticketType " +
            "FROM stock s " +
            "LEFT JOIN ticket t ON s.ticket_id = t.id " +
            "WHERE s.id = #{id}")
    StockEntity selectByIdWithTicketName(@Param("id") Integer id);
}
