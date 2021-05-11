package com.example.demo.common;

public enum ResponseCode {
    USERNAME_NOT_EMPTY(1,"用户名不能为空"),
    PASSWORD_NOT_EMPTY(2,"密码不能为空"),
    USERNAME_NOT_EXIST(3,"用户名不存在"),
    USERNAME_EXIST(4,"用户名存在"),
    PASSWORD_ERROR(5,"密码错误"),
    PARAMTER_NOT_EMPTY(6,"参数不能为空"),
    REGISTER_FAIL(7,"注册失败"),
    QRCODELOGIN_FAIL(8,"扫码登录失败")
    ;

    private int code;
    private String msg;

    ResponseCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
