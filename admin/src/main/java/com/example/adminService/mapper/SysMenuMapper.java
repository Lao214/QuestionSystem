package com.example.adminService.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.adminService.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findMenuListUserId(@Param("userId") String userId);
}
