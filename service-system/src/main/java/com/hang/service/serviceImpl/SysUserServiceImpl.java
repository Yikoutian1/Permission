package com.hang.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.mapper.SysUserMapper;
import com.hang.model.system.SysUser;
import com.hang.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName SysUserServiceImpl
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/7/10 010 14:08
 * @Version 1.0
 */

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

}
