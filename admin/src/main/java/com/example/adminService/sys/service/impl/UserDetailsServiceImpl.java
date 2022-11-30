package com.example.adminService.sys.service.impl;

import com.example.adminService.sys.custom.CustomUser;
import com.example.adminService.sys.entity.SysUser;
import com.example.adminService.sys.service.SysMenuService;
import com.example.adminService.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** 
 * @description: security 相关类
 * @param:  
 * @return:  
 * @author 劳威锟
 * @date: 2022/11/30 10:53 AM
 */ 

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /** 
     * @description: 查询用户信息，并封装给CustomUser 
     * @param:
     * @return:  
     * @author 劳威锟
     * @date: 2022/11/30 1:12 PM
     */ 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserInfoByUserName(username);
        if(sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("用户被禁用了");
        }
        //根据userid查询操作权限数据
        List<String> userPermsList = sysMenuService.getUserButtonList(sysUser.getId().toString());
        //转换security要求格式数据
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String perm:userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
