package com.example.adminService.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.adminService.sys.entity.SysRole;
import com.example.adminService.sys.entity.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    //条件分页查询
    IPage<SysRole> selectPage(Page<SysRole> pageParam, @Param("vo") SysRoleQueryVo sysRoleQueryVo);

}
