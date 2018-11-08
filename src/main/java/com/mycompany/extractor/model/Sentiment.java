/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor.model;

import java.util.Locale;

/**
 *
 * @author user
 */
public class Sentiment {

    private String id;
    private double score;
    private long tweet_id;

    public long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(long tweet_id) {
        this.tweet_id = tweet_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%d,%.4f,%s%n", tweet_id, score, score <= 0.45 ? "negative" : score >= 0.55 ? "positive" : "neutral");
    }
}
