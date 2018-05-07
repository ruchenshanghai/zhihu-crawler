package com.crawl.core.parser;

import com.crawl.zhihu.entity.Page;

import java.util.List;
import java.util.Map;

public interface ListPageParser<K, V> extends Parser {
    List parsedPage(Page page, Map<K, V> map);
}
