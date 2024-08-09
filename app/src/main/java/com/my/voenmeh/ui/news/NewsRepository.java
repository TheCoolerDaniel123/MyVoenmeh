package com.my.voenmeh.ui.news;

import android.util.Log;

import com.my.voenmeh.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NewsRepository {
    List<PostRepository> listOfPosts = null;
    final String accessToken = "e9c2c921e9c2c921e9c2c921e1ead507a6ee9c2e9c2c9218ff45770cd19be73beb73d56";
    final String ownerId = "-457254637";
    final String domain = "bstu_voenmeh";
    final String offset = "0";
    final Integer count = Constants.NUMBER_OF_POSTS;
    final String filter = "owner";
    final String v = "5.199"; // TODO getLatest();

    volatile String jsonString;

    NewsRepository() {
        fetchLatestNews();
    }

    private void fetchLatestNews() {
        listOfPosts = new ArrayList<PostRepository>();

        makeNetworkRequest();
        while(jsonString == null) {
            ;
        } // ожидание ответа...

        // на этом моменте в jsonString хранится ответ с сервера ВК в виде json-строки

        String text = ""; // текст записи под номером 1, 2, 3...
        String imageUrl = "";

        for (int i = 0; i < Constants.NUMBER_OF_POSTS; i++) {
            text = getTextFromJson(i);
            imageUrl = getImageFromJson(i);
            listOfPosts.add(new PostRepository(text, imageUrl));
        }
    }

    private void makeNetworkRequest() {
        String requestString = "https://api.vk.com/method/wall.get?access_token=" + accessToken + "&owner_id=" + ownerId +
                "&domain=" + domain + "&offset=" + offset + "&count=" + count.toString() + "&filter=" + filter +
                "&v=" + v;

        Thread NetworkRequest;
        Runnable runnable;
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    jsonString = getContent(requestString);
                }
                catch (Exception e) {
                    Log.d("EXCEPTION: ", e.toString());
                }
            }
        };
        NetworkRequest = new Thread(runnable);
        NetworkRequest.start();
    }

    private String getContent(String path) throws IOException {
        HttpsURLConnection conn = null;
        BufferedReader reader = null;
        InputStream istream = null;

        try {
            URL url = new URL(path);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.connect();

            istream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(istream));
            StringBuilder buf = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }

            //System.out.println("returned jsonString in getContent(): " + buf);
            return buf.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
            if (istream != null) {
                istream.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String getTextFromJson(int index) {
        // в переменной index - номер поста по порядку, из которого надо вытащить текст
        String txt = "";

        // парсинг json-строки
        try {
            JSONObject jResponse = new JSONObject(jsonString);
            JSONObject jObj = jResponse.getJSONObject("response");
            JSONArray jArr = jObj.getJSONArray("items");
            JSONObject oneObject = jArr.getJSONObject(index);
            txt = oneObject.getString("text");
        }
        catch (JSONException e) {
            System.out.println("JSON Exception: " + e.toString());
        }

        return txt;
    }

    private String getImageFromJson(int postIndex) {
        //postIndex - индекс поста
        String img = ""; //https://sun9-33.userapi.com/impg/-Ye-AMZ__Epgw8gzYlP97a6ccOXzBmknxtCnvw/WuEBawwHTBk.jpg?size=1080x1036&quality=96&sign=6b9e5b453df618fbc8625494fa236160&type=album";

        try {
            JSONObject jResponse = new JSONObject(jsonString);
            JSONObject jObj = jResponse.getJSONObject("response");
            JSONArray jArr = jObj.getJSONArray("items");
            JSONObject oneObject = jArr.getJSONObject(postIndex);

            // теперь мы находимся в items[i]
            JSONArray jArrAttachments = oneObject.getJSONArray("attachments");
            for (int i = 0; i < jArrAttachments.length(); i++) { // выполнится 1 раз или столько, сколько в посте фоток
                JSONObject itemi = jArrAttachments.getJSONObject(i);
                JSONObject photo = itemi.getJSONObject("photo");
                JSONArray sizes = photo.getJSONArray("sizes");
                JSONObject needed = sizes.getJSONObject(sizes.length() - 1); // картинка с самым охуенным качеством
                img = needed.getString("url");
            }
        }
        catch (JSONException e) {
            System.out.println("JSON Exception: " + e.toString());
        }

        return img;
    }
}
