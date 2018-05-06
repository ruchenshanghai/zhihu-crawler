package com.crawl.zhihu.entity;

public class ParsedAnswer {
    private int id;
    private int question_id;
    private String user_id;
    private int voteup_count;
    private int comment_count;
    private String content;

    public ParsedAnswer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getVoteup_count() {
        return voteup_count;
    }

    public void setVoteup_count(int voteup_count) {
        this.voteup_count = voteup_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
