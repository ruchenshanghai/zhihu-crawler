package com.crawl.zhihu.dao;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.zhihu.entity.ParsedAnswer;
import com.crawl.zhihu.entity.ParsedQuestion;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParsedEntityDAOImpl implements ParsedEntityDAOInterface {
    @Override
    public boolean isExistEntity(String sql) throws SQLException {
        int count = 0;
        PreparedStatement pstmt;
        pstmt = ConnectionManager.getConnection().prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            count = rs.getInt("count(*)");
        }
        rs.close();
        pstmt.close();
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isParsedUserExisted(String user_token) {
        String containsParsedUserSql = "select count(*) from parsed_user where user_token='" + user_token + "'";
        try {
            if (isExistEntity(containsParsedUserSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedUser(ParsedUser parsedUser) {
        if (isParsedUserExisted(parsedUser.getUser_token())) {
            return false;
        }
        String columns = "id, avatar_url, user_token, name, headline, following_count, answer_count, question_count, voteup_count, thanked_count, " +
                "follower_count, articles_count, identity, locations, educations, best_answerer, employments, is_advertiser, is_org, gender";
        String values = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        String sql = "insert into parsed_user (" + columns + ") values (" + values + ")";
        return false;
    }

    @Override
    public boolean isParsedTopicExisted(String id) {
        return false;
    }

    @Override
    public boolean insertParsedTopic(ParsedTopic parsedTopic) {
        return false;
    }

    @Override
    public boolean isParsedQuestionExisted(int id) {
        return false;
    }

    @Override
    public boolean insertParsedQuestion(ParsedQuestion parsedQuestion) {
        return false;
    }

    @Override
    public boolean isParsedAnswerExisted(int id) {
        return false;
    }

    @Override
    public boolean insertParsedAnswer(ParsedAnswer parsedAnswer) {
        return false;
    }
}
