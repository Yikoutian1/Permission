package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.model.system.SysMenu;
import com.hang.model.vo.AssginMenuVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    // 树形节点
    List<SysMenu> findNodes();
    // 删除节点
    void removeMenuById(String id);
    // 根据角色获取菜单
    List<SysMenu> findSysMenuByRoleId(Long roleId);
    // 给角色分配权限
    void doAssign(AssginMenuVo assginMenuVo);
}
