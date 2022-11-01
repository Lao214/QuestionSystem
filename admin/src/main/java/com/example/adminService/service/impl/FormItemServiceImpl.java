package com.example.adminService.service.impl;

import com.example.adminService.entity.FormItem;
import com.example.adminService.mapper.FormItemMapper;
import com.example.adminService.service.FormItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
@Service
public class FormItemServiceImpl extends ServiceImpl<FormItemMapper, FormItem> implements FormItemService {


    @Override
    public boolean updateByFormId(FormItem formItem) {
        return baseMapper.updateByFormId(formItem);
    }
}
