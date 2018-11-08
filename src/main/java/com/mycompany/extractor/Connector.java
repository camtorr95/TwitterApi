package com.mycompany.extractor;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

/*
 * Gson: https://github.com/google/gson
 * Maven info:
 *     groupId: com.google.code.gson
 *     artifactId: gson
 *     version: 2.8.1
 *
 * Once you have compiled or downloaded gson-2.8.1.jar, assuming you have placed it in the
 * same folder as this file (GetSentiment.java), you can compile and run this program at
 * the command line as follows.
 *
 * javac GetSentiment.java -classpath .;gson-2.8.1.jar -encoding UTF-8
 * java -cp .;gson-2.8.1.jar GetSentiment
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.extractor.model.Keyphrase;
import com.mycompany.extractor.model.Sentiment;
import com.mycompany.extractor.model.Tweet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

class Document {

    public String id, language, text;

    public Document(String id, String language, String text) {
        this.id = id;
        this.language = language;
        this.text = text;
    }
}

class Documents {

    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<>();
    }

    public void add(String id, String language, String text) {
        this.documents.add(new Document(id, language, text));
    }
}

public class Connector {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************
// Replace the accessKey string value with your valid access key.
    static String accessKey = "8947d2faf2174ac1875f3cbd2f09c039";

// Replace or verify the region.
// You must use the same region in your REST API call as you used to obtain your access keys.
// For example, if you obtained your access keys from the westus region, replace 
// "westcentralus" in the URI below with "westus".
// NOTE: Free trial access keys are generated in the westcentralus region, so if you are using
// a free trial access key, you should not need to change this region.
    static String host = "https://eastus.api.cognitive.microsoft.com";

    static String path_sentiment = "/text/analytics/v2.0/sentiment";
    static String path_keyphrases = "/text/analytics/v2.0/keyPhrases";

    private static BufferedReader in = null;
    private static HttpsURLConnection connection = null;

    private static List<Tweet> tweets = Loader.getTweets().stream().filter(t -> !t.isIs_retweet()).collect(toList());

    private static int counter = 0;

    private static void get_sentiment(Documents documents, StringBuilder response) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host + path_sentiment);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
    }
    private static void get_keyphrase(Documents documents, StringBuilder response) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host + path_keyphrases);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    private static List<Response> responses = new LinkedList<>();

    private static void gson_map(String response) {
        Gson gson = new Gson();
        Response resp = gson.fromJson(response, Response.class);
        responses.add(resp);
        System.out.println(resp.toString());
    }

    public static void analyze() {
        AtomicInteger count = new AtomicInteger(0);
//        sentiment_analysis();
//        responses.forEach(r -> r.getDocuments().forEach(st -> st.setTweet_id(tweets.get(count.getAndIncrement()).getId())));
//        responses.forEach(r -> r.getDocuments().forEach(Loader::load_sentiment));
        keyphrase_analysis();
        responses.forEach(r -> r.getDocuments().forEach(kp -> kp.setTweet_id(tweets.get(count.getAndIncrement()).getId())));
        responses.forEach(r -> r.getDocuments().forEach(Loader::load_keyphrase));
    }

    private static void sentiment_analysis() {
        try {
            for (int i = 0; i < 5000; i += 500) {
                sentiment_run(tweets.subList(i, i + 500));
            }
            sentiment_run(tweets.subList(5000, 5482));
            in.close();
            in = null;
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void keyphrase_analysis() {
        try {
            for (int i = 0; i < 5000; i += 500) {
                keyphrase_run(tweets.subList(i, i + 500));
            }
            keyphrase_run(tweets.subList(5000, 5482));
            in.close();
            in = null;
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void keyphrase_run(List<Tweet> tweets) {
        StringBuilder response = new StringBuilder();
        try {
            Documents documents = new Documents();
            tweets.forEach(t -> documents.add(String.valueOf(t.getId()), "es", t.getText()));
            get_keyphrase(documents, response);
            System.out.println(response);
            gson_map(response.toString());
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (Exception ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void sentiment_run(List<Tweet> tweets) {
        StringBuilder response = new StringBuilder();
        try {
            Documents documents = new Documents();
            tweets.forEach(t -> documents.add(String.valueOf(t.getId()), "es", t.getText()));
            get_sentiment(documents, response);
            gson_map(response.toString());
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (Exception ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class Response {

        private List<Keyphrase> documents;
        private List<String> errors;

        public List<Keyphrase> getDocuments() {
            return documents;
        }

        public void setDocuments(List<Keyphrase> documents) {
            this.documents = documents;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }
}
