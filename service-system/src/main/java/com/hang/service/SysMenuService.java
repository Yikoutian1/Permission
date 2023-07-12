package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.model.system.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();
}
