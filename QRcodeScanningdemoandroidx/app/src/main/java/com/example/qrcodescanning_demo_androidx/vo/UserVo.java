package com.example.qrcodescanning_demo_androidx.vo;

public class UserVo {

    private int  uesrid;
    private String username;
    private String password;

    public UserVo() {
    }

    public UserVo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUesrid() {
        return uesrid;
    }

    public void setUesrid(int uesrid) {
        this.uesrid = uesrid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
