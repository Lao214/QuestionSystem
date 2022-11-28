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
import org.springframework.util.StringUtils;
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

    @PostMapping("saveUIJSONPC")
    public Result saveUIJSONPC(@RequestBody FormVo formvo, HttpServletRequest request){
        EvaluateUi evaluateUi =new EvaluateUi();
        if (formvo.getUiKey() < 1) {
            /**如果没有 uiKey 说明还没创建，创建UI模板。**/
            evaluateUi.setCollections(0l);
            evaluateUi.setLikes(0l);
            evaluateUi.setCreateTime(new Date());
            evaluateUi.setUpdateTime(new Date());
            evaluateUi.setType("PC");
            evaluateUi.setComponents(formvo.getData());
            String jwtToken = request.getHeader("token");
            String username = tokenManager.getUserFromToken(jwtToken);
            evaluateUi.setUser(username);
            evaluateUi.setIsPublish(0);
            boolean save = evaluateUiService.save(evaluateUi);
            if(save){
                Form form =new Form();
                form.setId(formvo.getId());
                form.setEvaluateWeb(evaluateUi.getId().intValue());
                boolean update = formService.updateById(form);
                if(update) {
                    return Result.success().data("uiKey", evaluateUi.getId()).msg("添加成功");
                }
            }
            return Result.error();
        }
        else {
            evaluateUi.setUpdateTime(new Date());
            evaluateUi.setComponents(formvo.getData());
            evaluateUi.setId(formvo.getUiKey());
            boolean update = evaluateUiService.updateById(evaluateUi);
            if(update) {
                return Result.success().data("uiKey", evaluateUi.getId()).msg("修改成功");
            } else {
                return Result.error();
            }
        }
    }

    @GetMapping("getUiKeyPC/{formId}")
    public Result getUiKeyPC(@PathVariable String formId, HttpServletRequest request){
        QueryWrapper<Form> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",formId);
        Form form = formService.getOne(queryWrapper);
        QueryWrapper<EvaluateUi> queryWrapperUI =new QueryWrapper<>();
        queryWrapperUI.eq("id",form.getEvaluateWeb());
        EvaluateUi evaluateUi = evaluateUiService.getOne(queryWrapperUI);
        return Result.success().data("ui",evaluateUi);
    }

    @GetMapping("getWebUiByFormId/{formId}")
    public Result getUiByFormId(@PathVariable String formId, HttpServletRequest request){
        Form form = formService.getOne(new QueryWrapper<Form>().eq("id",formId));
        EvaluateUi evaluateUi = evaluateUiService.getOne(new QueryWrapper<EvaluateUi>().eq("id", form.getEvaluateWeb()));
        return Result.success().data("evaluateUi",evaluateUi);
    }
}

