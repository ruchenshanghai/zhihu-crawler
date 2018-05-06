package com.crawl.zhihu.entity;

public class ParsedQuestion {
    private int id;
    private String content;
    private int followers;
    private int viewers;
    private int answers;
    private int comments;
    private String[] keywords;

    public ParsedQuestion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
}
