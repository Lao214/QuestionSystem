package com.example.adminService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.Vo.FormVo;
import com.example.adminService.service.FormService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import utils.Calculator;

import java.util.Date;
import java.util.Map;

/**
 * @author peterlin
 * @version 1.0
 * @description: TODO
 * @date 2022/12/3 8:38 AM
 */
@SpringBootTest
 class test {
    @Autowired
    private Calculator calculator;

    @Autowired
    private FormService formService;

    @Autowired
    private MongoTemplate mongoTemplate;

    String data = "\"{\"source\":\"其他\",\"formId\":\"1591227933773537281\",\"createTime\":\"2022-12-03 08:12:25\",\"1. 有新的概念和事情出现时，有时会让我从之前的想法和事情中分心。\":\"非常像我\",\"2. 我不会因为挫折就气馁，我不轻易放弃。\":\"很像我\",\"3. 我常常设定目标以后，又改追求不同的目标。\":\"有点像我\",\"4. 我很努力工作。\":\"不太像我\",\"5. 我很难专注于需要花费好几个月\":\"一点都不像我\",\"6. 任何事情只要开始动手，我一定要完成才肯罢休。\":\"不太像我\",\"7. 每年我的兴趣都会改变。\":\"有点像我\",\"8. 我很勤奋，从不放弃。\":\"很像我\",\"9. 我曾经有很短的一段时间对某个点子或事情很入迷，但后来就失去兴趣了。\":\"非常像我\",\"10. 为了克服重要的挑战，我不害怕挫折的打击。\":\"不太像我\",\"submitOs\":\"Mac OS\",\"completeTime\":11}\"";
    String scoreJSON = "{\"q1\":\"5\",\"q2\":\"4\",\"q3\":\"3\",\"q4\":\"2\",\"q5\":\"5\",\"q6\":\"2\",\"q7\":\"3\",\"q8\":\"3\",\"q9\":\"5\",\"s10\":\"2\"}";

    @Test
     void postFormDat(){
        FormVo formvo =new FormVo();
        formvo.setData(data);
        formvo.setScoreJSON(scoreJSON);
        formvo.setId("1591227933773537281");
        formvo.setJobNo("X2004611");
        formvo.setTitle("TEST");
        String ipAddr = "127.0.0.1";
        String submitAddress = "内网IP";
        String dateStr = DateUtil.formatDate(new Date()).replaceAll("-", "");
        String createBy = "";

        if(StringUtils.isEmpty(formvo.getJobNo())){
            String ua = formvo.getUa()+ipAddr;
            String uaId = UUID.nameUUIDFromBytes((ua).getBytes()).toString();
            createBy = uaId + "-" + dateStr;
        } else {
            createBy = formvo.getJobNo() + dateStr;
        }
        Form form = formService.getById(formvo.getId());
        formvo.setData(formvo.getData().replaceAll("\\.","_"));
        if(form.getStatus()==0){

        }
        if(form.getStatus()==1){
            Map<String,String> dataMap = JSONObject.parseObject(formvo.getData(), Map.class);
            dataMap.put("createBy", createBy);
            dataMap.put("submitAddress", submitAddress);
            String resultStr = "";
            if(form.getType()==1){
                String formula = form.getEvaluateLogic();
                formula = formula.replaceAll("x","*");
                formula = formula.replaceAll("÷","/");
                //type =1 表示有反馈的问卷
                Map<Object,Object> map = JSONObject.parseObject(formvo.getScoreJSON(), Map.class);
                for (Map.Entry<Object,Object> entry:map.entrySet()){
                    formula = formula.replaceAll(entry.getKey().toString(),entry.getValue().toString());
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
                mongoTemplate.insert(dataToJSONString, formvo.getTitle() + formvo.getId());
//                return Result.success().data("dataMap",dataMap).data("evaluateUrlPhone",form.getEvaluatePhone()).data("evaluateUrlWeb",form.getEvaluateWeb()).data("resultStr",resultStr);
            }else {
                String dataToJSONString = JSONObject.toJSONString(dataMap);
                mongoTemplate.insert(dataToJSONString, formvo.getTitle() + formvo.getId());
//                return Result.success().data("evaluateUrlPhone",form.getEvaluatePhone()).data("evaluateUrlWeb",form.getEvaluateWeb());
            }
        }
    }
}
