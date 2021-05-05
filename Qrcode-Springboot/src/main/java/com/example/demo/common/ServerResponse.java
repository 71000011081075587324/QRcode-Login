package com.example.demo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 *封装接口返回给前端的统一格式
 * @param <T>   返回的data数据对象类型
 *
 */
// 确保序列化JSON时，如果是null对象，其key也会消失。
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> {

    private int status;  //状态：0 代表成功
    private T data;     //返回数据封装到data中
    private String msg;     //提示信息

    private ServerResponse() {
    }


    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    //    使之不在JSON序列化结果当中
    @JsonIgnore
    // 可以快速进行成功与否的条件判断
    public boolean isSuccess() {
        return this.status == 0;
    }

    public static ServerResponse createServerResponseBySuccess(){
        return new ServerResponse(0);
    }

    public static <T> ServerResponse createServerResponseBySuccess(T data){
        return new ServerResponse(0,data);
    }

    public static <T> ServerResponse createServerResponseBySuccess(T data,String msg){
        return new ServerResponse(0,data,msg);
    }

    public static ServerResponse createServerResponseByFail(int status){
        return new ServerResponse(status);
    }

    public static ServerResponse createServerResponseByFail(int status,String msg){
        return new ServerResponse(status,null,msg);
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
