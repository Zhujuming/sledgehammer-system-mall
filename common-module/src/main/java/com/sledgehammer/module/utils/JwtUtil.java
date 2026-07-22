package com.sledgehammer.module.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT 工具类
 * 基于 com.auth0:java-jwt 实现，提供 JWT 的生成、验证和解析功能
 */
public class JwtUtil {

    /**
     * JWT 签名密钥
     * 生产环境建议从配置文件或环境变量中读取
     */
    private static final String SECRET_KEY = "sledgehammer-system-mall-jwt-secret-key-2024";

    /**
     * 获取签名算法
     *
     * @return HMAC256 算法实例
     */
    private static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY);
    }

    /**
     * 生成 JWT Token（默认过期时间 24 小时）
     *
     * @param subject Token 主题，通常为用户标识（如用户ID）
     * @return JWT Token 字符串
     */
    public static String generateToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(getAlgorithm());
    }

    /**
     * 生成 JWT Token（自定义过期时间）
     *
     * @param subject          Token 主题，通常为用户标识（如用户ID）
     * @param expirationMillis 过期时间（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(String subject, long expirationMillis) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(getAlgorithm());
    }

    /**
     * 生成 JWT Token（携带自定义 Claims，默认过期时间 24 小时）
     *
     * @param claims  自定义 Claims 键值对
     * @param subject Token 主题，通常为用户标识（如用户ID）
     * @return JWT Token 字符串
     */
    public static String generateToken(Map<String, Object> claims, String subject) {
        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000));

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            addClaim(builder, entry.getKey(), entry.getValue());
        }
        return builder.sign(getAlgorithm());
    }

    /**
     * 生成 JWT Token（携带自定义 Claims，自定义过期时间）
     *
     * @param claims           自定义 Claims 键值对
     * @param subject          Token 主题，通常为用户标识（如用户ID）
     * @param expirationMillis 过期时间（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(Map<String, Object> claims, String subject, long expirationMillis) {
        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis));

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            addClaim(builder, entry.getKey(), entry.getValue());
        }
        return builder.sign(getAlgorithm());
    }

    /**
     * 根据值类型添加 Claim
     *
     * @param builder JWT 构建器
     * @param key     Claim 键
     * @param value   Claim 值
     */
    private static void addClaim(com.auth0.jwt.JWTCreator.Builder builder, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            builder.withClaim(key, (String) value);
        } else if (value instanceof Integer) {
            builder.withClaim(key, (Integer) value);
        } else if (value instanceof Long) {
            builder.withClaim(key, (Long) value);
        } else if (value instanceof Double) {
            builder.withClaim(key, (Double) value);
        } else if (value instanceof Boolean) {
            builder.withClaim(key, (Boolean) value);
        } else if (value instanceof Date) {
            builder.withClaim(key, (Date) value);
        } else if (value instanceof java.time.Instant) {
            builder.withClaim(key, (java.time.Instant) value);
        } else if (value instanceof Map) {
            builder.withClaim(key, (Map<String, ?>) value);
        } else if (value instanceof List) {
            builder.withClaim(key, (List<?>) value);
        } else if (value instanceof Integer[]) {
            builder.withArrayClaim(key, (Integer[]) value);
        } else if (value instanceof Long[]) {
            builder.withArrayClaim(key, (Long[]) value);
        } else if (value instanceof String[]) {
            builder.withArrayClaim(key, (String[]) value);
        } else {
            builder.withClaim(key, value.toString());
        }
    }

    /**
     * 验证 Token 是否有效（仅验证签名和过期时间）
     *
     * @param token JWT Token 字符串
     * @return true 表示有效，false 表示无效
     */
    public static boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 验证 Token 是否有效（验证签名、过期时间和主题）
     *
     * @param token   JWT Token 字符串
     * @param subject 期望的主题值
     * @return true 表示有效，false 表示无效
     */
    public static boolean validateToken(String token, String subject) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getSubject().equals(subject) && !isTokenExpired(token);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 判断 Token 是否已过期
     *
     * @param token JWT Token 字符串
     * @return true 表示已过期，false 表示未过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            Date expiresAt = jwt.getExpiresAt();
            return expiresAt != null && expiresAt.before(new Date());
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    /**
     * 解码 Token（验证签名并返回解码后的 JWT 对象）
     *
     * @param token JWT Token 字符串
     * @return 解码后的 DecodedJWT 对象
     * @throws JWTVerificationException 签名验证失败时抛出
     */
    public static DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    /**
     * 提取 Token 的主题（Subject）
     *
     * @param token JWT Token 字符串
     * @return 主题字符串
     */
    public static String extractSubject(String token) {
        return decodeToken(token).getSubject();
    }

    /**
     * 提取 Token 的过期时间
     *
     * @param token JWT Token 字符串
     * @return 过期时间 Date 对象
     */
    public static Date extractExpiration(String token) {
        return decodeToken(token).getExpiresAt();
    }

    /**
     * 提取 Token 的签发时间
     *
     * @param token JWT Token 字符串
     * @return 签发时间 Date 对象
     */
    public static Date extractIssuedAt(String token) {
        return decodeToken(token).getIssuedAt();
    }

    /**
     * 提取指定名称的 Claim（返回原始 Claim 对象）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @return Claim 对象
     */
    public static Claim extractClaim(String token, String claimName) {
        return decodeToken(token).getClaim(claimName);
    }

    /**
     * 提取指定名称的 Claim（转换为 String 类型）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @return String 类型的值，不存在返回 null
     */
    public static String extractClaimAsString(String token, String claimName) {
        Claim claim = extractClaim(token, claimName);
        return claim.asString();
    }

    /**
     * 提取指定名称的 Claim（转换为 Long 类型）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @return Long 类型的值，不存在返回 null
     */
    public static Long extractClaimAsLong(String token, String claimName) {
        Claim claim = extractClaim(token, claimName);
        return claim.asLong();
    }

    /**
     * 提取指定名称的 Claim（转换为 Integer 类型）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @return Integer 类型的值，不存在返回 null
     */
    public static Integer extractClaimAsInteger(String token, String claimName) {
        Claim claim = extractClaim(token, claimName);
        return claim.asInt();
    }

    /**
     * 提取指定名称的 Claim（转换为 Boolean 类型）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @return Boolean 类型的值，不存在返回 null
     */
    public static Boolean extractClaimAsBoolean(String token, String claimName) {
        Claim claim = extractClaim(token, claimName);
        return claim.asBoolean();
    }

    /**
     * 提取指定名称的 Claim（转换为指定类型）
     *
     * @param token     JWT Token 字符串
     * @param claimName Claim 名称
     * @param clazz     目标类型 Class
     * @param <T>       目标类型泛型
     * @return 指定类型的值，不存在返回 null
     */
    public static <T> T extractClaimAs(String token, String claimName, Class<T> clazz) {
        Claim claim = extractClaim(token, claimName);
        return claim.as(clazz);
    }
}