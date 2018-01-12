package com.zhibaowang.model;

public class User {

    private String way;
    //用户名,非昵称,可以是手机号
    private String username;
    //用户密码
    private String password;
    //验证码
    private String authcode;
    //本消息报产生时的时间戳(毫秒)
    private long timestamp;
    //额外信息
    private String msg;
    //是否为已认证用户
    private boolean isAuth;
    //昵称
    private String nickName;
    //token
    private String token;
    //头像的url
    private String icon;
    //性别
    private String gender;
    //用户唯一识别号,服务器端要存这个
    private String userId;
    //是否处于登录在线状态
    private boolean isLogin = false;
    //用户用来提交请求时的验证信息
    private String requestCode;
    public User(){}
    //请求结果
    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public String getWay() {
        return "GET".equals(way) ? "GET" : "POST";
    }

    private void setWay(String way) {
        this.way = way;
    }


    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestCode() {
        return requestCode;
    }
}
