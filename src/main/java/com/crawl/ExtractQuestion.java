package com.crawl;


import com.crawl.zhihu.entity.ParsedUser;
import net.minidev.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ExtractQuestion {
    public static void main(String[] args) {
//        String quesStr = "https://www.zhihu.com/answers/258204941";
//        Pattern pattern = Pattern.compile("https://www.zhihu.com/answers/(\\d+)");
//        Matcher matcher = pattern.matcher(quesStr);
//        if(matcher.find())
//            System.out.println(matcher.group(1));

//        String[] test = new String[]{"test1", "test2"};
//        ParsedUser.Education test2 = new ParsedUser.Education();
//        test2.setSchool_id(123);
//        test2.setMajor_id(234);
//        ParsedUser.Education[] test3 = new ParsedUser.Education[2];
//        test3[0] = new ParsedUser.Education(123, 234);
//        test3[1] = test2;
//        String jsonArray = JSONArray.toJSONString(Arrays.asList(test3));
//        System.out.println(jsonArray);

//        System.out.println(JSONArray.toJSONString(Arrays.asList(null)));

//        ParsedUser.Education test = new ParsedUser.Education();
//        System.out.println(test.getMajor_id());
//        System.out.println(test.getSchool_id());
//        String testCode = unicode2String("\\u7f8e\\u98df");
//        System.out.println(testCode);
//        try {
//            String code1 = new String(testCode.getBytes(), "utf-8");
//            System.out.println(code1);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }
}
