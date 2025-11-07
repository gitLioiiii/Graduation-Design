package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.TicketValidateGroup;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 使用mybatis-plus
@Data
@TableName("ticket")
public class TicketEntity {
    
    @TableId(type = IdType.AUTO)
    @NotNull(
        groups = {
            TicketValidateGroup.Update.class,
            TicketValidateGroup.Remove.class
        }
    )
    @Min(
        value = 1,
        groups = {
            TicketValidateGroup.Update.class,
            TicketValidateGroup.Remove.class
        }
    )
    private Integer id;

    @NotBlank(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private String ticketName;

    @NotBlank(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private String ticketType;

    // 与前端一致，数据库是是ticket_image
    private String ticketImage;

    @NotBlank(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private String status;

    @NotBlank(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private String timeType;

    @NotNull(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    @DecimalMin(
        value = "0.01",
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private BigDecimal dayPrice;

    @NotNull(
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    @DecimalMin(
        value = "0.01",
        groups = {
            TicketValidateGroup.Create.class,
            TicketValidateGroup.Update.class
        }
    )
    private BigDecimal weekendPrice;

    // 数据库是 deletedAt（驼峰），MyBatis-Plus 默认会转换为 deleted_at。
    // 数据库字段名是驼峰命名（deletedAt），需要显式指定，避免被转换为 deleted_at
    @TableField("deletedAt")
    private LocalDateTime deletedAt;

    // 数据库字段名是驼峰命名（registeredAt），需要显式指定
    @TableField("registeredAt")
    private LocalDateTime registeredAt;

    // 数据库字段名是驼峰命名（updatedAt），需要显式指定
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
