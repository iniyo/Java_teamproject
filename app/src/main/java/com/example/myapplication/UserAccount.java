package com.example.myapplication;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    private String idToken; // Firebase Uid(고유토큰정보)
    private String emailI; // 이메일아이디
    private String password; //비밀번호
    private int check_count; // 체크

    public UserAccount(){ }     //빈생성자 안만들면 db오류 발생함

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailI() {
        return emailI;
    }

    public void setEmailI(String emailI) {
        this.emailI = emailI;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public int getCheck_count() {
        return check_count;
    }

    public void setCheck_count(int check_count) {
        this.check_count = check_count;
    }
}
