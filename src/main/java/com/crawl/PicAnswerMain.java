package com.crawl;


import com.crawl.zhihu.ZhiHuHttpClient;

public class PicAnswerMain {
    public static void main(String[] args){
//        ZhiHuHttpClient.getInstance().startCrawlAnswerByUserToken("tang-qi-55-37");
//        ZhiHuHttpClient.getInstance().startCrawlQuestion(53369195);
        ZhiHuHttpClient.getInstance().startCrawlAllAnswer();
    }
}
