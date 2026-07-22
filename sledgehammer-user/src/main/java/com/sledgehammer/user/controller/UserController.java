package com.sledgehammer.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户模块的控制器类
 * 提供用户相关的 HTTP 请求处理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public String login() {
        //验证用户名密码是否正确
        //如果正确，使用JWT生成一个token，返回给用户，存储在Redis中
        //如果错误，返回错误信息
        //将token返回给用户，返回登录成功信息
        return "Login success!";
    }
}
