package com.crawl.zhihu.task;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.core.util.Config;
import com.crawl.core.util.Constants;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ParsedAnswer;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 图片答案task
 */
public class ParsedAnswerTask extends AbstractPageTask{
    private static Logger logger = LoggerFactory.getLogger(ParsedAnswerTask.class);
    private String userToken;
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();

    public ParsedAnswerTask(){

    }


    public ParsedAnswerTask(HttpRequestBase request, boolean proxyFlag, String userToken){
        super(request, proxyFlag);
        this.userToken = userToken;
    }

    @Override
    protected void retry() {
        zhiHuHttpClient.getAnswerPageThreadPool().execute(new ParsedAnswerTask(request, true, this.userToken));
    }

    @Override
    protected void handle(Page page) {
        DocumentContext dc = JsonPath.parse(page.getHtml());
        ParsedAnswer[] parsedAnswers = parseAnswers(dc);
        for(ParsedAnswer parsedAnswer : parsedAnswers){
            if (Config.dbEnable && parsedAnswer.getVoteup_count() > 0 && parsedAnswer.getComment_count() > 0) {
                Connection connection = getConnection();
                boolean insertRes = parsedEntityDAOInterface.insertParsedAnswer(connection, parsedAnswer);
                if (insertRes) {
                    logger.info("parsed_answer: " + parsedAnswer.toString() + " " + insertRes);
                }

                if(!parsedEntityDAOInterface.isParsedQuestionExisted(connection, parsedAnswer.getQuestion_id())) {
                    ZhiHuHttpClient.getInstance().startCrawlQuestion(parsedAnswer.getQuestion_id());
                }
            }
        }
        boolean isStart = dc.read("$.paging.is_start");
        if (isStart){
            Integer totals = dc.read("$.paging.totals");
            for (int j = 1; j < totals; j++) {
                String nextUrl = String.format(Constants.USER_ANSWER_URL, userToken, j * 20);
                HttpRequestBase request = new HttpGet(nextUrl);
                request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                zhiHuHttpClient.getAnswerPageThreadPool().execute(new ParsedAnswerTask(request, true, userToken));
            }
            String nextUserToken = ZhiHuHttpClient.getNextUserToken();
            if (nextUserToken != null) {
                String nextUrl = String.format(Constants.USER_ANSWER_URL, nextUserToken, 0);
                HttpRequestBase request = new HttpGet(nextUrl);
                request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                zhiHuHttpClient.getAnswerPageThreadPool().execute(new ParsedAnswerTask(request, true, nextUserToken));
            }
        }
    }

    private ParsedAnswer[] parseAnswers(DocumentContext dc){
        Integer answerCount = dc.read("$.data.length()");
        ParsedAnswer[] parsedAnswers = new ParsedAnswer[answerCount];
        for(int i = 0; i < answerCount; i++){
            parsedAnswers[i] = new ParsedAnswer();
            parsedAnswers[i].setId((Integer) dc.read("$.data[" + i + "].id"));
            parsedAnswers[i].setQuestion_id((Integer) dc.read("$.data[" + i + "].question.id"));
            parsedAnswers[i].setUser_id((String) dc.read("$.data[" + i + "].author.id"));
            parsedAnswers[i].setVoteup_count((Integer) dc.read("$.data[" + i + "].voteup_count"));
            parsedAnswers[i].setComment_count((Integer) dc.read("$.data[" + i + "].comment_count"));
            parsedAnswers[i].setContent((String) dc.read("$.data[" + i + "].content"));
        }
        return parsedAnswers;
    }

    /**
     * 每个thread维护一个Connection
     * @return
     */
    private Connection getConnection(){
        Thread currentThread = Thread.currentThread();
        Connection cn = null;
        if (!connectionMap.containsKey(currentThread)){
            cn = ConnectionManager.createConnection();
            connectionMap.put(currentThread, cn);
        }  else {
            cn = connectionMap.get(currentThread);
        }
        return cn;
    }
}
