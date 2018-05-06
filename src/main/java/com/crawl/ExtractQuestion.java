package com.crawl;


import com.crawl.zhihu.entity.ParsedUser;
import net.minidev.json.JSONArray;

import java.util.Arrays;

public class ExtractQuestion {
    public static void main(String[] args) {
//        String quesStr = "https://www.zhihu.com/answers/258204941";
//        Pattern pattern = Pattern.compile("https://www.zhihu.com/answers/(\\d+)");
//        Matcher matcher = pattern.matcher(quesStr);
//        if(matcher.find())
//            System.out.println(matcher.group(1));
        String[] test = new String[]{"test1", "test2"};
        ParsedUser.Education test2 = new ParsedUser.Education();
        test2.setSchool_id(123);
        test2.setMajor_id(234);
        ParsedUser.Education[] test3 = new ParsedUser.Education[2];
        test3[0] = new ParsedUser.Education(123, 234);
        test3[1] = test2;
        String jsonArray = JSONArray.toJSONString(Arrays.asList(test3));
        System.out.println(jsonArray);
    }
}
