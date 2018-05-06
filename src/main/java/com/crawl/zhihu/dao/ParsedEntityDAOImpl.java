package com.crawl.zhihu.dao;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.zhihu.entity.ParsedAnswer;
import com.crawl.zhihu.entity.ParsedQuestion;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;
import net.minidev.json.JSONArray;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class ParsedEntityDAOImpl implements ParsedEntityDAOInterface {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ParsedEntityDAOInterface.class);
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
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, parsedUser.getId());
            preparedStatement.setString(2, parsedUser.getAvatar_url());
            preparedStatement.setString(3, parsedUser.getUser_token());
            preparedStatement.setString(4, parsedUser.getName());
            preparedStatement.setString(5, parsedUser.getHeadline());
            preparedStatement.setInt(6, parsedUser.getFollowing_count());
            preparedStatement.setInt(7, parsedUser.getAnswer_count());
            preparedStatement.setInt(8, parsedUser.getQuestion_count());
            preparedStatement.setInt(9, parsedUser.getVoteup_count());
            preparedStatement.setInt(10, parsedUser.getThanked_count());
            preparedStatement.setInt(11, parsedUser.getFollower_count());
            preparedStatement.setInt(12, parsedUser.getArticles_count());
            preparedStatement.setString(13, parsedUser.getIdentity());
            preparedStatement.setString(14, JSONArray.toJSONString(Arrays.asList(parsedUser.getLocations())));
            preparedStatement.setString(15, JSONArray.toJSONString(Arrays.asList(parsedUser.getEducations())));
            preparedStatement.setString(16, JSONArray.toJSONString(Arrays.asList(parsedUser.getBest_answerer())));
            preparedStatement.setString(17, JSONArray.toJSONString(Arrays.asList(parsedUser.getEmployments())));
            preparedStatement.setBoolean(18, parsedUser.isIs_advertiser());
            preparedStatement.setBoolean(19, parsedUser.isIs_org());
            preparedStatement.setBoolean(20, parsedUser.isGenderMale());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("parsed_user: " + parsedUser.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isParsedTopicExisted(String id) {
        String containsParsedTopicSql = "select count(*) from parsed_topic where id='" + id + "'";
        try {
            if (isExistEntity(containsParsedTopicSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedTopic(ParsedTopic parsedTopic) {
        if (isParsedTopicExisted(parsedTopic.getId())) {
            return false;
        }
        String columns = "id, name, avatar_url, introduction, is_location, is_school, is_major, is_company";
        String values = "?, ?, ?, ?, ?, ?, ?, ?";
        String sql = "insert into parsed_topic (" + columns + ") values (" + values + ")";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, parsedTopic.getId());
            preparedStatement.setString(2, parsedTopic.getName());
            preparedStatement.setString(3, parsedTopic.getAvatar_url());
            preparedStatement.setString(4, parsedTopic.getIntroduction());
            preparedStatement.setBoolean(5, parsedTopic.isIs_location());
            preparedStatement.setBoolean(6, parsedTopic.isIs_school());
            preparedStatement.setBoolean(7, parsedTopic.isIs_major());
            preparedStatement.setBoolean(8, parsedTopic.isIs_company());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("parsed_topic: " + parsedTopic.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isParsedQuestionExisted(int id) {
        String containsParsedQuestionSql = "select count(*) from parsed_question where id=" + id;
        try {
            if (isExistEntity(containsParsedQuestionSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedQuestion(ParsedQuestion parsedQuestion) {
        if (isParsedQuestionExisted(parsedQuestion.getId())) {
            return false;
        }
        String columns = "id, content, followers, viewers, comments, answers, keywords";
        String values = "?, ?, ?, ?, ?, ?, ?";
        String sql = "insert into parsed_question (" + columns + ") values (" + values + ")";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, parsedQuestion.getId());
            preparedStatement.setString(2, parsedQuestion.getContent());
            preparedStatement.setInt(3, parsedQuestion.getFollowers());
            preparedStatement.setInt(4, parsedQuestion.getViewers());
            preparedStatement.setInt(5, parsedQuestion.getComments());
            preparedStatement.setInt(6, parsedQuestion.getAnswers());
            preparedStatement.setString(7, JSONArray.toJSONString(Arrays.asList(parsedQuestion.getKeywords())));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("parsed_question: " + parsedQuestion.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isParsedAnswerExisted(int id) {
        String containsParsedAnswerSql = "select count(*) from parsed_answer where id=" + id;
        try {
            if (isExistEntity(containsParsedAnswerSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedAnswer(ParsedAnswer parsedAnswer) {
        if (isParsedAnswerExisted(parsedAnswer.getId())) {
            return false;
        }
        String columns = "id, question_id, user_id, voteup_count, comment_count, content";
        String values = "?, ?, ?, ?, ?, ?";
        String sql = "insert parsed_answer (" + columns + ") values (" + values + ")";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, parsedAnswer.getId());
            preparedStatement.setInt(2, parsedAnswer.getQuestion_id());
            preparedStatement.setString(3, parsedAnswer.getUser_id());
            preparedStatement.setInt(4, parsedAnswer.getVoteup_count());
            preparedStatement.setInt(5, parsedAnswer.getComment_count());
            preparedStatement.setString(6, parsedAnswer.getContent());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("parsed_answer: " + parsedAnswer.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
