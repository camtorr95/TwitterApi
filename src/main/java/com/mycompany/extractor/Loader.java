/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor;

import com.mycompany.extractor.model.Tweet;
import com.mycompany.extractor.model.User;
import java.util.HashMap;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author user
 */
public class Loader {

    private final static HashMap<String, User> USERS = new HashMap<>();
    private final static HashMap<Long, Tweet> TWEETS = new HashMap<>();

    public static void load_user(User user) {
        USERS.putIfAbsent(user.getName(), user);
    }

    public static void load_tweet(Tweet tw) {
        TWEETS.putIfAbsent(tw.getId(), tw);
    }

    public static List<User> get_users() {
        System.out.println("Users: " + USERS.size());
        return USERS.values().stream().collect(toList());
    }

    public static List<Tweet> getTweets() {
        System.out.println("Tweets: " + TWEETS.size());
        return TWEETS.values().stream().collect(toList());
    }
}
