/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor.model;

import java.util.Date;

/**
 *
 * @author user
 */
public class Tweet {

    private String text;
    private String source;
    private long user;
    private long id;
    private Date date;
    private boolean is_retweet;
    private long id_retweeted;
    private int retweet_count;

    public Tweet(String text, String source, long user, long id, Date date, boolean is_retweet, long id_retweeted, int retweet_count) {
        this.text = text;
        this.source = source;
        this.user = user;
        this.id = id;
        this.date = date;
        this.is_retweet = is_retweet;
        this.id_retweeted = id_retweeted;
        this.retweet_count = retweet_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isIs_retweet() {
        return is_retweet;
    }

    public void setIs_retweet(boolean is_retweet) {
        this.is_retweet = is_retweet;
    }

    public long getId_retweeted() {
        return id_retweeted;
    }

    public void setId_retweeted(long id_retweeted) {
        this.id_retweeted = id_retweeted;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    @Override
    public String toString() {
//        String json_format = "{text:\"%s\",source:\"%s\",user:%d,id:%d,date:%s,is_retweet:%b,id_retweeted:%d,retweet_count:%d}";
//        String data_format = "\"%s\"~~\"%s\"~~%d~~%d~~%s~~%b~~%d~~%d";
        String data_format = "%s,%s,%d,%d,%s,%b,%d,%d";
        return String.format(data_format, text, source, user, id, date, is_retweet, id_retweeted, retweet_count);
    }
}
