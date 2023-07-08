package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.mapper.SysRoleMapper;
import com.hang.model.system.SysRole;
import com.hang.model.vo.SysRoleQueryVo;
import com.hang.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SysRoleServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/6 006 21:17
 * @Version 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
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

}
