package com.sledgehammer.module.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTCreator.Builder;

import java.util.Date;
import java.util.HashMap;
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
    private static final String SECRET_KEY = "sledgehammer-system-mall-jwt-secret-key-2026";
    /**
     * 默认用户名
     */
    private static final String USERNAME = "sledgehammer";

    /**
     * 默认 Token 过期时间（24 小时）单位：毫秒
     */
    private static final long TOKEN_TIMEOUT_MILLIS = 1000 * 60 * 60 * 24;

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
     * @param userId 用户ID
     * @return JWT Token 字符串
     */

    public static String generateToken(String userId) {

        //由于该生成器设置Header的参数为一个<String, Object>的Map，所以需要将userId转换为Map
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT"); // 设置Token类型为JWT
        headers.put("alg", "HS256"); // 设置签名算法为HMAC256

        //开始生成 Token，将之前准备好的header设置进去
        String Token = JWT.create()
                .withHeader(headers)                // 设置Header
                .withClaim("userId", userId)  // 将userId设置为Claim
                //Token过期时间(默认一天)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIMEOUT_MILLIS))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(USERNAME)  // 设置签发者(可自定义)
                .sign(getAlgorithm()); //进行签名，选择加密算法，以一个字符串密钥为参数

        //Token生成完毕，可以发送给客服端了，前端可以使用
        //localStorage.setItem("token",Token);进行存储在下次请求是携带发送给服务器进行验证
        System.out.println(Token);
        return Token;
    }

    public static void main(String[] args) {
        generateToken("1234567890");
    }

    /**
     * 生成 JWT Token（默认过期时间 24 小时）
     *
     * @param subject Token 主题，通常为用户标识（如用户ID）
     * @return JWT Token 字符串
     */
//    public static String generateToken(String subject) {
//        return JWT.create()
//                .withSubject(subject)
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIMEOUT_MILLIS))
//                .sign(getAlgorithm());
//    }

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
        Builder builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIMEOUT_MILLIS));

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
        switch (value) {
            case String s -> builder.withClaim(key, s);
            case Integer i -> builder.withClaim(key, i);
            case Long l -> builder.withClaim(key, l);
            case Double v -> builder.withClaim(key, v);
            case Boolean b -> builder.withClaim(key, b);
            case Date date -> builder.withClaim(key, date);
            case java.time.Instant instant -> builder.withClaim(key, instant);
            case Map map -> builder.withClaim(key, (Map<String, ?>) value);
            case List list -> builder.withClaim(key, list);
            case Integer[] integers -> builder.withArrayClaim(key, integers);
            case Long[] longs -> builder.withArrayClaim(key, longs);
            case String[] strings -> builder.withArrayClaim(key, strings);
            default -> builder.withClaim(key, value.toString());
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