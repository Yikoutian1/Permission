package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysMenuMapper
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/12 012 15:12
 * @Version 1.0
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    // 根据userid查询菜单权限数据
    List<SysMenu> findMenuListUserId(@Param("userId") String sysUserId);
}
