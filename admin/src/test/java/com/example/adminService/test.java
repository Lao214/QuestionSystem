package com.example.adminService;

import com.example.adminService.entity.Form;
import com.example.adminService.entity.FormItem;
import com.example.adminService.service.FormItemService;
import com.example.adminService.service.FormService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * @author peterlin
 * @version 1.0
 * @description: TODO
 * @date 2022/10/28 9:53 AM
 */
@SpringBootTest
 class test {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @Test
    void create(){
        mongoTemplate.save("{\n" +
                "  \"_id\": {\n" +
                "    \"$oid\": \"63633606c09ac54077e22716\"\n" +
                "  },\n" +
                "  \"第1题 您投资失败遭遇到一个财务困境。这个情况您觉得自己可以影响掌控到什么程度\": \"完全无法控制\",\n" +
                "  \"第2题 领导忽视您的升职。为了改善这个情况，您觉得自己要负责任到什么程度。\": \"完全不负责\",\n" +
                "  \"工号\": \"12312\",\n" +
                "  \"formId\": \"1587962903200284673\",\n" +
                "  \"createTime\": \"2022-11-03 11:31:18\",\n" +
                "  \"formName\": \"逆商\"\n" +
                "}","表单");
    }

    @Test
    void tianjia(){
        FormItem formItem =new FormItem();
        formItem.setItem("{\n" +
                "\t\"list\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"label\": \"请输入您的标题\",\n" +
                "\t\t\t\"icon\": \"icon-zihao\",\n" +
                "\t\t\t\"options\": {\n" +
                "\t\t\t\t\"textAlign\": \"center\",\n" +
                "\t\t\t\t\"showLabel\": true,\n" +
                "\t\t\t\t\"hidden\": false,\n" +
                "\t\t\t\t\"showRequiredMark\": false,\n" +
                "\t\t\t\t\"noFormItem\": true,\n" +
                "\t\t\t\t\"color\": \"rgba(0, 0, 0, 0.9)\",\n" +
                "\t\t\t\t\"fontFamily\": \"\",\n" +
                "\t\t\t\t\"fontSize\": \"24pt\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"key\": \"text_1666947059803\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\": \"editor\",\n" +
                "\t\t\t\"label\": \"富文本\",\n" +
                "\t\t\t\"list\": [],\n" +
                "\t\t\t\"options\": {\n" +
                "\t\t\t\t\"height\": 300,\n" +
                "\t\t\t\t\"placeholder\": \"请输入\",\n" +
                "\t\t\t\t\"defaultValue\": \"\",\n" +
                "\t\t\t\t\"chinesization\": true,\n" +
                "\t\t\t\t\"hidden\": false,\n" +
                "\t\t\t\t\"disabled\": false,\n" +
                "\t\t\t\t\"showLabel\": false,\n" +
                "\t\t\t\t\"width\": \"100%\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"model\": \"description\",\n" +
                "\t\t\t\"key\": \"editor_1666947747637\",\n" +
                "\t\t\t\"help\": \"\",\n" +
                "\t\t\t\"rules\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"required\": false,\n" +
                "\t\t\t\t\t\"message\": \"必填项\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"type\": \"radio\",\n" +
                "\t\t\t\"label\": \"单选框\",\n" +
                "\t\t\t\"options\": {\n" +
                "\t\t\t\t\"disabled\": false,\n" +
                "\t\t\t\t\"showLabel\": true,\n" +
                "\t\t\t\t\"hidden\": false,\n" +
                "\t\t\t\t\"defaultValue\": \"\",\n" +
                "\t\t\t\t\"dynamicKey\": \"\",\n" +
                "\t\t\t\t\"dynamic\": false,\n" +
                "\t\t\t\t\"options\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"value\": \"1\",\n" +
                "\t\t\t\t\t\t\"label\": \"选项1\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"value\": \"2\",\n" +
                "\t\t\t\t\t\t\"label\": \"选项2\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"value\": \"3\",\n" +
                "\t\t\t\t\t\t\"label\": \"选项3\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"model\": \"radio_1666948851888\",\n" +
                "\t\t\t\"key\": \"radio_1666948851888\",\n" +
                "\t\t\t\"help\": \"\",\n" +
                "\t\t\t\"rules\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"required\": false,\n" +
                "\t\t\t\t\t\"message\": \"必填项\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"config\": {\n" +
                "\t\t\"layout\": \"vertical\",\n" +
                "\t\t\"labelCol\": {\n" +
                "\t\t\t\"span\": 4\n" +
                "\t\t},\n" +
                "\t\t\"wrapperCol\": {\n" +
                "\t\t\t\"span\": 18\n" +
                "\t\t},\n" +
                "\t\t\"hideRequiredMark\": false,\n" +
                "\t\t\"customStyle\": \"\"\n" +
                "\t}\n" +
                "}");
        formItem.setFormId("1585907758421524482");
        formItemService.save(formItem);
    }


}
