package com.example.howmany;

import android.graphics.drawable.Drawable;

public class PeopleList {
    private Integer id;
    private String name; // 분반
    private String major; // 학수번호
    private String enter_time; // 수업이름

    public PeopleList(){}

    public void setId(Integer id) { this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setEnter_time(String enter_time) {
        this.enter_time = enter_time;
    }

    public PeopleList(Integer id ,String name, String major, String enter_time){
        this.id = id;
        this.name = name;
        this.major = major;
        this.enter_time = enter_time;
    }
    public Integer getId() { return id; }
    public String getName() {return name;}
    public String getMajor() { return major;
    }
    public String getEnter_time() {
        return enter_time;
    }


}
