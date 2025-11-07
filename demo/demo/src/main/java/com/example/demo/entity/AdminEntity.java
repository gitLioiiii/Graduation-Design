package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.AdminValidateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin")
public class AdminEntity {

    @TableId(type = IdType.AUTO)
    @NotNull(
        groups = {
            AdminValidateGroup.Update.class,
            AdminValidateGroup.Remove.class
        }
    )
    @Min(
        value = 1,
        groups = {
            AdminValidateGroup.Update.class,
            AdminValidateGroup.Remove.class
        }
    )
    private Integer id;

    // 注意：数据库字段名是 admin，对应前端的 username
    @TableField("admin")
    @NotBlank(
        groups = {
            AdminValidateGroup.Create.class,
            AdminValidateGroup.Login.class
        }
    )
    @Size(
        min = 2,
        max = 16,
        groups = {
            AdminValidateGroup.Create.class,
            AdminValidateGroup.Login.class
        }
    )
    private String username;

    @NotBlank(
        groups = {
            AdminValidateGroup.Create.class,
            AdminValidateGroup.Login.class
        }
    )
    @Size(
        min = 6,
        max = 16,
        groups = {
            AdminValidateGroup.Create.class,
            AdminValidateGroup.Login.class
        }
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(
        max = 32,
        groups = {
            AdminValidateGroup.Create.class,
            AdminValidateGroup.Update.class
        }
    )
    private String name;

    private String avatar;

    @TableField("deletedAt")
    private LocalDateTime deletedAt;

    @TableField("registeredAt")
    private LocalDateTime registeredAt;
}
