package com.example.adminService.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.adminService.sys.entity.AssginMenuVo;
import com.example.adminService.sys.entity.RouterVo;
import com.example.adminService.sys.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-29
 */
public interface SysMenuService extends IService<SysMenu> {

    //菜单列表（树形）
    List<SysMenu> findNodes();

    // //删除菜单
    void removeMenuById(String id);

    //根据角色分配菜单
    List<SysMenu> findMenuByRoleId(Long roleId);

    //给角色分配菜单权限
    void doAssign(AssginMenuVo assginMenuVo);

    //根据userid查询菜单权限值
    List<RouterVo> getUserMenuList(String id);

    //根据userid查询按钮权限值
    List<String> getUserButtonList(String id);
}
