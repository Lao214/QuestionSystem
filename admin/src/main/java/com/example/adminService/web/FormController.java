package com.example.adminService.web;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.adminService.utils.AddressUtils;
import com.example.adminService.utils.RedisCache;
import com.example.adminService.entity.Form;
import com.example.adminService.entity.FormItem;
import com.example.adminService.entity.QueryVo.FormQuery;
import com.example.adminService.entity.User;
import com.example.adminService.entity.Vo.FormVo;
import com.example.adminService.security.TokenManager;
import com.example.adminService.service.FormItemService;
import com.example.adminService.service.FormService;
import com.example.adminService.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
@RestController
@RequestMapping("/webApi/form")
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCache redisCache;



    /**
     * @description: 根据条件分页查询list
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2022/9/27 11:15 AM
     * requestBody  使用json传递数据，把json数据封装到对应的对象里面 需要用post提交方式
     * responseBody 返回数据，返回json数据
     */
    @ApiOperation(value = "根据条件分页查询list")
    @PostMapping("queryFormListPage/{current}/{limit}")
    public Result queryFormListPage(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) FormQuery formQuery){
        /**创建page对象**/
        Page<Form> formPage =new Page<>(current,limit);
        /**构建条件**/
        QueryWrapper<Form> queryWrapper =new QueryWrapper<>();
        String  name = formQuery.getName();
        Integer status = formQuery.getStatus();
        Integer type = formQuery.getType();
        /**判断是否为空，如果为空，不拼接条件**/
        if(!StringUtils.isEmpty(name)){
            //构建条件
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)){
            //构建条件
            queryWrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(status)){
            //构建条件
            queryWrapper.eq("type",type);
        }
