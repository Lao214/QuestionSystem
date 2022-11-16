package com.example.adminService.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminService.entity.EvaluateUi;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.QueryVo.FormQuery;
import com.example.adminService.entity.Vo.FormVo;
import com.example.adminService.security.TokenManager;
import com.example.adminService.service.EvaluateUiService;
import com.example.adminService.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>反馈的界面pc端 前端控制器</p>
 * @author 劳威锟
 * @since 2022-11-14
 */
@RestController
@RequestMapping("/webApi/evaluateUi")
public class EvaluateUiController {

    @Autowired
    EvaluateUiService evaluateUiService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    FormService formService;

    @PostMapping("saveUIJSON")
    public Result saveUIJSON(@RequestBody FormVo formvo, HttpServletRequest request){
        EvaluateUi evaluateUi =new EvaluateUi();
        evaluateUi.setCollection(0l);
        evaluateUi.setCreateTime(new Date());
        evaluateUi.setUpdateTime(new Date());
        evaluateUi.setLike(0l);
        evaluateUi.setType(formvo.getUiType());
        evaluateUi.setComponents(formvo.getData());
        String jwtToken = request.getHeader("token");
        String username = tokenManager.getUserFromToken(jwtToken);
        evaluateUi.setUser(username);
        evaluateUi.setIsPublish(0);
        boolean save = evaluateUiService.save(evaluateUi);
        if(save){
            Form form =new Form();
            form.setId(formvo.getId());
            if(evaluateUi.getType().equals("PC")){
                form.setEvaluateWeb(evaluateUi.getId().intValue());
            } else if(evaluateUi.getType().equals("Phone")){
                form.setEvaluatePhone(evaluateUi.getId().intValue());
            }
            formService.updateById(form);
        }
        return Result.success();
    }

    @PostMapping("updateUIJSON")
    public Result updateUIJSON(@RequestBody FormVo formvo, HttpServletRequest request){
        EvaluateUi evaluateUi =new EvaluateUi();
        evaluateUi.setUpdateTime(new Date());
        evaluateUi.setComponents(formvo.getData());
        QueryWrapper<Form> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",formvo.getId());
        Form form = formService.getOne(queryWrapper);
        if(formvo.getUiType().equals("PC")){
            evaluateUi.setId(form.getEvaluateWeb().longValue());
        } else if(formvo.getUiType().equals("Phone")) {
            evaluateUi.setId(form.getEvaluatePhone().longValue());
        }
        boolean save = evaluateUiService.updateById(evaluateUi);
        return Result.success();
    }
}

