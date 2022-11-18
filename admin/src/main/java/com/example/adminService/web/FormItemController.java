package com.example.adminService.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.FormItem;
import com.example.adminService.entity.Vo.FormVo;
import com.example.adminService.service.FormItemService;
import com.example.adminService.service.FormService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import utils.Result;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
@RestController
@RequestMapping("/webApi/formItem")
public class FormItemController {

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @description: 查询表单项
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2022/9/27 1:31 PM
     */
    @GetMapping("getFormItemByFormId/{id}")
    @ApiOperation(value = "查询表单项")
    public Result getFormItemByFormId(@PathVariable String id){
        Object formItem = redisTemplate.opsForValue().get(id + "item");
        if(formItem == null) {
            QueryWrapper<FormItem> formItemQueryWrapper=new QueryWrapper<>();
            formItemQueryWrapper.eq("form_id",id);
            FormItem one = formItemService.getOne(formItemQueryWrapper);
            if(one!=null){
                redisTemplate.opsForValue().set(id+"item",one);
                return Result.success().data("formItem",one);
            }else {
                return Result.error();
            }
        }else {
            return Result.success().data("formItem",formItem);
        }
    }

    @PostMapping("updateFormItem")
    @ApiOperation(value = "更新表单项")
    public Result updateFormItem(@RequestBody FormVo formvo){
        Form form =new Form();
        form.setId(formvo.getId());
        form.setEvaluateLogic(formvo.getFormula());
        form.setType(1);
        form.setUpdateTime(new Date());
        boolean update = formService.updateById(form);
        if(update){
            Object o = redisTemplate.opsForValue().get(formvo.getId() + "item");
            if(o != null) {
                redisTemplate.delete(formvo.getId()+"item");
            }
            FormItem formItem =new FormItem();
            formItem.setFormId(formvo.getId());
            formItem.setItem(formvo.getValues());
            boolean updateOK = formItemService.updateByFormId(formItem);
            if(updateOK){
                return  Result.success();
            }
        }
        return Result.error();
    }


}

