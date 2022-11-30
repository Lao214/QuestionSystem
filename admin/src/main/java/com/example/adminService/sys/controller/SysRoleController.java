package com.example.adminService.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminService.sys.entity.AssginRoleVo;
import com.example.adminService.sys.entity.SysRole;
import com.example.adminService.sys.entity.SysRoleQueryVo;
import com.example.adminService.sys.exception.EchoesException;
import com.example.adminService.sys.service.SysRoleService;
import com.example.adminService.utils.SysResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("获取用户的角色数据")
    @GetMapping("toAssign/{userId}")
    public SysResult toAssign(@PathVariable String userId) {
        Map<String,Object> roleMap = sysRoleService.getRolesByUserId(userId);
        return SysResult.ok(roleMap);
    }

    @ApiOperation("用户分配角色")
    @PostMapping("doAssign")
    public SysResult doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return SysResult.ok();
    }

    //7 批量删除
    // 多个id值 [1,2,3]
    // json数组格式 --- java的list集合
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public SysResult batchRemove(@RequestBody List<Long> ids) {
        sysRoleService.removeByIds(ids);
        return SysResult.ok();
    }

    //6 修改-最终修改
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("最终修改")
    @PostMapping("update")
    public SysResult updateRole(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.updateById(sysRole);
        if(isSuccess) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

    //5 修改-根据id查询
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询")
    @PostMapping("findRoleById/{id}")
    public SysResult findRoleById(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return SysResult.ok(sysRole);
    }

    //4 添加
    // @RequestBody 不能使用get提交方式
    // 传递json格式数据，把json格式数据封装到对象里面 {...}
//    @Log(title = "角色管理",businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public SysResult saveRole(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.save(sysRole);
        if(isSuccess) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

    //3 条件分页查询
    // page当前页  limit每页记录数
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public SysResult findPageQueryRole(@PathVariable Long page,
                                       @PathVariable Long limit,
                                       SysRoleQueryVo sysRoleQueryVo) {
        //创建page对象
        Page<SysRole> pageParam = new Page<>(page,limit);
        //调用service方法
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam,sysRoleQueryVo);
        //返回
        return SysResult.ok(pageModel);
    }

    //2 逻辑删除接口
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("逻辑删除接口")
    @DeleteMapping("remove/{id}")
    public SysResult removeRole(@PathVariable Long id) {
        //调用方法删除
        boolean isSuccess = sysRoleService.removeById(id);
        if(isSuccess) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

}

