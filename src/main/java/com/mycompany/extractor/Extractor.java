/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
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
    private static final String QUERY = "transmilenio";
    
    public static void search() throws Exception {
        File file = new File(String.format("data_%s.txt", QUERY));
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            Query query = new Query(QUERY);
            query.setCount(100);
            query.setSince("2016-11-07");
            query.setUntil("2018-11-07");
            QueryResult result = TWITTER.search(query);
            while (result.getTweets().size() > 0) {
                Long minId = Long.MAX_VALUE;
                for (Status s : result.getTweets()) {
                    write_result(out, s);
                    if (s.getId() < minId) {
                        minId = s.getId();
                    }
                }
                query.setMaxId(minId - 1);
                result = TWITTER.search(query);
            }
        }
    }
    
    private static void write_result(BufferedWriter out, Status status) {
        String user = status.getUser().getName();
        String user_location = status.getUser().getLocation();
        String tweet = status.getText();
        Date date = status.getCreatedAt();
        double latitude;
        double longitude;
        if (status.getGeoLocation() != null) {
            latitude = status.getGeoLocation().getLatitude();
            longitude = status.getGeoLocation().getLongitude();
        } else {
            latitude = -1;
            longitude = -1;
        }
        String place;
        if (status.getPlace() != null) {
            place = status.getPlace().getName();
        } else {
            place = "unknown";
        }
        int retweets = status.getRetweetCount();
        Status retweeted = status.getRetweetedStatus();
        Status quoted = status.getQuotedStatus();
        if (retweeted != null) {
            write_result(out, retweeted);
        }
        if (quoted != null) {
            write_result(out, quoted);
        }
        try {
            out.write(String.format("{user: %s, user_location: %s, date: %s, text: %s, latitude: %.10f, longitude: %.10f, place: %s, retweets: %d}%n",
                    user, user_location, date.toString(), tweet, latitude, longitude, place, retweets));
        } catch (IOException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
