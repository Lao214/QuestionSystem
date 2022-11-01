package com.example.adminService.service;

import com.example.adminService.entity.FormItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
public interface FormItemService extends IService<FormItem> {

    boolean updateByFormId(FormItem formItem);
}
