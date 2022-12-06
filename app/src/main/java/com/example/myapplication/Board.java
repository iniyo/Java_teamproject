package com.example.myapplication;

public class Board { //Board가 테이블 명

    // 소문자나 대문자로만 구성할 것.
    private String board_name;
    //인원수
    private String people_number;
    //날짜
    private String date;
    //시간
    private String time;
    //같이
    private String together;
    //혼자
    private String solo;
    //배달 내용
    private String delevery_info;
    //제목
    private String title_info;
    //유저 UID
    private String uid;
    // test 용 문서 아이디 저장
    private String id;
    // 카운트 값
    private String count;

    private String showpeople;
    //uid체크용
    private String checkuid;

    public String getCheckuid() {
        return checkuid;
    }

    public void setCheckuid(String checkuid) {
        this.checkuid = checkuid;
    }

    // 생성자
    public Board(){}
    public Board(String board_name, String people_number, String date, String time, String together, String solo, String delevery_info, String title_info, String uid, String id){
        this.board_name = board_name;
        this.people_number = people_number;
        this.date = date;
        this.time = time;
        this.together = together;
        this.solo = solo;
        this.delevery_info = delevery_info;
        this.title_info = title_info;
        this.uid = uid;
        this.id = id;
    }
    //getter and setter

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public String getPeople_number() {
        return people_number;
    }

    public void setPeople_number(String people_number) {
        this.people_number = people_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTogether() {
        return together;
    }

    public void setTogether(String together) {
        this.together = together;
    }

    public String getSolo() {
        return solo;
    }

    public void setSolo(String solo) {
        this.solo = solo;
    }

    public String getDelevery_info() {
        return delevery_info;
    }

    public void setDelevery_info(String delevery_info) {
        this.delevery_info = delevery_info;
    }

    public String getTitle_info() { return title_info; }

    public void setTitle_info(String title_info) { this.title_info = title_info; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getCount() { return count; }

    public void setCount(String count) { this.count = count; }

    public String getShowpeople() { return showpeople; }

    public void setShowpeople(String showpeople) { this.showpeople = showpeople; }
}
