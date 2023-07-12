package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.mapper.SysRoleMapper;
import com.hang.mapper.SysUserRoleMapper;
import com.hang.model.system.SysRole;
import com.hang.model.system.SysUserRole;
import com.hang.model.vo.AssginRoleVo;
import com.hang.model.vo.SysRoleQueryVo;
import com.hang.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

/**
 * @ClassName SysRoleServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/6 006 21:17
 * @Version 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    // 条件分页查询
    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
        IPage<SysRole>  pageModel = baseMapper.selectPage(pageParam,sysRoleQueryVo);
        return pageModel;
    }

    /**
     * 回顾在mapper.xml写sql
     * @return
     */
    @Override
    public List<SysRole> mylist() {
        return baseMapper.mylist();
    }

    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getRoleByUserId(Long userId) {
        // 获取所有角色
        List<SysRole> roles = baseMapper.selectList(null);
        // 根据用户id进行查询，已经分配的角色信息
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,SysUserRole::getUserId,userId);
        // 查出来了所有字段
        List<SysUserRole> userRolesList = sysUserRoleMapper.selectList(queryWrapper);
        // 从list中获取所有的角色id
        List<String> userRoleIds = userRolesList.stream().map((item)->{
            return (item.getRoleId());
        }).collect(Collectors.toList());
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",roles);// 所有的角色
        returnMap.put("userRoleIds",userRoleIds);// 已经分配的角色集合
        userRoleIds.forEach(System.out::println);
        return returnMap;
    }

    /**
     * 用户分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssogn(AssginRoleVo assginRoleVo) {
        // 根据用户id删除之前分配的角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleMapper.delete(queryWrapper);
        // 获取所有角色的id,添加角色用户关系表
        // 1.得到角色id
        List<String> roles = assginRoleVo.getRoleIdList();
        // 2.添加关系表
        roles.forEach((item)->{
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(item);
            sysUserRoleMapper.insert(sysUserRole);
        });
    }
}
