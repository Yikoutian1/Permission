package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.mapper.SysUserMapper;
import com.hang.model.system.SysRole;
import com.hang.model.system.SysUser;
import com.hang.model.vo.RouterVo;
import com.hang.model.vo.SysUserQueryVo;
import com.hang.service.SysMenuService;
import com.hang.service.SysUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysUserServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/10 010 14:08
 * @Version 1.0
 */

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageInfo, SysUserQueryVo sysUserQueryVo) {
        IPage<SysUser>  pageModel = baseMapper.selectPage(pageInfo,sysUserQueryVo);
        return pageModel;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        baseMapper.updateStatus(id,status);
    }

    /**
     * 根据用户名进行查询
     * @param username
     * @return
     */
    @Override
    public SysUser getUserInfoByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String,Object> map= new HashMap<>();
        map.put("name",username);
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("roles","[\"admin\"]");
        // 构造查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        // 根据username查询用户基本信息
        SysUser sysUser = this.getUserInfoByUserName(username);
        // 根据userid查询菜单权限值
        List<RouterVo> routerVoList = sysMenuService.getUserMenu(sysUser.getId());
        // 根据userid查询按钮权限值
        List<String> userButton = sysMenuService.getUserButton(sysUser.getId());
        // 菜单权限数据
        map.put("routers",routerVoList);
        // 按钮权限数据
        map.put("buttons",userButton);
        return map;
    }
}
