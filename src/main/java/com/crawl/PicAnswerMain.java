package com.crawl;


import com.crawl.zhihu.ZhiHuHttpClient;

public class PicAnswerMain {
    public static void main(String[] args){
        ZhiHuHttpClient.getInstance().startCrawlAnswerPic("tang-qi-55-37");
    }
}
