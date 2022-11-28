package com.example.adminService.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import utils.Calculator;
import utils.Result;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
    public Result postFormData(@RequestBody FormVo formvo, HttpServletRequest request){
        Form form = formService.getById(formvo.getId());
        formvo.setData(formvo.getData().replaceAll("\\.","_"));
        if(form.getStatus()==0){
            return Result.error().msg("表单状态异常");
        }
        if(form.getStatus()==1){
            Map<String,String> dataMap = JSONObject.parseObject(formvo.getData(), Map.class);
            String resultStr = "";
             if(form.getType()==1){
                 String formula = form.getEvaluateLogic();
                 formula = formula.replaceAll("x","*");
                 formula = formula.replaceAll("÷","/");
                 //type =1 表示有反馈的问卷
                 Map<Object,Object> map = JSONObject.parseObject(formvo.getScoreJSON(), Map.class);
                 for (Map.Entry<Object,Object> entry:map.entrySet()){
                     formula =formula.replaceAll(entry.getKey().toString(),entry.getValue().toString());
                 }
                 String[] splitFormula = formula.split(",");
                 Calculator calculator = new Calculator();
                 for(int i=0;i < splitFormula.length;i++){
                     int index = splitFormula[i].indexOf("=");
                     splitFormula[i] = splitFormula[i].substring(0,index);
                     double result = calculator.executeExpression(splitFormula[i]);
                     dataMap.put("result"+(i+1),result+"");
                     resultStr = resultStr + result + ",";
                     for (int j=0;j<splitFormula.length;j++){
                         splitFormula[j] = splitFormula[j].replaceAll("result"+(i+1),result+"");
                     }
                 }
                 String dataToJSONString = JSONObject.toJSONString(dataMap);
                 mongoTemplate.save(dataToJSONString, formvo.getTitle() + formvo.getId());
                 return Result.success().data("dataMap",dataMap).data("evaluateUrlPhone",form.getEvaluatePhone()).data("evaluateUrlWeb",form.getEvaluateWeb()).data("resultStr",resultStr);
             }else {
                 mongoTemplate.save(formvo.getData(), formvo.getTitle() + formvo.getId());
                 return Result.success().data("evaluateUrlPhone",form.getEvaluatePhone()).data("evaluateUrlWeb",form.getEvaluateWeb());
             }
        }
        return null;
    }

//    @PostMapping("getFormDataListPage/{current}/{limit}")
//    public Result getFormDataListPage(@PathVariable long current, @PathVariable int limit, @RequestBody(required = false) FormQuery formQuery){
//        QueryWrapper<FormItem> queryWrapper =new QueryWrapper<>();
//        queryWrapper.eq("form_id",formQuery.getId());
//        FormItem one = formItemService.getOne(queryWrapper);
//        one.setItem(one.getItem().replaceAll("\\.","_"));
//        List<ItemVo> itemVos = JSONArray.parseArray(one.getItem(), ItemVo.class);
//        List<String> header = new ArrayList<>();
//        header.add("createTime");
//        header.add("bg");
//        header.add("bu");
//        header.add("jobNo");
//        header.add("result1");
//        header.add("result2");
//        header.add("result3");
//        header.add("area");
//        header.add("sex");
//        header.add("hrtype");
//        Query query = new Query();
//        for(ItemVo iv : itemVos) {
//            if(iv.getComponent().equals("radioGroup") || iv.getComponent().equals("slider")){
//                query.fields().include(iv.getModelValue());
//                header.add(iv.getModelValue());
//            }
//        }
//        header.add("submitAddress");
//        header.add("submitOs");
//        query.fields().exclude("_id");
//        query.fields().include("createTime");
//        query.fields().include("realname");
//        query.fields().include("result3");
//        query.fields().include("result2");
//        query.fields().include("result1");
//        query.fields().include("submitAddress");
//        query.fields().include("submitOs");
//        query.fields().include("bg");
//        query.fields().include("bu");
//        query.fields().include("jobNo");
//        query.fields().include("area");
//        query.fields().include("sex");
//        query.fields().include("hrtype");
//        long total = mongoTemplate.count(query, one.getName() + formQuery.getId());
//        query.limit(limit);
//        query.skip((current-1)*10);
//        List<Map> strings = mongoTemplate.find(query, Map.class, one.getName() + formQuery.getId());
//        return Result.success().data("map",strings).data("total",total).data("header",header);
//    }

    @PostMapping("getFormDataListPage/{current}/{limit}")
    public Result getFormDataListPage(@PathVariable long current, @PathVariable int limit, @RequestBody(required = false) FormQuery formQuery){
        QueryWrapper<FormItem> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("form_id",formQuery.getId());
        FormItem one = formItemService.getOne(queryWrapper);
        one.setItem(one.getItem().replaceAll("\\.","_"));
        List<ItemVo> itemVos = JSONArray.parseArray(one.getItem(), ItemVo.class);
        Query query = new Query();
        query.fields().exclude("_id");
        query.fields().exclude("isSubscriber");
        query.fields().exclude("sign");
        query.fields().exclude("createBy");
        query.fields().exclude("formId");
        query.fields().exclude("fromPC");
        query.fields().exclude("serviceno");
        long total = mongoTemplate.count(query, one.getName() + formQuery.getId());
        query.limit(limit);
        query.skip((current-1)*10);
        List<Map> strings = mongoTemplate.find(query, Map.class, one.getName() + formQuery.getId());
        return Result.success().data("map",strings).data("total",total);
    }

}
