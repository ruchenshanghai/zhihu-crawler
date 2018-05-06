package com.crawl.zhihu.parser;

import com.crawl.core.parser.ListPageParser;
import com.crawl.zhihu.entity.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录模式 “我关注的人”列表页面解析器
 * 2016-12-28起 不在使用
 */
@Deprecated
public class ZhiHuUserFollowingListPageParser implements ListPageParser {
    private static ZhiHuUserFollowingListPageParser zhiHuUserFollowingListPageParser;
    public static ZhiHuUserFollowingListPageParser getInstance(){
        if(zhiHuUserFollowingListPageParser == null){
            zhiHuUserFollowingListPageParser = new ZhiHuUserFollowingListPageParser();
        }
        return zhiHuUserFollowingListPageParser;
    }
    @Override
    public List<String> parseListPage(Page page) {
        List<String> list = new ArrayList<String>(20);
        Document doc = Jsoup.parse(page.getHtml());
        Elements es = doc.select(".zm-list-content-medium .zm-list-content-title a");
        String u = null;
        for(Element temp:es){
            u = (String) (temp.attr("href") + "/following");
            list.add(u);
        }
        return list;
    }
}
