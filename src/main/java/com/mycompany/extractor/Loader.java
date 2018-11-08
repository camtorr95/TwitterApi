/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor;

import com.mycompany.extractor.model.Keyphrase;
import com.mycompany.extractor.model.Sentiment;
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
    private final static HashMap<Long, Sentiment> SENTIMENTS = new HashMap<>();
    private final static HashMap<Long, Keyphrase> KEYPHRASES = new HashMap<>();

    public static void load_user(User user) {
        USERS.putIfAbsent(user.getId(), user);
    }

    public static void load_tweet(Tweet tw) {
        TWEETS.putIfAbsent(tw.getId(), tw);
    }

    public static void load_sentiment(Sentiment st) {
        SENTIMENTS.putIfAbsent(Long.parseLong(st.getId()), st);
    }

    public static void load_keyphrase(Keyphrase kp) {
        KEYPHRASES.putIfAbsent(Long.parseLong(kp.getId()), kp);
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
            Tweet new_tweet = new Tweet(props[0].replace("\n", "").replace("\r", "").replaceAll("\\,", " "), props[1], Long.parseLong(props[2]), Long.parseLong(props[3]), df.parse(props[4]), Boolean.parseBoolean(props[5]), Long.parseLong(props[6]), Integer.parseInt(props[7]));
            TWEETS.put(new_tweet.getId(), new_tweet);
        } catch (ParseException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_users_csv() {
        try {
            File users = new File("data/users.csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(users));
            String header = "id,name,location\n";
            out.write(header);
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
            String header = "text,source,user,id,date,is_retweet,id_retweeted,retweet_count\n";
            out.write(header);
            getTweets().forEach(tweet -> write_tweet(out, tweet));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_sentiments_csv() {
        try {
            File tweets = new File("data/sentiments.csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(tweets));
            String header = "tweet_id,score,value\n";
            out.write(header);
            get_sentiments().forEach(st -> write_sentiment(out, st));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void write_keyphrases_csv() {
        try {
            File keyphrases = new File("data/keyphrases.csv");
            BufferedWriter out = new BufferedWriter(new FileWriter(keyphrases));
            String header = "tweet_id,keyword\n";
            out.write(header);
            get_keyphrases().forEach(kp -> write_keyphrase(out, kp));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_keyphrase(BufferedWriter out, Keyphrase kp) {
        try {
            out.write(kp.toString());
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_sentiment(BufferedWriter out, Sentiment st) {
        try {
            out.write(st.toString());
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_user(BufferedWriter out, User user) {
        try {
            out.write(user.toString());
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_tweet(BufferedWriter out, Tweet tweet) {
        try {
            out.write(tweet.toString());
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<User> get_users() {
        System.out.println("Users: " + USERS.size());
        return USERS.values().stream().collect(toList());
    }

    public static List<Sentiment> get_sentiments() {
        System.out.println("Sentiments: " + SENTIMENTS.size());
        return SENTIMENTS.values().stream().collect(toList());
    }

    public static List<Keyphrase> get_keyphrases() {
        System.out.println("Keyphrases: " + KEYPHRASES.size());
        return KEYPHRASES.values().stream().collect(toList());
    }

    public static List<Tweet> getTweets() {
        System.out.println("Tweets: " + TWEETS.size());
        return TWEETS.values().stream().collect(toList());
    }
}
