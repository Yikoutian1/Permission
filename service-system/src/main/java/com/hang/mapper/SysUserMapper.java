package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.model.system.SysUser;
import com.hang.model.vo.SysUserQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    public IPage<SysUser> selectPage(Page<SysUser> pageInfo, @Param("vo") SysUserQueryVo sysUserQueryVo);
    public void updateStatus(Long id,@Param("status") Integer status);
}
