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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class App {

    private static final String[] QUERYS = {"sitp", "transmilenio", "transporte publico bogota"};

    public static void main(String[] args) {
//        Arrays.asList(QUERYS).forEach(q -> Extractor.search_data(q));
//        write_data();
        read_data();
//        analyze();
        write_data_csv();
    }

    private static void write_data() {
        File users = new File("data/users.data");
        File tweets = new File("data/tweets.data");
        try (BufferedWriter uout = new BufferedWriter(new FileWriter(users));
                BufferedWriter tout = new BufferedWriter(new FileWriter(tweets))) {
            Loader.get_users().forEach(u -> write_object(uout, u));
            Loader.getTweets().forEach(t -> write_object(tout, t));
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void read_data() {
        File users = new File("data/users.data");
        File tweets = new File("data/tweets.data");
        Loader.load_users_file(users);
        Loader.load_tweets_file(tweets);
    }

    private static void write_data_csv() {
//        Loader.write_users_csv();
        Loader.write_tweets_csv();
//        Loader.write_sentiments_csv();
//        Loader.write_keyphrases_csv();
    }

    private static void write_object(BufferedWriter out, Object o) {
        try {
            out.write(o.toString());
            out.write("==");
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void analyze() {
        Connector.analyze();
    }
}
