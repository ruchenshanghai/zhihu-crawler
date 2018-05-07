package com.crawl.zhihu.dao;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.zhihu.entity.ParsedAnswer;
import com.crawl.zhihu.entity.ParsedQuestion;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ParsedEntityDAOInterface {
    boolean isExistEntity(Connection connection, String sql) throws SQLException;

    boolean isParsedUserExisted(Connection connection, String user_token);

    boolean insertParsedUser(Connection connection, ParsedUser parsedUser);

    String[] selectAllUserTokenOrderByWeight(Connection connection);

    boolean isParsedTopicExisted(Connection connection, int id);

    boolean insertParsedTopic(Connection connection, ParsedTopic parsedTopic);

    boolean insertOrUpdateParsedTopic(Connection connection, ParsedTopic parsedTopic);

    boolean isParsedQuestionExisted(Connection connection, int id);

    boolean insertParsedQuestion(Connection connection, ParsedQuestion parsedQuestion);


    boolean isParsedAnswerExisted(Connection connection, int id);

    boolean insertParsedAnswer(Connection connection, ParsedAnswer parsedAnswer);


}
