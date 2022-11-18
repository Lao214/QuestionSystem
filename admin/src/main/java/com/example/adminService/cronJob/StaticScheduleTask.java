package com.example.adminService.cronJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.ViewCount;
import com.example.adminService.service.FormService;
import com.example.adminService.service.ViewCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.adminService.utils.RedisCache;

import java.util.Date;
import java.util.List;

/**
 * @author 劳威锟
 * @version 1.0
 * @description: TODO
 * @date 2022/11/18 10:08 AM
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {

    @Autowired
    RedisCache redisCache;

    @Autowired
    FormService formService;

    @Autowired
    ViewCountService viewCountService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void countViewEveryDay(){
        QueryWrapper<Form> queryWrapper =new QueryWrapper<>();
        List<Form> list = formService.list(queryWrapper);
        for (Form form: list) {
            Integer viewCache = redisCache.getCacheObject(form.getId());
            /**如果今天有人访问 数据库插入一条数据**/
            if(viewCache != null){
                ViewCount viewCountEntity =new ViewCount();
                viewCountEntity.setCount(viewCache.longValue());
                viewCountEntity.setFormId(form.getId());
                viewCountEntity.setCreateTime(new Date());
                viewCountService.save(viewCountEntity);
            }
        }
    }
}
