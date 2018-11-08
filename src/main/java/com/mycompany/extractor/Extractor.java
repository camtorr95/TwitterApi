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
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author user
 */
public class Extractor {

    private static final Twitter TWITTER = TwitterFactory.getSingleton();

    public static void search_data(String search_query) {
        try {
            Query query = new Query(search_query);
            query.setCount(100);
            query.setSince("2016-11-07");
            query.setUntil("2018-11-07");
            QueryResult result = TWITTER.search(query);
            while (result.getTweets().size() > 0) {
                Long minId = Long.MAX_VALUE;
                for (Status s : result.getTweets()) {
                    get_result_data(s);
                    if (s.getId() < minId) {
                        minId = s.getId();
                    }
                }
                query.setMaxId(minId - 1);
                result = TWITTER.search(query);
            }
        } catch (Exception ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void get_result_data(Status status) {
        //Load User
        long user_id = status.getUser().getId();
        User new_user = new User(user_id, status.getUser().getName(), status.getUser().getLocation());
        Loader.load_user(new_user);

        //Load Tweet
        int low_bound = status.getSource().indexOf(">") + 1;
        int up_bound = status.getSource().indexOf("<", 3);
        String source = status.getSource().substring(low_bound, up_bound);
        long id_rewteeted = status.isRetweet() ? status.getRetweetedStatus().getId() : -1;
        Tweet new_tweet = new Tweet(status.getText(), source, user_id, status.getId(), status.getCreatedAt(), status.isRetweet(), id_rewteeted, status.getRetweetCount());
        Loader.load_tweet(new_tweet);
    }

    public static void search_test() throws Exception {
        File file = new File(String.format("data_test.txt"));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            Query query = new Query("sitp");
            query.setCount(100);
            query.setSince("2016-11-07");
            query.setUntil("2018-11-07");
            QueryResult result = TWITTER.search(query);
            while (result.getTweets().size() > 0) {
                Long minId = Long.MAX_VALUE;
                for (Status s : result.getTweets()) {
                    write_result_test(out, s);
                    if (s.getId() < minId) {
                        minId = s.getId();
                    }
                }
                query.setMaxId(minId - 1);
                result = TWITTER.search(query);
            }
        }
    }

    private static void write_result_test(BufferedWriter out, Status status) {
        int tweet = status.getText().hashCode();
        long id = status.getId();
        boolean rt = status.isRetweet();
        long rt_id = rt ? status.getRetweetedStatus().getId() : -1;
        int low_bound = status.getSource().indexOf(">") + 1;
        int up_bound = status.getSource().indexOf("<", 3);
        String source = status.getSource().substring(low_bound, up_bound);
        try {
            out.write(String.format("source: %s -- text: %d -- id: %d -- rt: %b -- quote: %d%n", source, tweet, id, rt, rt_id));
        } catch (IOException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
