package com.example.adminService.web;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.FormItem;
import com.example.adminService.entity.QueryVo.FormQuery;
import com.example.adminService.entity.Vo.FormVo;
import com.example.adminService.entity.Vo.ItemVo;
import com.example.adminService.security.TokenManager;
import com.example.adminService.service.FormItemService;
import com.example.adminService.service.FormService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import utils.Result;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @author 劳威锟
 * @version 1.0
 * @description: TODO
 * @date 2022/11/3 10:28 AM
 */
@RestController
@RequestMapping("/webApi/formData")
public class formDataController {
    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @PostMapping("postFormData")
    @ApiOperation(value = "提交数据")
    public Result addForm(@RequestBody FormVo formvo, HttpServletRequest request){
        Form form = formService.getById(formvo.getId());
        if(form.getStatus()==0){
            return Result.error().msg("表单状态异常");
        }
        if(form.getStatus()==1){
             mongoTemplate.save(formvo.getData(), formvo.getTitle() + formvo.getId());
             return Result.success();
        }
        return null;
    }

    @PostMapping("getFormDataListPage/{current}/{limit}")
    public Result getFormDataListPage(@PathVariable long current, @PathVariable int limit, @RequestBody(required = false) FormQuery formQuery){
        QueryWrapper<FormItem> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("form_id",formQuery.getId());
        FormItem one = formItemService.getOne(queryWrapper);
        List<ItemVo> itemVos = JSONArray.parseArray(one.getItem(), ItemVo.class);
        Query query = new Query();
        for(ItemVo iv : itemVos){
            if(iv.getComponent().equals("radioGroup") || iv.getComponent().equals("slider")){
                query.fields().include(iv.getModelValue());
            }
        }
        long total = mongoTemplate.count(query, one.getName() + formQuery.getId());
        query.fields().exclude("_id");
        query.limit(limit);
        query.skip((current-1)*10);
        List<Map> strings = mongoTemplate.find(query, Map.class, one.getName() + formQuery.getId());
        return Result.success().data("map",strings).data("total",total);
    }



}
