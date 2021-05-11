package com.google.zxing.vo;


/**
 *封装接口返回给前端的统一格式
 * @param <T>   返回的data数据对象类型
 *
 */
public class ServerResponse<T> {

    private int status;  //状态：0 代表成功
    private T data;     //返回数据封装到data中
    private String msg;     //提示信息


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
