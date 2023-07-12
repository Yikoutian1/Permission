package com.hang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.common.result.Result;
import com.hang.model.system.SysRole;
import com.hang.model.vo.AssginRoleVo;
import com.hang.model.vo.SysRoleQueryVo;
import com.hang.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName SysRoleController
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/6 006 21:14
 * @Version 1.0
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController{
    @Autowired
    private SysRoleService sysRoleService;
    @ApiOperation("查询所有角色记录")
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll(){
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }
    @ApiOperation("查询所有角色记录(回顾在mapper.xml写sql)")
    @GetMapping("/findAllSQL")
    public Result<List<SysRole>> findAllSQL(){
        List<SysRole> list = sysRoleService.mylist();
        return Result.ok(list);
    }
    /**
     * 条件分页查询,page代表当前页,limit代表限制每页数
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result findPageQueryRole(@PathVariable Long page,
                                    @PathVariable Long limit,
                                    SysRoleQueryVo sysRoleQueryVo){
                                    // SysRoleQueryVo角色查询实体
        // 创建page对象
        Page<SysRole> pageParam = new Page<>(page,limit);
        // 调用service方法
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam,sysRoleQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation("添加角色")//添加不需要id值,自动填充
    @PostMapping("/save")
    public Result saveRole(@RequestBody SysRole sysRole){
        boolean save = sysRoleService.save(sysRole);
        if(save){
            return Result.ok("添加成功");
        }else {
            return Result.fail("添加失败");
        }
    }
    @ApiOperation("修改")//修改需要id值
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole){
        boolean isSuccess = sysRoleService.updateById(sysRole);
        if(isSuccess){
            return Result.ok("修改成功");
        }else{
            return Result.fail("修改失败");
        }
    }
    @ApiOperation(value = "根据id查询")
    @GetMapping("/findRoleById/{id}")
    public Result findRoleById(@PathVariable Long id){
        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        sysRoleService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * -----------------用户角色权限管理接口---------------------
     *          分为两步
     * assign : 分配;分派;指派
     * (因为之前本来就有绑定数据,现在需要重新分配,只能先查出来,在删除)
     * (然后再在数据库中写入,然后才成功保存新的角色)
     */
    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String, Object> map = sysRoleService.getRoleByUserId(userId);
        return Result.ok(map);
    }
    /**
     * 删除之前已经分配的角色
     * 保存新分配的角色
     * AssginRoleVole类已经封装在entity了
     */
    @ApiOperation(value = "用户重新分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssogn(assginRoleVo);
        return Result.ok();
    }
}
