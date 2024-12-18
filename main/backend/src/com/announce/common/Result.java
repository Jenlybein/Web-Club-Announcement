package com.announce.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 全局统一返回结果类
 * 用于标准化 API 接口的返回结果，包含返回码、返回消息和返回数据
 */
@Setter
@Getter
public class Result<T> {
    private Integer code;   // 返回码
    private String message; // 返回消息
    private T data;         // 返回数据
    public Result(){}

    /**
     * 根据数据构建一个返回结果对象
     * @param data 返回的数据
     */
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    /**
     * 根据数据、状态码和消息构建一个返回结果对象
     * @param body 返回的数据
     * @param code 返回的状态码
     * @param message 返回的消息
     */
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 根据数据和枚举 ResultCodeEnum 创建一个返回结果对象
     * @param body 返回的数据
     * @param resultCodeEnum 包含返回码和消息的枚举
     */
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 操作成功的返回结果，使用默认的成功码和消息
     * @param data 返回的数据
     * @return 成功的 Result 对象
     */
    public static<T> Result<T> sucess(T data){
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}