package com.example.adminService.mapper;

import com.example.adminService.entity.FormItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
public interface FormItemMapper extends BaseMapper<FormItem> {

    boolean updateByFormId(FormItem formItem);
}
