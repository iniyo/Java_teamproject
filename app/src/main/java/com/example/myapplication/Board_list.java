package com.example.myapplication;

public class Board_list {
    private String pl2_peopleNum; // 총 인원
    private String pl2_time; // 배달시킬시간
    private String pl2_title; // 제목

    public String getPl2_peopleNum() {
        return pl2_peopleNum;
    }

    public void setPl2_peopleNum(String pl2_peopleNum) {
        this.pl2_peopleNum = pl2_peopleNum;
    }

    public String getPl2_time() {
        return pl2_time;
    }

    public void setPl2_time(String pl2_tim) {
        this.pl2_time = pl2_tim;
    }

    public String getPl2_title() {
        return pl2_title;
    }

    public void setPl2_title(String pl2_title) {
        this.pl2_title = pl2_title;
    }

    public Board_list(String pl2_peopleNum, String pl2_time, String pl2_title) {
        this.pl2_peopleNum = pl2_peopleNum;
        this.pl2_time = pl2_time;
        this.pl2_title = pl2_title;
    }
}
