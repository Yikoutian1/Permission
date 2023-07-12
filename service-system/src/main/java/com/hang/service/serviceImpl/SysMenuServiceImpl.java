package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.mapper.SysMenuMapper;
import com.hang.model.system.SysMenu;
import com.hang.service.SysMenuService;
import com.hang.utils.MenuHelper;
import org.springframework.stereotype.Service;

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
}
