package com.example.adminService.sys.securityFilter;

import com.alibaba.fastjson.JSON;
import com.example.adminService.sys.custom.CustomUser;
import com.example.adminService.sys.entity.LoginVo;
import com.example.adminService.utils.SysResult;
import com.example.adminService.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.example.adminService.utils.JwtHelper;
import com.example.adminService.utils.ResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lao
 * @version 1.0
 * @description: TODO
 * @date 2022/11/30 10:55 AM
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

   public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate){
       this.setAuthenticationManager(authenticationManager);
       this.setPostOnly(false);
       //指定登录接口及提交方式，可以指定任意路径
       this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
       this.redisTemplate = redisTemplate;
   }


    //获取用户名和密码，认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //流的方式获取用户信息封装到对象中
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            //封装用户信息到 UsernamePasswordAuthenticationToken
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //认证成功调用
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        //获取认证对象
        CustomUser customUser = (CustomUser)auth.getPrincipal();

        //保存权限数据
        redisTemplate.opsForValue().set(customUser.getUsername(),
                JSON.toJSONString(customUser.getAuthorities()));

        //生成token ,传入id和用户
        String token = JwtHelper.createToken(customUser.getSysUser().getId().toString(), customUser.getSysUser().getUsername());

        //返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, SysResult.ok(map));
    }


    //认证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if(e.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, SysResult.build(null, 204, e.getMessage()));
        } else {
            ResponseUtil.out(response, SysResult.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }

}

