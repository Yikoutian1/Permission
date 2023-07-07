package com.hang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hang.model.system.SysRole;
import com.hang.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysRoleController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/6 006 21:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController{
    @Autowired
    private SysRoleService sysRoleService;
    @GetMapping("/findAll")
    public List<SysRole> findAll(){
        return sysRoleService.list();
    }
}
