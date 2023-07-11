package com.hang.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.common.result.Result;
import com.hang.model.system.SysUser;
import com.hang.model.vo.SysUserQueryVo;
import com.hang.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SysUserController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/10 010 14:08
 * @Version 1.0
 */

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("用户管理分页")
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       SysUserQueryVo sysUserQueryVo
                       ){
        // 创建分页对象
        Page<SysUser> pageInfo = new Page<>(page,limit);
        // mybatis-plus 里面的IPage
        IPage<SysUser> res = sysUserService.selectPage(pageInfo,sysUserQueryVo);
        return Result.ok(res);
    }

    @ApiOperation("用户添加")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){
        boolean save = sysUserService.save(sysUser);
//        if(save){
//            return Result.ok();
//        }else{
//            return  Result.fail();
//        }
        return save ? Result.ok() : Result.fail();
    }
    @ApiOperation("根据id查询")
    @GetMapping("/getUserById/{id}")
    public Result getUserById(@PathVariable Long id){
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        boolean res = sysUserService.updateById(sysUser);
        return res ? Result.ok() : Result.fail();
    }
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean res = sysUserService.removeById(id);
        return res ? Result.ok() : Result.fail();
    }
    @ApiOperation("更改用户状态")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,
                               @PathVariable Integer status){
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }
}