package com.hang.controller;

import com.hang.common.result.Result;
import com.hang.common.utils.JwtHelper;
import com.hang.common.utils.PasswordUtils;
import com.hang.exception.MyException;
import com.hang.model.system.SysUser;
import com.hang.model.vo.LoginVo;
import com.hang.service.SysUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/8 008 18:41
 * @Version 1.0
 */
@Slf4j
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        // 根据用户名查询数据库
        SysUser sysUser = sysUserService.getUserInfoByUserName(loginVo.getUsername());
        // 如果查询为空
        if(sysUser==null){
            throw new MyException(20001,"用户不存在");
        }

        // 判断密码是否一致
        if(!PasswordUtils.check(loginVo.getPassword(),sysUser.getPassword())){
            throw new MyException(20001,"用户名或密码不正确");
        }
        log.info("密码匹配:{}",PasswordUtils.check(loginVo.getPassword(),sysUser.getPassword()));
        // 判断用户是否可以
        if(sysUser.getStatus().intValue() == 0){
            throw new MyException(20001,"用户名已被禁用");
        }
        // 根据userid和username生成token字符串,通过map进行返回
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        // 获取请求头token字符串
        String token = request.getHeader("token");
        // 从token获取用户名称(username)
        String username = JwtHelper.getUsername(token);
        // 根据用户名称获取用户信息(基本信息 和 菜单权限 和 按钮权限数据)
        Map<String,Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
