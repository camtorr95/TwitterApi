/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor;

import com.mycompany.extractor.model.Tweet;
import com.mycompany.extractor.model.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author user
 */
public class Loader {

    private final static HashMap<Long, User> USERS = new HashMap<>();
    private final static HashMap<Long, Tweet> TWEETS = new HashMap<>();

    public static void load_user(User user) {
        USERS.putIfAbsent(user.getId(), user);
    }

    public static void load_tweet(Tweet tw) {
        TWEETS.putIfAbsent(tw.getId(), tw);
    }

    public static void load_users_file(File file) {
        try {
            Scanner sc = new Scanner(file);
            sc.useDelimiter("==");
            while (sc.hasNext()) {
                String user = sc.next();
                add_user_string(user);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void load_tweets_file(File file) {
        try {
            Scanner sc = new Scanner(file);
            sc.useDelimiter("==");
            while (sc.hasNext()) {
                String user = sc.next();
                add_tweet_string(user);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void add_user_string(String user) {
        String[] props = user.split("~~");
        User new_user = new User(Long.parseLong(props[0]), props[1], props[2]);
        USERS.put(new_user.getId(), new_user);
    }

    private static void add_tweet_string(String tweet) {
        try {
            String[] props = tweet.split("~~");
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
            Tweet new_tweet = new Tweet(props[0], props[1], Long.parseLong(props[2]), Long.parseLong(props[3]), df.parse(props[4]), Boolean.parseBoolean(props[5]), Long.parseLong(props[6]), Integer.parseInt(props[7]));
            TWEETS.put(new_tweet.getId(), new_tweet);
        } catch (ParseException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_users_csv() {
        try {
            File users = new File("data/users.csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(users));
            get_users().forEach(user -> write_user(out, user));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_tweets_csv() {
        try {
            File tweets = new File("data/tweets.csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(tweets));
            getTweets().forEach(tweet -> write_tweet(out, tweet));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_user(BufferedWriter out, User user) {
        try {
            out.write(user.toString());
            out.write("\n");
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_tweet(BufferedWriter out, Tweet tweet) {
        try {
            out.write(tweet.toString());
            out.write("\n");
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
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
