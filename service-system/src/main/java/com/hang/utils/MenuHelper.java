package com.hang.utils;

import com.hang.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MenuHelper
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/12 012 16:01
 * @Version 1.0
 */

public class MenuHelper {
    /**
     * 构建树形结构  P71
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList){
        // 创建集合封装最终数据
        List<SysMenu> trees =  new ArrayList<>();
        // 遍历所有菜单集合
        for (SysMenu sysMenu:sysMenuList) {
            // 找到递归入口,parent_id = 0,顶层管理员数据
            if(sysMenu.getParentId().longValue() == 0){
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }
    // 从根结点进行递归查询,查询子节点
    // 判断id 是否等于 parent_id , 如果相同就是子节点,那么进行封装
    public static SysMenu findChildren(SysMenu sysMenu,
                                       List<SysMenu> sysMenuList){
        // 孩子数据初始化
        sysMenu.setChildren(new ArrayList<>());
        // 遍历递归进行查找
        for(SysMenu it:sysMenuList){
            // 获取当前菜单id
            String id = sysMenu.getId();
            // 获取所有菜单parent_id
            String parent_id = String.valueOf(it.getParentId());
            // 进行比对,左边id跟右边一列的parent_id进行一一比对
            if(id.equals(parent_id)){
                // 如果孩子节点为空,那么则创建孩子
                if(sysMenu.getChildren() == null){
                    sysMenu.setChildren(new ArrayList<>());
                }
                // 递归进行查找
                sysMenu
                        .getChildren()
                        .add(findChildren(it,sysMenuList));
            }
        }
        // 递归结束,数据返回
        return sysMenu;
    }
}
