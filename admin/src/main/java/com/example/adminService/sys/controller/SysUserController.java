package com.example.adminService.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminService.sys.entity.SysUser;
import com.example.adminService.sys.entity.SysUserQueryVo;
import com.example.adminService.sys.service.SysUserService;
import com.example.adminService.utils.SysResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.MD5;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
@RestController
@RequestMapping("/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("更改用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public SysResult updateStatus(@PathVariable String id,
                                  @PathVariable Integer status) {
        sysUserService.updateStatus(id,status);
        return SysResult.ok();
    }

    @ApiOperation("用户列表")
    @GetMapping("/{page}/{limit}")
    public SysResult list(@PathVariable Long page,
                          @PathVariable Long limit,
                          SysUserQueryVo sysUserQueryVo) {
        //创建page对象
        Page<SysUser> pageParam = new Page<>(page,limit);
        //调用service方法
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam,sysUserQueryVo);
        return SysResult.ok(pageModel);
    }

    @ApiOperation("添加用户")
    @PostMapping("save")
    public SysResult save(@RequestBody SysUser user) {
        //把输入密码进行加密 MD5
        String encrypt = MD5.encrypt(user.getPassword());
        user.setStatus(1);
        user.setPassword(encrypt);
        boolean is_Success = sysUserService.save(user);
        if(is_Success) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("getUser/{id}")
    public SysResult getUser(@PathVariable String id) {
        SysUser user = sysUserService.getById(id);
        return SysResult.ok(user);
    }

    @ApiOperation("修改用户")
    @PostMapping("update")
    public SysResult update(@RequestBody SysUser user) {
        boolean is_Success = sysUserService.updateById(user);
        if(is_Success) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

    @ApiOperation("删除用户")
    @DeleteMapping("remove/{id}")
    public SysResult remove(@PathVariable String id) {
        boolean is_Success = sysUserService.removeById(id);
        if(is_Success) {
            return SysResult.ok();
        } else {
            return SysResult.fail();
        }
    }

}

