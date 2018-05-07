package com.crawl.zhihu.dao;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.zhihu.entity.ParsedAnswer;
import com.crawl.zhihu.entity.ParsedQuestion;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;
import net.minidev.json.JSONArray;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class ParsedEntityDAOImpl implements ParsedEntityDAOInterface {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ParsedEntityDAOInterface.class);


//    public static void DBTablesInit() {
//        ResultSet rs = null;
//        Properties p = new Properties();
//        Connection cn = ConnectionManager.getConnection();
//        try {
//            //加载properties文件
//            p.load(ZhiHuDao1Imp.class.getResourceAsStream("/config.properties"));
//            rs = cn.getMetaData().getTables(null, null, "url", null);
//            Statement st = cn.createStatement();
//            //不存在url表
//            if(!rs.next()){
//                //创建url表
//                st.execute(p.getProperty("createUrlTable"));
//                logger.info("url表创建成功");
////                st.execute(p.getProperty("createUrlIndex"));
////                logger.info("url表索引创建成功");
//            }
//            else{
//                logger.info("url表已存在");
//            }
//            rs = cn.getMetaData().getTables(null, null, "user", null);
//            //不存在user表
//            if(!rs.next()){
//                //创建user表
//                st.execute(p.getProperty("createUserTable"));
//                logger.info("user表创建成功");
////                st.execute(p.getProperty("createUserIndex"));
////                logger.info("user表索引创建成功");
//            }
//            else{
//                logger.info("user表已存在");
//            }
//            rs.close();
//            st.close();
//            cn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean isExistEntity(Connection connection, String sql) throws SQLException {
        int count = 0;
        PreparedStatement pstmt;
        pstmt = connection.prepareStatement(sql);
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
    public boolean isParsedUserExisted(Connection connection, String user_token) {
        String containsParsedUserSql = "select count(*) from parsed_user where user_token='" + user_token + "'";
        try {
            if (isExistEntity(connection, containsParsedUserSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedUser(Connection connection, ParsedUser parsedUser) {
        if (isParsedUserExisted(connection, parsedUser.getUser_token())) {
            return false;
        }
        String columns = "id, avatar_url, user_token, name, headline, following_count, answer_count, question_count, voteup_count, thanked_count, " +
                "follower_count, articles_count, identity, locations, educations, best_answerer, employments, is_advertiser, is_org, gender, business_id";
        String values = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
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
            preparedStatement.setString(14, parsedUser.getLocations() == null ? null : Arrays.toString(parsedUser.getLocations()));
            preparedStatement.setString(15, parsedUser.getEducations() == null || parsedUser.getEducations().length == 0 ? null : JSONArray.toJSONString(Arrays.asList(parsedUser.getEducations())));
            preparedStatement.setString(16, parsedUser.getBest_answerer() == null || parsedUser.getBest_answerer().length == 0 ? null : Arrays.toString(parsedUser.getBest_answerer()));
            preparedStatement.setString(17, parsedUser.getEmployments() == null ? null : JSONArray.toJSONString(Arrays.asList(parsedUser.getEmployments())));
            preparedStatement.setBoolean(18, parsedUser.isIs_advertiser());
            preparedStatement.setBoolean(19, parsedUser.isIs_org());
            preparedStatement.setBoolean(20, parsedUser.isGender());
            preparedStatement.setInt(21, parsedUser.getBusiness_id());
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
    public boolean isParsedTopicExisted(Connection connection, int id) {
        String containsParsedTopicSql = "select count(*) from parsed_topic where id='" + id + "'";
        try {
            if (isExistEntity(connection, containsParsedTopicSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedTopic(Connection connection, ParsedTopic parsedTopic) {
        if (isParsedTopicExisted(connection, parsedTopic.getId())) {
            return false;
        }
        String columns = "id, name, avatar_url, introduction, location, school, major, company, job, business";
        String values = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        String sql = "insert into parsed_topic (" + columns + ") values (" + values + ")";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, parsedTopic.getId());
            preparedStatement.setString(2, parsedTopic.getName());
            preparedStatement.setString(3, parsedTopic.getAvatar_url());
            preparedStatement.setString(4, parsedTopic.getIntroduction());
            preparedStatement.setInt(5, parsedTopic.isIs_location() ? 1 : 0);
            preparedStatement.setInt(6, parsedTopic.isIs_school() ? 1 : 0);
            preparedStatement.setInt(7, parsedTopic.isIs_major() ? 1 : 0);
            preparedStatement.setInt(8, parsedTopic.isIs_company() ? 1 : 0);
            preparedStatement.setInt(9, parsedTopic.isIs_job() ? 1 : 0);
            preparedStatement.setInt(10, parsedTopic.isIs_business() ? 1 : 0);
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
    public boolean insertOrUpdateParsedTopic(Connection connection, ParsedTopic parsedTopic) {
        if (!isParsedTopicExisted(connection, parsedTopic.getId())) {
            return insertParsedTopic(connection, parsedTopic);
        }
        String preSql = "select location, school, major, company, job, business from parsed_topic where id=" + parsedTopic.getId();
        try {
            PreparedStatement pstmt = connection.prepareStatement(preSql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int location = rs.getInt("location") + (parsedTopic.isIs_location() ? 1 : 0);
                int school = rs.getInt("school") + (parsedTopic.isIs_school() ? 1 : 0);
                int major = rs.getInt("major") + (parsedTopic.isIs_major() ? 1 : 0);
                int company = rs.getInt("company") + (parsedTopic.isIs_company() ? 1 : 0);
                int job = rs.getInt("job") + (parsedTopic.isIs_job() ? 1 : 0);
                int business = rs.getInt("business") + (parsedTopic.isIs_business() ? 1 : 0);
                String tempSql = "update parsed_topic set location=?, school=?, major=?, company=?, job=?, business=? where id=?";
                PreparedStatement tempPstmt = connection.prepareStatement(tempSql);
                tempPstmt.setInt(1, location);
                tempPstmt.setInt(2, school);
                tempPstmt.setInt(3, major);
                tempPstmt.setInt(4, company);
                tempPstmt.setInt(5, job);
                tempPstmt.setInt(6, business);
                tempPstmt.setInt(7, parsedTopic.getId());
                tempPstmt.executeUpdate();
                tempPstmt.close();
                logger.info("update: " + parsedTopic.toString());
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean isParsedQuestionExisted(Connection connection, int id) {
        String containsParsedQuestionSql = "select count(*) from parsed_question where id=" + id;
        try {
            if (isExistEntity(connection, containsParsedQuestionSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedQuestion(Connection connection, ParsedQuestion parsedQuestion) {
        if (isParsedQuestionExisted(connection, parsedQuestion.getId())) {
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
    public boolean isParsedAnswerExisted(Connection connection, int id) {
        String containsParsedAnswerSql = "select count(*) from parsed_answer where id=" + id;
        try {
            if (isExistEntity(connection, containsParsedAnswerSql)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertParsedAnswer(Connection connection, ParsedAnswer parsedAnswer) {
        if (isParsedAnswerExisted(connection, parsedAnswer.getId())) {
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
