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
import com.mycompany.extractor.model.Tweet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.documents = new ArrayList<Document>();
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
    
    public static String get_sentiment(Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");
        
        return connectApi(encoded_text, new URL(host + path_sentiment));
    }
    
    public static String get_keyphrases(Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");
        
        return connectApi(encoded_text, new URL(host + path_keyphrases));
    }
    
    private static String connectApi(byte[] encoded_text, URL url) throws Exception {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);
        
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();
        
        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        
        return response.toString();
    }
    
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
    
    public static void analyze_tweets() {
        List<Tweet> tweets = Loader.getTweets().stream().filter(t -> !t.isIs_retweet()).collect(toList());
        Documents documents = new Documents();
        tweets.forEach(tweet -> documents.add(String.valueOf(tweet.getId()), "es", tweet.getText()));
        try {
            write_response(get_sentiment(documents), "sentiment");
            write_response(get_keyphrases(documents), "keyphrases");
        } catch (Exception ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void write_response(String response, String service) throws IOException {
        File file = new File("analisis/analisis_" + service + ".data");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(prettify(response));
        out.write("\n");
    }

//    public static void main(String[] args) {
//        try {
//            Documents documents = new Documents();
//            documents.add("1", "en", "I really enjoy the new XBox One S. It has a clean look, it has 4K/HDR resolution and it is affordable.");
//            documents.add("2", "es", "Este ha sido un dia terrible, llegué tarde al trabajo debido a un accidente automobilistico.");
//            
//            String response = get_sentiment(documents);
//            String rta = get_keyphrases(documents);
//            System.out.println(prettify(response));
//            System.out.println(prettify(rta));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
}
