package com.crawl.zhihu.entity;

public class ParsedTopic {
    //唯一标示id
    private int id;
    //唯一标示图片地址
    private String avatar_url;
    //标识名称
    private String name;
    //话题简介
    private String introduction;
    //是否为地址
    private boolean is_location = false;
    //是否为学校
    private boolean is_school = false;
    //是否为专业
    private boolean is_major = false;
    //是否为职业
    private boolean is_job = false;
    //是否为公司
    private boolean is_company = false;
    //是否为行业
    private boolean is_business = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean isIs_location() {
        return is_location;
    }

    public void setIs_location(boolean is_location) {
        this.is_location = is_location;
    }

    public boolean isIs_school() {
        return is_school;
    }

    public void setIs_school(boolean is_school) {
        this.is_school = is_school;
    }

    public boolean isIs_major() {
        return is_major;
    }

    public void setIs_major(boolean is_major) {
        this.is_major = is_major;
    }

    public boolean isIs_company() {
        return is_company;
    }

    public void setIs_company(boolean is_company) {
        this.is_company = is_company;
    }

    public boolean isIs_job() {
        return is_job;
    }

    public void setIs_job(boolean is_job) {
        this.is_job = is_job;
    }

    public boolean isIs_business() {
        return is_business;
    }

    public void setIs_business(boolean is_business) {
        this.is_business = is_business;
    }

    @Override
    public String toString() {
        return "ParsedTopic{" +
                "id=" + id +
                ", avatar_url='" + avatar_url + '\'' +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", is_location=" + is_location +
                ", is_school=" + is_school +
                ", is_major=" + is_major +
                ", is_job=" + is_job +
                ", is_company=" + is_company +
                ", is_business=" + is_business +
                '}';
    }
}
