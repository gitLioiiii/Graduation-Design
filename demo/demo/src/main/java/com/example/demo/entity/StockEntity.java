package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.StockValidateGroup;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("stock")
public class StockEntity {

    @TableId(type = IdType.AUTO)
    @NotNull(
        groups = {
            StockValidateGroup.Update.class,
            StockValidateGroup.Remove.class
        }
    )
    @Min(
        value = 1,
        groups = {
            StockValidateGroup.Update.class,
            StockValidateGroup.Remove.class
        }
    )
    private Integer id;

    @NotNull(
        groups = {
            StockValidateGroup.Create.class,
            StockValidateGroup.Update.class
        }
    )
    @Min(
        value = 0,
        groups = {
            StockValidateGroup.Create.class,
            StockValidateGroup.Update.class
        }
    )
    @TableField("total_stock")
    private Integer totalStock;

    @Min(
        value = 0,
        groups = {
            StockValidateGroup.Create.class,
            StockValidateGroup.Update.class
        }
    )
    @TableField("day_stock")
    private Integer dayStock;

    @NotNull(
        groups = {
            StockValidateGroup.Create.class,
            StockValidateGroup.Update.class
        }
    )
    @Min(
        value = 1,
        groups = {
            StockValidateGroup.Create.class,
            StockValidateGroup.Update.class
        }
    )
    @TableField("ticket_id")
    private Integer ticketId;

    // 关联的门票信息（查询时使用，不映射到数据库）
    @TableField(exist = false)
    private String ticketName;

    @TableField(exist = false)
    private String ticketType;

}
