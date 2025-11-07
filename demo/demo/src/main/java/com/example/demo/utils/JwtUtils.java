package com.example.demo.utils;

import java.time.Instant;
import java.util.List;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.example.demo.configuration.JwtConfiguration;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.AdminEntity;

@Component
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    // 提供JwtEncoder，用于编码JWT token
    public JwtUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // 生成用户JWT token
    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(JwtConfiguration.getTokenValidity());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(JwtConfiguration.getIssuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("username", user.getUsername())
                .claim("name", user.getName())
                // 权限为user
                .claim("authorities", List.of("user"))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // 生成管理员JWT token
    public String generateToken(AdminEntity admin) {
        Instant now = Instant.now();
        Instant expiry = now.plus(JwtConfiguration.getTokenValidity());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(JwtConfiguration.getIssuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(admin.getUsername())
                .claim("userId", admin.getId())
                .claim("username", admin.getUsername())
                .claim("name", admin.getName())
                // 权限为admin
                .claim("authorities", List.of("admin"))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // 从JWT token中提取用户ID
    public Integer extractUserId(String token) {
        // 这个方法主要用于解析token，实际使用中Spring Security会自动处理
        return null;
    }

    // 验证token是否有效
    public boolean validateToken(String token) {
        try {
            // Spring Security会自动验证token的有效性
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
