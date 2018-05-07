package com.crawl.zhihu.task;

import com.crawl.core.dao.ConnectionManager;
import com.crawl.core.util.Config;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ParsedQuestion;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedQuestionTask extends AbstractPageTask {
    private static Logger logger = LoggerFactory.getLogger(ParsedAnswerTask.class);
    private int id;

    public ParsedQuestionTask() {
    }

    public ParsedQuestionTask(HttpRequestBase request, boolean proxyFlag, int questionID) {
        super(request, proxyFlag);
        this.id = questionID;
    }

    @Override
    protected void retry() {
        System.out.println("retry question task");
    }

    @Override
    protected void handle(Page page) {
        Document dc = Jsoup.parse(page.getHtml());
        ParsedQuestion parsedQuestion = parseQuestion(dc);
        if (Config.dbEnable && parsedQuestion != null) {
            java.sql.Connection connection = ConnectionManager.getConnection();
            if (parsedEntityDAOInterface.isParsedQuestionExisted(connection, parsedQuestion.getId())) {
                logger.info("parsed_question existed");
            } else {
                boolean insertRes = parsedEntityDAOInterface.insertParsedQuestion(connection, parsedQuestion);
                logger.info("parsed_question: " + parsedQuestion.toString() + " " + insertRes);
            }
        }
    }

    private ParsedQuestion parseQuestion(Document dc) {
        try {
            ParsedQuestion parsedQuestion = new ParsedQuestion();
            String questionContent;
            String[] keywordArray;
            int followers;
            int viewers;
            int comments = 0;
            int answers = 0;
            Elements elContent = dc.select("[itemProp=name]");
            questionContent = elContent.first().attr("content");
            System.out.println(questionContent);

            Elements elKeyword = dc.select("[itemProp=keywords]");
            String questionKeyword = elKeyword.first().attr("content");
            System.out.println(questionKeyword);
            keywordArray = questionKeyword.split(",");

            Elements elNums = dc.select(".NumberBoard-itemValue");
            followers = Integer.parseInt(elNums.first().attr("title"));
            viewers = Integer.parseInt(elNums.get(1).attr("title"));


            TextNode elComment = (TextNode) dc.select(".Zi--Comment").first().parent().parent().childNode(1);
            Pattern commentPattern = Pattern.compile("(\\d+)\\s条评论");
            Matcher commentMatcher = commentPattern.matcher(elComment.text());
            if(commentMatcher.find()) {
                System.out.println(commentMatcher.group(1));
                comments = Integer.parseInt(commentMatcher.group(1));
            }

            Element elAnswer = (dc.select(".List-headerText")).first().child(0);
            String answerStr = elAnswer.ownText();
            Pattern answerPattern = Pattern.compile("(\\S+)\\s个回答");
            Matcher answerMatcher = answerPattern.matcher(answerStr);
            if (answerMatcher.find()) {
                answerStr = answerMatcher.group(1);
                try {
                    answers = new DecimalFormat().parse(answerStr).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            parsedQuestion.setId(this.id);
            parsedQuestion.setContent(questionContent);
            parsedQuestion.setFollowers(followers);
            parsedQuestion.setViewers(viewers);
            parsedQuestion.setComments(comments);
            parsedQuestion.setAnswers(answers);
            parsedQuestion.setKeywords(keywordArray);

            return parsedQuestion;
        } catch (Exception e) {
            return null;
        }
    }
}
