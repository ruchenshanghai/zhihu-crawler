package com.crawl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractQuestion {
    public static void main(String[] args) {
        String quesStr = "https://www.zhihu.com/answers/258204941";
        Pattern pattern = Pattern.compile("https://www.zhihu.com/answers/(\\d+)");
        Matcher matcher = pattern.matcher(quesStr);
        if(matcher.find())
            System.out.println(matcher.group(1));
    }
}
