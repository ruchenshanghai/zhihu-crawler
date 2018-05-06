package com.crawl.zhihu.entity;

import java.util.Arrays;

public class ParsedUser {
    private String id;
    private String avatar_url;
    private String user_token;
    private String name;
    private String headline;
    // parse by badge
    private String identity;
    private int[] best_answerer;

    private int[] locations;
    private Education[] educations;
    private int[] employments;

    private int following_count;
    private int answer_count;
    private int question_count;
    private int voteup_count;
    private int thanked_count;
    private int follower_count;
    private int articles_count;

    private boolean is_advertiser;
    private boolean is_org;
    private boolean gender;


    public ParsedUser() {
    }

    @Override
    public String toString() {
        return "ParsedUser{" +
                "id='" + id + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", user_token='" + user_token + '\'' +
                ", name='" + name + '\'' +
                ", headline='" + headline + '\'' +
                ", identity='" + identity + '\'' +
                ", best_answerer=" + Arrays.toString(best_answerer) +
                ", locations=" + Arrays.toString(locations) +
                ", educations=" + Arrays.toString(educations) +
                ", employments=" + Arrays.toString(employments) +
                ", following_count=" + following_count +
                ", answer_count=" + answer_count +
                ", question_count=" + question_count +
                ", voteup_count=" + voteup_count +
                ", thanked_count=" + thanked_count +
                ", follower_count=" + follower_count +
                ", articles_count=" + articles_count +
                ", is_advertiser=" + is_advertiser +
                ", is_org=" + is_org +
                ", gender=" + gender +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int[] getBest_answerer() {
        return best_answerer;
    }

    public void setBest_answerer(int[] best_answerer) {
        this.best_answerer = best_answerer;
    }

    public int[] getLocations() {
        return locations;
    }

    public void setLocations(int[] locations) {
        this.locations = locations;
    }

    public Education[] getEducations() {
        return educations;
    }

    public void setEducations(Education[] educations) {
        this.educations = educations;
    }

    public int[] getEmployments() {
        return employments;
    }

    public void setEmployments(int[] employments) {
        this.employments = employments;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public int getVoteup_count() {
        return voteup_count;
    }

    public void setVoteup_count(int voteup_count) {
        this.voteup_count = voteup_count;
    }

    public int getThanked_count() {
        return thanked_count;
    }

    public void setThanked_count(int thanked_count) {
        this.thanked_count = thanked_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getArticles_count() {
        return articles_count;
    }

    public void setArticles_count(int articles_count) {
        this.articles_count = articles_count;
    }

    public boolean isIs_advertiser() {
        return is_advertiser;
    }

    public void setIs_advertiser(boolean is_advertiser) {
        this.is_advertiser = is_advertiser;
    }

    public boolean isIs_org() {
        return is_org;
    }

    public void setIs_org(boolean is_org) {
        this.is_org = is_org;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public static class Education {
        private int school_id;
        private int major_id;

        public Education() {
        }

        public Education(int school_id, int major_id) {
            this.school_id = school_id;
            this.major_id = major_id;
        }

        public int getSchool_id() {
            return school_id;
        }

        public void setSchool_id(int school_id) {
            this.school_id = school_id;
        }

        @Override
        public String toString() {
            return "Education{" +
                    "school_id=" + school_id +
                    ", major_id=" + major_id +
                    '}';
        }
    }
}