//        if(!StringUtils.isEmpty(begin)){
//            //构建条件
//            queryWrapper.ge("gmt_create",begin);
//        }
//        if(!StringUtils.isEmpty(end)){
//            //构建条件
//            queryWrapper.le("gmt_create",end);
//        }
        //排序 根据创建时间降序
        queryWrapper.orderByDesc("create_time");
        /**调用分页方法**/
        formService.page(formPage,queryWrapper);
        /**total 为所有记录**/
        long total =formPage.getTotal();
        List<Form> list=formPage.getRecords();
        return  Result.success().data("rows",list).data("total",total);
    }


    /**
     * @description: 添加表单
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2022/9/27 1:31 PM
     */
    @PostMapping("addForm")
    @ApiOperation(value = "添加表单")
    public Result addForm(@RequestBody FormVo formvo, HttpServletRequest request){
        String jwtToken = request.getHeader("token");
        String username = tokenManager.getUserFromToken(jwtToken);
        QueryWrapper<User> formQueryWrapper=new QueryWrapper<>();
        formQueryWrapper.eq("username",username);
        User one = userService.getOne(formQueryWrapper);
        Form form = new Form();
        form.setName(formvo.getTitle());
        form.setUserId(one.getId());
        if(!StringUtils.isEmpty(formvo.getDescription())){
            form.setDescription(formvo.getDescription());
        }
        form.setCreateTime(new Date());
        form.setUpdateTime(new Date());
        boolean save =  formService.save(form);
        if(save){
            FormItem formItem =new FormItem();
            formItem.setName(formvo.getTitle());
            formItem.setFormId(form.getId());
            formItem.setItem(formvo.getValues());
            boolean saveOK = formItemService.save(formItem);
            if(saveOK){
                return Result.success().data("formItem",formItem);
            }else {
                return Result.error();
            }
        }else {
            return Result.error();
        }
    }

    /**
     * @description: 更新表单
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2022/9/27 1:31 PM
     */
    @PostMapping("update")
    @ApiOperation(value = "修改表单")
    public Result update(@RequestBody FormVo formvo, HttpServletRequest request){
            Form form =new Form();
            form.setId(formvo.getId());
            form.setName(formvo.getTitle());
            if(!StringUtils.isEmpty(formvo.getDescription())){
                form.setDescription(formvo.getDescription());
            }
            boolean save = formService.updateById(form);
            if(save){
                FormItem formItem = new FormItem();
                formItem.setName(formvo.getTitle());
                formItem.setItem(formvo.getValues());
                formItem.setFormId(formvo.getId());
                boolean saveOK = formItemService.updateByFormId(formItem);
                if(saveOK){
                    Object o = redisTemplate.opsForValue().get(formvo.getId() + "item");
                    if(o != null){
                        redisTemplate.delete(formvo.getId()+"item");
                    }
                    return Result.success().data("formItem",formItem);
                }else {
                    return Result.error();
                }
            }else {
                return Result.error();
            }
    }

    /**
     * @description: 发布表单
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2022/9/27 1:31 PM
     */
    @PostMapping("publish/{id}")
    @ApiOperation(value = "发布")
    public Result publish(@PathVariable String id, HttpServletResponse response)throws  Exception{
        Form form =new Form();
        form.setId(id);
        form.setStatus(1);
        boolean save = formService.updateById(form);
        if(save){
            return Result.success().data("status",form.getStatus());
        }else {
            return Result.error();
        }
    }

    @GetMapping("/qrCode/{id}")
    @ApiOperation(value = "获取二维码")
    public void qrCode(HttpServletResponse response,@PathVariable String id) throws Exception {
        String text = "http://localhost:9528/#/build?id="+id;
//        String text = "https://www.csdn.net/";
        String projectPath = System.getProperty("user.dir");
        String logoPath = projectPath+ "/src/resources/static/muye.png";
        //String destPath = projectPath+ "/src/resources/static/qrcode/"+id+".jpg";
        QrCodeUtils.encode(text,logoPath,response.getOutputStream(),true);
    }

    @GetMapping("/getByFormId/{id}")
    public Result getByFormId(@PathVariable String id) throws Exception {
        QueryWrapper<Form> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Form one = formService.getOne(queryWrapper);
        return Result.success().data("form",one);
    }


    @PostMapping("viewCount")
    public Result viewCount(HttpServletRequest request,@RequestBody FormVo formvo){
        String submitID = "";
        String ipAddr = HttpUtils.getIpAddr(request);
        String submitAddress =  AddressUtils.getRealAddressByIP(ipAddr);
        /**判断有无工号**/
        if(StringUtils.isEmpty(formvo.getJobNo())){
            /**首先检测该访问者今天有没有访问过**/
            Object visitorCache = redisCache.getCacheObject(formvo.getUa()+ipAddr+formvo.getId());
            String dateStr = DateUtil.formatDate(new Date()).replaceAll("-","");
            String visitorStr = RandomUtil.randomNumbers(7);
            if(visitorCache ==null){
                /**如果没有就缓存，然后检测该问卷有没有人访问过**/
                redisCache.setCacheObject(formvo.getUa()+ipAddr+formvo.getId(),visitorStr+dateStr,3, TimeUnit.DAYS);
                Integer viewCache = redisCache.getCacheObject(formvo.getId());
                if(viewCache == null){
                    redisCache.setCacheObject(formvo.getId(),1);
//                    /**数据库插入一条数据**/
//                    ViewCountEntity viewCountEntity =new ViewCountEntity();
//                    viewCountEntity.setCount(1l);
//                    viewCountEntity.setFormKey(formKey);
//                    viewCountService.save(viewCountEntity);
                    return Result.success().data("submitID",visitorStr+dateStr).data("submitAddress",submitAddress);
                }else {
                    redisCache.setCacheObject(formvo.getId(),viewCache+1);
                    return Result.success().data("submitID",visitorStr+dateStr).data("submitAddress",submitAddress);
                }
            }else {
                return Result.success().data("submitID",visitorCache).data("submitAddress",submitAddress);
            }
        }
        else {
            /**首先检测该员工今天有没有访问过**/
            String visitorCache = redisCache.getCacheObject(formvo.getJobNo() + "-" + formvo.getId());
            if (visitorCache == null) {
                String dateStr = DateUtil.formatDate(new Date()).replaceAll("-", "");
                redisCache.setCacheObject(formvo.getJobNo() + "-" + formvo.getId(), formvo.getJobNo() + dateStr, 3, TimeUnit.DAYS);
                Integer viewCache = redisCache.getCacheObject(formvo.getId());
                if (viewCache == null) {
                    redisCache.setCacheObject(formvo.getId(), 1);
                    return Result.success().data("submitID", formvo.getJobNo() + dateStr).data("submitAddress", submitAddress);
                } else {
                    redisCache.setCacheObject(formvo.getId(), viewCache + 1);
                    return Result.success().data("submitID", formvo.getJobNo() + dateStr).data("submitAddress", submitAddress);
                }
            } else {
                return Result.success().data("submitID", visitorCache).data("submitAddress", submitAddress);
            }
        }
    }

}

