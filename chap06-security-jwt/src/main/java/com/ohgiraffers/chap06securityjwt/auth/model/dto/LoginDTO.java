package com.ohgiraffers.chap06securityjwt.auth.model.dto;

public class LoginDTO {
// 로그인 시 사용할 DTO 클래스
    private String id;
    private String pass;

    public LoginDTO() {
    }

    public LoginDTO(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "id='" + id + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
