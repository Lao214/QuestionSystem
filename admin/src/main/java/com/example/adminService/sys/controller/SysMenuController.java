package com.example.adminService.sys.controller;

import com.example.adminService.sys.entity.AssginMenuVo;
import com.example.adminService.sys.entity.SysMenu;
import com.example.adminService.sys.service.SysMenuService;
import com.example.adminService.utils.SysResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;


    @ApiOperation("给角色分配菜单权限")
    @PostMapping("/doAssign")
    public SysResult doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return SysResult.ok();
    }

    //根据角色分配菜单
    @ApiOperation("根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public SysResult toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findMenuByRoleId(roleId);
        return SysResult.ok(list);
    }

    //菜单列表（树形）
    @ApiOperation("菜单列表")
    @GetMapping("findNodes")
    public SysResult findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return SysResult.ok(list);
    }


    //添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("save")
    public SysResult save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return SysResult.ok();
    }

    //根据id查询
    @ApiOperation("根据id查询菜单")
    @GetMapping("findNode/{id}")
    public SysResult findNode(@PathVariable String id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return  SysResult.ok(sysMenu);
    }

    //修改
    @ApiOperation("修改菜单")
    @PostMapping("update")
    public SysResult update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return SysResult.ok();
    }

    //删除菜单
    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public SysResult remove(@PathVariable String id) {
        sysMenuService.removeMenuById(id);
        return SysResult.ok();
    }
}

