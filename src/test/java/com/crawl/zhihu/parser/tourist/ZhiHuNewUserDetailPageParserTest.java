package com.crawl.zhihu.parser.tourist;

import com.crawl.core.parser.DetailPageParser;
import com.crawl.zhihu.parser.ZhiHuNewUserDetailPageParser;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import com.crawl.core.util.HttpClientUtil;
import org.junit.Test;

import java.io.IOException;


public class ZhiHuNewUserDetailPageParserTest {
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
//        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
//        String url = "https://www.zhihu.com/people/excited-vczh/following";
        Page page = new Page();
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        try {
            page.setHtml(HttpClientUtil.getWebPage(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setUrl(url);
        DetailPageParser parser = ZhiHuNewUserDetailPageParser.getInstance();
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 1; i++){
            User user = parser.parseDetailPage(page);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cost time:" + (endTime - startTime) + "ms");
    }
}