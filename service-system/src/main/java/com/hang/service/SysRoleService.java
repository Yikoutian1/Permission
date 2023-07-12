package com.hang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.model.system.SysRole;
import com.hang.model.vo.AssginRoleVo;
import com.hang.model.vo.SysRoleQueryVo;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo);

    List<SysRole> mylist();

    Map<String, Object> getRoleByUserId(Long userId);

    void doAssogn(AssginRoleVo assginRoleVo);
}
