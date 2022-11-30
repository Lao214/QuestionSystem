package com.example.adminService.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.adminService.sys.entity.AssginRoleVo;
import com.example.adminService.sys.entity.SysRole;
import com.example.adminService.sys.entity.SysRoleQueryVo;

import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
public interface SysRoleService extends IService<SysRole> {

    //条件分页查询
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo);

    //获取用户的角色数据
    Map<String, Object> getRolesByUserId(String userId);

    //用户分配角色
    void doAssign(AssginRoleVo assginRoleVo);

}
