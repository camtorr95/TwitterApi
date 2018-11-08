/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author user
 */
public class Keyphrase implements Serializable {

    private String id;
    private List<String> keyPhrases;
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

    public List<String> getKeyPhrases() {
        return keyPhrases;
    }

    public void filter() {
        this.keyPhrases = this.keyPhrases.stream().filter(kp -> kp.split(" ").length == 1).map(kp -> Normalizer.normalize(kp, Normalizer.Form.NFD).toLowerCase()).collect(toList());
    }

    public void setKeyPhrases(List<String> keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.filter();
        keyPhrases.forEach(kp -> sb.append(String.format("%d,%s%n", tweet_id, kp)));
        return sb.toString();
    }
}
