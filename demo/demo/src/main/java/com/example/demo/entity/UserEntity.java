package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.UserValidateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserEntity {
    
    @TableId(type = IdType.AUTO)
    @NotNull(
        groups = {
            UserValidateGroup.Update.class,
            UserValidateGroup.Remove.class
        }
    )
    @Min(
        value = 1,
        groups = {
            UserValidateGroup.Update.class,
            UserValidateGroup.Remove.class
        }
    )
    private Integer id;

    @NotBlank(
        groups = {
            UserValidateGroup.Create.class,
            UserValidateGroup.Login.class
        }
    )
    @Size(
        min = 2,
        max = 16,
        groups = {
            UserValidateGroup.Create.class,
            UserValidateGroup.Login.class
        }
    )
    private String username;

    @NotBlank(
        groups = {
            UserValidateGroup.Create.class,
            UserValidateGroup.Login.class
        }
    )
    @Size(
        min = 6,
        max = 16,
        groups = {
            UserValidateGroup.Create.class,
            UserValidateGroup.Login.class
        }
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(
        max = 32,
        groups = {
            UserValidateGroup.Create.class,
            UserValidateGroup.Update.class
        }
    )
    private String name;

    private String avatar;

    @TableField("deletedAt")
    private LocalDateTime deletedAt;

    @TableField("registeredAt")
    private LocalDateTime registeredAt;
}

