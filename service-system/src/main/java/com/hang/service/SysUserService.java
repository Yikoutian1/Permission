package com.hang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.model.system.SysUser;
import com.hang.model.vo.SysUserQueryVo;

import java.util.Map;

public interface SysUserService extends IService<SysUser>{
    IPage<SysUser> selectPage(Page<SysUser> pageInfo, SysUserQueryVo sysUserQueryVo);

    void updateStatus(Long id, Integer status);

    SysUser getUserInfoByUserName(String username);

    Map<String, Object> getUserInfo(String username);
}
