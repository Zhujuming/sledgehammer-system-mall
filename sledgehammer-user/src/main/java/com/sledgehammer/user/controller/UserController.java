package com.sledgehammer.user.controller;

import com.sledgehammer.module.utils.JwtUtil;
import com.sledgehammer.user.controller.req.LoginReqVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块的控制器类
 * 提供用户相关的 HTTP 请求处理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginReqVO reqVO) {
        Map<String, Object> result = new HashMap<>();
        String username = reqVO.getUsername();
        String password = reqVO.getPassword();
        // 验证用户名密码是否正确
        if (!password.equals("root123")) {
            result.put("success", false);
            result.put("message", "Invalid username or password");
            return result;
        }

        // 如果正确，使用JWT生成一个token，返回给用户，存储在Redis中
        String token = JwtUtil.generateToken("070123");
        // 使用业务前缀作为Key，用户名作为Value，并将毫秒转换为秒作为过期时间
        redisTemplate.opsForValue().set("login:token:" + token, username, JwtUtil.TOKEN_TIMEOUT_MILLIS / 1000, TimeUnit.SECONDS);

        // 将token返回给用户，返回登录成功信息
        result.put("success", true);
        result.put("token", token);
        result.put("message", "Login success!");
        return result;
    }

}
