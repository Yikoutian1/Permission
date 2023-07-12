package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.exception.MyException;
import com.hang.mapper.SysMenuMapper;
import com.hang.mapper.SysRoleMenuMapper;
import com.hang.model.system.SysMenu;
import com.hang.model.system.SysRole;
import com.hang.model.system.SysRoleMenu;
import com.hang.model.vo.AssginMenuVo;
import com.hang.service.SysMenuService;
import com.hang.service.SysRoleService;
import com.hang.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysMenuServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/12 012 15:14
 * @Version 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     * 树形菜单结构
     * @return
     */
    @Override
    public List<SysMenu> findNodes() {
        // 获取所有菜单
        List<SysMenu> sysMenus = baseMapper.selectList(null);
        // 转换要求的数据格式
        List<SysMenu> result = MenuHelper.buildTree(sysMenus);
        return result;
    }

    /**
     * 删除菜单节点
     * @param id
     */
    @Override
    public void removeMenuById(String id) {
        // 查询当前菜单删除下是否有子节点(菜单)
        // 根据id和parent_id关系
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        // 仅需要查询到一个就可以证明有子节点
        queryWrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){// 有子菜单节点
            throw new MyException(201,"该菜单下有子菜单,不能进行删除");
        }
        // 没有子菜单则删除
        baseMapper.deleteById(id);
    }

    /**
     * 根据角色获取菜单
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        // 获取所有的菜单(可用的status=1)
        LambdaQueryWrapper<SysMenu> canUse = new LambdaQueryWrapper<>();
        canUse.eq(SysMenu::getStatus,1);
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(canUse);
        // 根据角色id查询,角色分配过的菜单列表
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper =  new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(sysRoleMenuLambdaQueryWrapper);
        // 从第二步查询的列表中,从角色分配的所有权限id获取菜单id,并包装
        List<String> roleMenuIds = new ArrayList<>();
        for(SysRoleMenu sysRoleMenu: sysRoleMenuList){
            String menuId = sysRoleMenu.getMenuId();
            roleMenuIds.add(menuId);
        }
        // 数据处理,判断菜单是否被选中,isSelect,选中true
        // 拿着分配菜单id 和 所有菜单对比,有相同的,然isSelect为true
        for (SysMenu sysMenu : sysMenuList) {
            if(roleMenuIds.contains(sysMenu)){
                sysMenu.setSelect(true);
            }else{
                sysMenu.setSelect(false);
            }
        }
        // 转换陈树形结构,用MenuHelper实现
        List<SysMenu> list = MenuHelper.buildTree(sysMenuList);
        return list;

    }
    /**
     * 给角色分配权限
     * @param assginMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //根据角色id删除菜单权限
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getId,assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(queryWrapper);
        //遍历菜单id列表集合,一个一个进行添加
        //对vo里面的两个字段(角色id,菜单id)进行添加
        List<String> menuIdList = assginMenuVo.getMenuIdList();
        for (String menuId: menuIdList) {
            // 先new一个角色菜单对象
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            // 对两个字段进行设置
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            // 在数据库中进行插入
            sysRoleMenuMapper.insert(sysRoleMenu);
        }

    }
}
