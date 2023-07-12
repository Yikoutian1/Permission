package com.hang.controller;

import com.hang.common.result.Result;
import com.hang.model.system.SysMenu;
import com.hang.model.vo.AssginMenuVo;
import com.hang.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysMenuController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/12 012 15:17
 * @Version 1.0
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation(value = "获取菜单树形列表")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result saveMenu(@RequestBody SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        return save ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据id获取菜单")
    @GetMapping("/findNode/{id}")
    public Result findNode(@PathVariable String id){
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }

    @ApiOperation(value = "更新菜单")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    /**
     * 不能直接删除了,假如一个绑定了一个,那么就会断层
     * 如果有子节点就不能删除如果没有子节点,那么直接删除
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable String id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }
}
