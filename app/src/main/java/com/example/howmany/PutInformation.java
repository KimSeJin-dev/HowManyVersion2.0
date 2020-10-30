package com.example.howmany;

import com.google.gson.annotations.SerializedName;

public class PutInformation {

    private String name; // 분반
    private String major; // 학수번호
    private String phone_num;// 연락처

    @SerializedName("body")
    private String text;

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getText() {
        return text;
    }
}
