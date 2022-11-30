package com.example.adminService.utils;

import lombok.Data;

@Data
public class SysResult<T> {

    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    public SysResult(){}

    // 返回数据
    protected static <T> SysResult<T> build(T data) {
        SysResult<T> result = new SysResult<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public static <T> SysResult<T> build(T body, Integer code, String message) {
        SysResult<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> SysResult<T> build(T body, ResultCodeEnum resultCodeEnum) {
        SysResult<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static<T> SysResult<T> ok(){
        return SysResult.ok(null);
    }

    /**
     * 操作成功
     * @param data  baseCategory1List
     * @param <T>
     * @return
     */
    public static<T> SysResult<T> ok(T data){
        SysResult<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> SysResult<T> fail(){
        return SysResult.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> SysResult<T> fail(T data){
        SysResult<T> result = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

    public SysResult<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public SysResult<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
