package com.crawl.zhihu.dao;

import com.crawl.zhihu.entity.ParsedAnswer;
import com.crawl.zhihu.entity.ParsedQuestion;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;

import java.sql.Connection;
import java.sql.SQLException;

public interface ParsedEntityDAOInterface {
    boolean isExistEntity(String sql) throws SQLException;

    boolean isParsedUserExisted(String user_token);

    boolean insertParsedUser(ParsedUser parsedUser);


    boolean isParsedTopicExisted(String id);

    boolean insertParsedTopic(ParsedTopic parsedTopic);


    boolean isParsedQuestionExisted(int id);

    boolean insertParsedQuestion(ParsedQuestion parsedQuestion);


    boolean isParsedAnswerExisted(int id);

    boolean insertParsedAnswer(ParsedAnswer parsedAnswer);

}
