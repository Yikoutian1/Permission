package com.hang.controller;

import com.hang.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/8 008 18:41
 * @Version 1.0
 */
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @PostMapping("/login")
    public Result login(){
        Map<String,Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }
    @GetMapping("/info")
    public Result info(){
        Map<String,Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
