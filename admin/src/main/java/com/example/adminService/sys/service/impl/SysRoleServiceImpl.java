package com.example.adminService.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.adminService.sys.entity.AssginRoleVo;
import com.example.adminService.sys.entity.SysRole;
import com.example.adminService.sys.entity.SysRoleQueryVo;
import com.example.adminService.sys.entity.SysUserRole;
import com.example.adminService.mapper.SysRoleMapper;
import com.example.adminService.mapper.SysUserRoleMapper;
import com.example.adminService.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-28
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
        IPage<SysRole> pageModel = baseMapper.selectPage(pageParam,sysRoleQueryVo);
        return pageModel;
    }

    //获取用户的角色数据
    @Override
    public Map<String, Object> getRolesByUserId(String userId) {
        //获取所有角色
        List<SysRole> roles = baseMapper.selectList(null);
        //根据用户id查询，已经分配角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<SysUserRole> userRolesList = sysUserRoleMapper.selectList(wrapper);
        //从userRoles集合获取所有角色id
        List<Long> userRoleIds = new ArrayList<>();
        for (SysUserRole userRole:userRolesList) {
            Long roleId = userRole.getRoleId();
            userRoleIds.add(roleId);
        }
        //封装到map集合
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",roles);//所有角色
        returnMap.put("userRoleIds",userRoleIds);//用户分配角色id集合
        return returnMap;
    }


    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //根据用户id删除之前分配角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",assginRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);
        //获取所有角色id，添加角色用户关系表
        //角色id列表
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        for (String roleId:roleIdList) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(assginRoleVo.getUserId());
            userRole.setRoleId(Long.parseLong(roleId));
            sysUserRoleMapper.insert(userRole);
        }
    }
}
