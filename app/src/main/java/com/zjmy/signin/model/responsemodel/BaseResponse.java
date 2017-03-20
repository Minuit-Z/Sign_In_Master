package com.zjmy.signin.model.responsemodel;

/**
 * @Description:网络请求的json数组的基类
 * @authors: utopia
 * @Create time: 16-12-22 上午10:21
 * @Update time: 16-12-22 上午10:21
*/
public class BaseResponse<T> {
    public int code;//状态码
    public String message;//提示信息
    public T data; //数据
}
