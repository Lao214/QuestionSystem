package com.example.adminService.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 劳威锟
 * @version 1.0
 * @description: TODO
 * @date 2022/9/20 8:09 AM
 */
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;



    /**
     * @description: 缓存基本的对象，Integer、String、实体类等
     * @param: key 缓存的键值
     * @param:  value 缓存的值
     * @author 劳威锟
     * @date: 2022/9/20 8:11 AM
     */

    public <T> void setCacheObject(final  String key,final  T value)
    {
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     * @description: 缓存基本的对象，Integer、String、实体类等
     * @param: key 缓存的键值
     * @param:  value 缓存的值
     * @param:  timeOut 时间
     * @param:  timeUnit 时间颗粒度
     * @author 劳威锟
     * @date: 2022/9/20 8:11 AM
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    /**
     * @description: 设置有效时间
     * @param:  key Redis键
     * @param: timeOut 超时时间
     * @return: true= 设置成； false= 设置失败
     * @author 劳威锟
     * @date: 2022/9/20 8:18 AM
     */

    public  boolean expire(final String key,final long timeOut,final TimeUnit unit){
        return  redisTemplate.expire(key,timeOut,unit);
    }

    /**
     * @description: 获得缓存的基本对象
     * @param:  key 缓存键值
     * @return: 该键值对应的数据
     * @author  劳威锟
     * @date: 2022/9/20 8:21 AM
     */
    public <T> T getCacheObject(final  String key){
        ValueOperations<String,T> operation=redisTemplate.opsForValue();
        return  operation.get(key);
    }


    /**
     * @description: 删除单个对象
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:23 AM
     */
    public  boolean deleteObject(final String key){
        return  redisTemplate.delete(key);
    }

    /**
     * @description: 删除集合对象
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:24 AM
     */

    public long deleteObject(final Collection collection){
        return  redisTemplate.delete(collection);
    }

    /**
     * @description: 缓存list数据
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:25 AM
     */
    public  <T> long setCacheList(final String key,final List<T> dataList){
        Long count =redisTemplate.opsForList().rightPushAll(key,dataList);
        return  count ==null?0:count;
    }

    /**
     * @description: 获取缓存的list对象
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:27 AM
     */

    public  <T> List<T> getCacheList(final String key){
        return redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * @description: 缓存set
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:28 AM
     */
    public <T>BoundSetOperations<String,T> setCacheSet(final String key, final Set<T> dataSet){
        BoundSetOperations<String,T> setOperation =redisTemplate.boundSetOps(key);
        Iterator<T> it =dataSet.iterator();
        while (it.hasNext()){
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * @description: 获取缓存的set
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:31 AM
     */
    public <T> Set<T> getCacheSet(final String key){
        return  redisTemplate.opsForSet().members(key);
    }

    /**
     * @description: 缓存map
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:32 AM
     */
    public  <T> void  setCacheMap(final  String key,final Map<String,T> dataMap){
        if(dataMap!=null){
            redisTemplate.opsForHash().putAll(key,dataMap);
        }
    }

    /**
     * @description: 获得缓存的map
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:34 AM
     */
    public  <T> Map<String,T> getCacheMap(final  String key){
        return  redisTemplate.opsForHash().entries(key);
    }
    
    /** 
     * @description: 往hash中存入数据 
     * @param:  
     * @return:  
     * @author peterlin
     * @date: 2022/9/20 8:35 AM
     */ 
    public <T> void  setCacheMapValue(final String key,final String hkey,final T value){
        
        redisTemplate.opsForHash().put(key,hkey,value);
    }
    
    /** 
     * @description:  获取hash中的数据
     * @param:  
     * @return:  
     * @author peterlin
     * @date: 2022/9/20 8:36 AM
     */
    public <T> T getCacheMapValue(final String key,final String hKey){
        HashOperations<String,String,T> opsForHash=redisTemplate.opsForHash();
        return opsForHash.get(key,hKey);
    }

    /**
     * @description: 删除hash中的数据
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:38 AM
     */
    public void delCacheMapValue(final String key,final String hkey){
        HashOperations hashOperations =redisTemplate.opsForHash();
        hashOperations.delete(key,hkey);
    }

    /**
     * @description: 获取多个hash中的数据
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:39 AM
     */
    public <T> List<T> getMultiCacheMapValue(final String key,final Collection<Object> hKeys){
        return  redisTemplate.opsForHash().multiGet(key,hKeys);
    }

    /**
     * @description: 获得缓存的基本对象列表
     * @param:
     * @return:
     * @author peterlin
     * @date: 2022/9/20 8:42 AM
     */
    public  Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }


//    public static void main(String[] args) {
//        DateTime dateTime = DateUtil.offsetDay(new Date(), -30);
//        System.out.println(dateTime.toString());
//    }


}
