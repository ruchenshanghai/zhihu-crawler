package com.crawl.zhihu.task;


import com.crawl.core.dao.ConnectionManager;
import com.crawl.core.parser.ListPageParser;
import com.crawl.core.util.Config;
import com.crawl.core.util.SimpleInvocationHandler;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;
import com.crawl.zhihu.parser.ParsedUserPageParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.crawl.core.util.Constants.USER_FOLLOWEES_URL;
import static com.crawl.zhihu.ZhiHuHttpClient.parseUserCount;

/**
 * 知乎用户列表详情页task
 */
public class ParsedUserListPageTask extends AbstractPageTask{
    private static Logger logger = LoggerFactory.getLogger(ParsedUserListPageTask.class);
    private static ListPageParser proxyUserListPageParser;
    /**
     * Thread-数据库连接
     */
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();
    static {
        proxyUserListPageParser = getProxyUserListPageParser();
    }


    public ParsedUserListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }

    /**
     * 代理类
     * @return
     */
    private static ListPageParser getProxyUserListPageParser(){
        ListPageParser userListPageParser = ParsedUserPageParser.getInstance();
        InvocationHandler invocationHandler = new SimpleInvocationHandler(userListPageParser);
        ListPageParser proxyUserListPageParser = (ListPageParser) Proxy.newProxyInstance(userListPageParser.getClass().getClassLoader(),
                userListPageParser.getClass().getInterfaces(), invocationHandler);
        return proxyUserListPageParser;
    }

    @Override
    protected void retry() {
        zhiHuHttpClient.getDetailListPageThreadPool().execute(new ParsedUserListPageTask(request, Config.isProxy));
    }

    @Override
    protected void handle(Page page) {
        if(!page.getHtml().startsWith("{\"paging\"")){
            //代理异常，未能正确返回目标请求数据，丢弃
            currentProxy = null;
            return;
        }
        Map<Integer, ParsedTopic> parsedTopicMap = new LinkedHashMap<>();
        List<ParsedUser> userList = proxyUserListPageParser.parsedPage(page, parsedTopicMap);

        for (ParsedTopic pt: parsedTopicMap.values()) {
            if (Config.dbEnable) {
                Connection cn = getConnection();
                parsedEntityDAOInterface.insertOrUpdateParsedTopic(cn, pt);
            }
        }
        for (ParsedUser pu: userList) {
            if (Config.dbEnable) {
                Connection cn = getConnection();
                if (parsedEntityDAOInterface.insertParsedUser(cn, pu)){
                    parseUserCount.incrementAndGet();
                }
                for (int j = 0; j < pu.getFollowing_count() / 20; j++) {
                    String nextUrl = String.format(USER_FOLLOWEES_URL, pu.getUser_token(), j * 20);
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new ParsedUserListPageTask(request, true));
                }
            }
        }
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

    public static Map<Thread, Connection> getConnectionMap() {
        return connectionMap;
    }

}
