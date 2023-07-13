package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.exception.MyException;
import com.hang.mapper.SysMenuMapper;
import com.hang.mapper.SysRoleMenuMapper;
import com.hang.model.system.SysMenu;
import com.hang.model.system.SysRoleMenu;
import com.hang.model.vo.AssginMenuVo;
import com.hang.model.vo.RouterVo;
import com.hang.service.SysMenuService;
import com.hang.utils.MenuHelper;
import com.hang.utils.RouterHelper;
import javafx.util.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据用户查询菜单权限
     * (三张表的关联查询)
     * @param sysUserId
     * @return
     */
    @Override
    public List<RouterVo> getUserMenu(String sysUserId) {
        // 菜单->角色->用户 (菜单权限分配给用户 角色分配给用户)
        // 用户需要先关联角色(sys_user_role) 角色再关联菜单(sys_role_menu)

        List <SysMenu> result = null;
        // admin是超级管理员,可以操作所有内容(查询所有权限数据)
        if("1".equals(sysUserId)){
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus,"1")
                         .orderByAsc(SysMenu::getSortValue);

            result =  baseMapper.selectList(queryWrapper);

        }else{// 如果不是1(1是超级管理员独有的权限)则查询该用户的权限
            result = baseMapper.findMenuListUserId(sysUserId);
        }
        // 构建陈树形结构
        List<SysMenu> sysMenusTree = MenuHelper.buildTree(result);


        // 实现前端动态路由(转换成前端路由要求的格式数据)   工具类
        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenusTree);
        return routerVos;
    }

    /**
     * 根据用户查询按钮权限
     * @param sysUserId
     */
    @Override
    public List<String> getUserButton(String sysUserId) {
        List<SysMenu> sysMenuList = null;
        // 判断是否是管理员(id==1)
        if("1".equals(sysUserId)){
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus,1);
            sysMenuList = baseMapper.selectList(queryWrapper);
        }else{// 不是管理员,普通用户
            sysMenuList = baseMapper.findMenuListUserId(sysUserId);
        }
        // sysMenuList进行遍历
        List<String> permissionList = new ArrayList<>();
        sysMenuList.forEach(item->{
            // type = 2 才是按钮
            if(item.getType() == 2){
                // 如果等于2,则获取数据库Perms字段,这个是按钮路径
                permissionList.add(item.getPerms());
            }
        });
        return permissionList;
    }
}
