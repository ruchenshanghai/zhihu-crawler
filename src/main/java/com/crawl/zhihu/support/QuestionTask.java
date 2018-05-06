package com.crawl.zhihu.support;

import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.task.AbstractPageTask;
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

public class QuestionTask extends AbstractPageTask {
    private static Logger logger = LoggerFactory.getLogger(PicAnswerTask.class);
    private int questionID;

    public QuestionTask() {
    }

    public QuestionTask(HttpRequestBase request, boolean proxyFlag, int questionID) {
        super(request, proxyFlag);
        this.questionID = questionID;
    }

    @Override
    protected void retry() {
        System.out.println("retry question task");
    }

    @Override
    protected void handle(Page page) {
        String questionContent;
        String[] keywordArray;
        int followers;
        int viewers;
        int comments = 0;
        int answers = 0;

        Document dc = Jsoup.parse(page.getHtml());
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



        System.out.println(comments);
//        Elements el_child = el.select("[itemProp=name]");
//        if (el_child != null) {
//            System.out.println(el.toString());
//            for(Element sub_el: el) {
//                if (sub_el.attr("itemprop").equals("name")) {
//                    System.out.println(sub_el.attr("content"));
//                } else {
//                    if (sub_el.attr("itemprop").equals("keywords")) {
//                        System.out.println(sub_el.attr("content"));
//                    }
//                }
//            }
//        }

    }
}
