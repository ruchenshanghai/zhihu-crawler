package com.crawl.zhihu.parser;


import com.crawl.core.parser.ListPageParser;
import com.crawl.core.util.Constants;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.ParsedTopic;
import com.crawl.zhihu.entity.ParsedUser;
import com.crawl.zhihu.entity.User;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户详情列表页
 */
public class ZhiHuUserListPageParser implements ListPageParser {
    private static ZhiHuUserListPageParser instance;

    public static ZhiHuUserListPageParser getInstance() {
        if (instance == null) {
            synchronized (ZhiHuHttpClient.class) {
                if (instance == null) {
                    instance = new ZhiHuUserListPageParser();
                }
            }
        }
        return instance;
    }

    public List<ParsedUser> parsedPage(Page page) {
        List<ParsedUser> parsedUserList = new ArrayList<>();
        Map<Integer, ParsedTopic> parsedTopicMap = new LinkedHashMap<>();
        String baseJsonPath = "$.data.length()";
        DocumentContext dc = JsonPath.parse(page.getHtml());
        Integer userCount = dc.read(baseJsonPath);
        for (int i = 0; i < userCount; i++) {
            String tempBaseJsonPath = "$.data[" + i + "]";
            ParsedUser tempParsedUser = new ParsedUser();
            tempParsedUser.setId((String) dc.read(tempBaseJsonPath + ".id"));
            tempParsedUser.setAvatar_url((String) dc.read(tempBaseJsonPath + ".avatar_url"));
            tempParsedUser.setUser_token((String) dc.read(tempBaseJsonPath + ".url_token"));
            tempParsedUser.setName((String) dc.read(tempBaseJsonPath + ".name"));
            tempParsedUser.setHeadline((String) dc.read(tempBaseJsonPath + ".headline"));
            tempParsedUser.setFollowing_count((Integer) dc.read(tempBaseJsonPath + ".following_count"));
            tempParsedUser.setAnswer_count((Integer) dc.read(tempBaseJsonPath + ".answer_count"));
            tempParsedUser.setQuestion_count((Integer) dc.read(tempBaseJsonPath + ".question_count"));
            tempParsedUser.setVoteup_count((Integer) dc.read(tempBaseJsonPath + ".voteup_count"));
            tempParsedUser.setThanked_count((Integer) dc.read(tempBaseJsonPath + ".thanked_count"));
            tempParsedUser.setFollower_count((Integer) dc.read(tempBaseJsonPath + ".follower_count"));
            tempParsedUser.setArticles_count((Integer) dc.read(tempBaseJsonPath + ".articles_count"));
            try {
                ParsedTopic tempBusiness = new ParsedTopic();
                tempBusiness.setId(Integer.parseInt((String) dc.read(tempBaseJsonPath + ".business.id")));
                tempBusiness.setName((String) dc.read(tempBaseJsonPath + ".business.name"));
                tempBusiness.setAvatar_url((String) dc.read(tempBaseJsonPath + ".business.avatar_url"));
                tempBusiness.setIntroduction((String) dc.read(tempBaseJsonPath + ".business.introduction"));
                tempBusiness.setIs_business(true);
                if (!parsedTopicMap.containsKey(tempBusiness.getId())) {
                    parsedTopicMap.put(tempBusiness.getId(), tempBusiness);
                } else {
                    parsedTopicMap.get(tempBusiness.getId()).setIs_business(true);
                }
                tempParsedUser.setBusiness_id(tempBusiness.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tempParsedUser.getFollowing_count() < Constants.MINIMUM_FOLLOWING_COUNT || tempParsedUser.getAnswer_count() < Constants.MINIMUM_ANSWER_COUNT || tempParsedUser.getQuestion_count() < Constants.MINIMUM_QUESTION_COUNT || tempParsedUser.getVoteup_count() < Constants.MINIMUM_VOTE_UP_COUNT || tempParsedUser.getThanked_count() < Constants.MINIMUM_THANKED_COUNT || tempParsedUser.getFollower_count() < Constants.MINIMUM_FOLLOWER_COUNT || tempParsedUser.getArticles_count() < Constants.MINIMUM_ARTICLE_COUNT) {
                continue;
            }
            ArrayList<ParsedUser.Education> tempEducations = new ArrayList<>();
            ArrayList<String> tempEmployments = new ArrayList<>();

            // badge -> identity, best_answerer
            int badgeLength = (Integer) dc.read(tempBaseJsonPath + ".badge.length()");
            if (badgeLength > 0) {
                String tempIdentity = "";
                ArrayList<Integer> tempBestAnswers = new ArrayList<>();
                for (int j = 0; j < badgeLength; j++) {
                    String tempBadgeBase = tempBaseJsonPath + ".bagde[" + j + "]";
                    if (dc.read(tempBadgeBase + ".type").equals("best_answerer")) {
                        int tempTopicCount = (Integer) dc.read(tempBadgeBase + ".topics.length()");
                        for (int topicIndex = 0; topicIndex < tempTopicCount; topicIndex++) {
                            String tempTopicBase = tempBadgeBase + ".topics[" + topicIndex + "]";
                            ParsedTopic tempTopic = new ParsedTopic();
                            tempTopic.setId(Integer.parseInt((String) dc.read(tempTopicBase + ".id")));
                            tempTopic.setName((String) dc.read(tempTopicBase + ".name"));
                            tempTopic.setAvatar_url((String) dc.read(tempTopicBase + ".avatar_url"));
                            tempTopic.setIntroduction((String) dc.read(tempTopicBase + ".introduction"));
                            tempBestAnswers.add(tempTopic.getId());
                            if (!parsedTopicMap.containsKey(tempTopic.getId())) {
                                parsedTopicMap.put(tempTopic.getId(), tempTopic);
                            }
                        }
                    } else if (dc.read(tempBadgeBase + ".type").equals("identity")) {
                        tempIdentity = dc.read(tempBadgeBase + ".description");
                    }
                }
                tempParsedUser.setIdentity(tempIdentity);
                int[] temp_best_answers = new int[tempBestAnswers.size()];
                for (int j = 0; j < tempBestAnswers.size(); j++) {
                    temp_best_answers[j] = tempBestAnswers.get(j);
                }
                tempParsedUser.setBest_answerer(temp_best_answers);
            } else {
                tempParsedUser.setIdentity(null);
                tempParsedUser.setBest_answerer(null);
            }
            // locations
            int locationLength = (Integer) dc.read(tempBaseJsonPath + ".locations.length()");
            if (locationLength > 0) {
                int[] tempLocations = new int[locationLength];
                for (int j = 0; j < locationLength; j++) {
                    String tempLocationBase = tempBaseJsonPath + ".locations[" + j + "]";
                    ParsedTopic tempLocation = new ParsedTopic();
                    tempLocation.setId(Integer.parseInt((String) dc.read(tempLocationBase + ".id")));
                    tempLocation.setName((String) dc.read(tempLocationBase + ".name"));
                    tempLocation.setAvatar_url((String) dc.read(tempLocationBase + ".avatar_url"));
                    tempLocation.setIntroduction((String) dc.read(tempLocationBase + ".introduction"));
                    tempLocation.setIs_location(true);
                    tempLocations[j] = tempLocation.getId();
                    if (!parsedTopicMap.containsKey(tempLocations[j])) {
                        parsedTopicMap.put(tempLocations[j], tempLocation);
                    } else {
                        parsedTopicMap.get(tempLocations[j]).setIs_location(true);
                    }
                }
                tempParsedUser.setLocations(tempLocations);
            } else {
                tempParsedUser.setLocations(null);
            }
            // educations

            // employments
        }


        return parsedUserList;
    }

    @Override
    public List<User> parseListPage(Page page) {
        List<User> userList = new ArrayList<>();
        String baseJsonPath = "$.data.length()";
        DocumentContext dc = JsonPath.parse(page.getHtml());
        Integer userCount = dc.read(baseJsonPath);
        for (int i = 0; i < userCount; i++) {
            User user = new User();
            String userBaseJsonPath = "$.data[" + i + "]";
//            Boolean temp_is_advertiser = dc.read(userBaseJsonPath + ".is_advertiser");
//            if (temp_is_advertiser) {
//                System.out.println("get");
//                System.out.println(dc.read(userBaseJsonPath + ".name"));
//            }

//            int badgeCount = (Integer)dc.read(userBaseJsonPath + ".badge.length()");
//            if (badgeCount > 0) {
////                System.out.println("get");
//                for (int j = 0; j < badgeCount; j++){
//                    String badgeTypeBase = userBaseJsonPath + ".badge[" + j + "].type";
//                    String tempType = dc.read(badgeTypeBase);
//                    System.out.println(tempType);
//                    if (!tempType.equals("best_answerer") && !tempType.equals("identity")) {
//                        System.out.println("new badge type: " + tempType);
//                    }
//                }
//            }
            setUserInfoByJsonPth(user, "userToken", dc, userBaseJsonPath + ".url_token");//user_token
            setUserInfoByJsonPth(user, "username", dc, userBaseJsonPath + ".name");//username
            setUserInfoByJsonPth(user, "hashId", dc, userBaseJsonPath + ".id");//hashId
            setUserInfoByJsonPth(user, "followees", dc, userBaseJsonPath + ".following_count");//关注人数
            setUserInfoByJsonPth(user, "location", dc, userBaseJsonPath + ".locations[0].name");//位置
            setUserInfoByJsonPth(user, "business", dc, userBaseJsonPath + ".business.name");//行业
            setUserInfoByJsonPth(user, "employment", dc, userBaseJsonPath + ".employments[0].company.name");//公司
            setUserInfoByJsonPth(user, "position", dc, userBaseJsonPath + ".employments[0].job.name");//职位
            setUserInfoByJsonPth(user, "education", dc, userBaseJsonPath + ".educations[0].school.name");//学校
            setUserInfoByJsonPth(user, "answers", dc, userBaseJsonPath + ".answer_count");//回答数
            setUserInfoByJsonPth(user, "asks", dc, userBaseJsonPath + ".question_count");//提问数
            setUserInfoByJsonPth(user, "posts", dc, userBaseJsonPath + ".articles_count");//文章数
            setUserInfoByJsonPth(user, "followers", dc, userBaseJsonPath + ".follower_count");//粉丝数
            setUserInfoByJsonPth(user, "agrees", dc, userBaseJsonPath + ".voteup_count");//赞同数
            setUserInfoByJsonPth(user, "thanks", dc, userBaseJsonPath + ".thanked_count");//感谢数
            try {
                Integer gender = dc.read(userBaseJsonPath + ".gender");
                if (gender != null && gender == 1) {
                    user.setSex("male");
                } else if (gender != null && gender == 0) {
                    user.setSex("female");
                }
            } catch (PathNotFoundException e) {
                //没有该属性
            }
            userList.add(user);
        }
        return userList;
    }

    /**
     * jsonPath获取值，并通过反射直接注入到user中
     *
     * @param user
     * @param fieldName
     * @param dc
     * @param jsonPath
     */
    private void setUserInfoByJsonPth(User user, String fieldName, DocumentContext dc, String jsonPath) {
        try {
            Object o = dc.read(jsonPath);
            Field field = user.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(user, o);
        } catch (PathNotFoundException e1) {
            //no results
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
