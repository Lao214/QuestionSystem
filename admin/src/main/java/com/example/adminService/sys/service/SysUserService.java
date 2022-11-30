package com.example.adminService.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.adminService.sys.entity.SysUser;
import com.example.adminService.sys.entity.SysUserQueryVo;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(String id, Integer status);

    //用户列表
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo);

    //username查询
    SysUser getUserInfoByUserName(String username);

    //根据用户名称获取用户信息（基本信息 和 菜单权限 和 按钮权限数据）
    Map<String, Object> getUserInfo(String username);

}
