package com.example.mangamania_app.repo;
import android.os.Handler;
import android.os.Message;

import com.example.mangamania_app.model.Chapter;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;



import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ChapterRepository {

    // chapter adapter ile selected chapter tutabilirsek bunu yazmaya gerek yok

    public void getChapterById(ExecutorService srv, Handler uiHandler,String id) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?id=" + URLEncoder.encode(id, "UTF-8") ;
                URL url = new URL("http://10.0.2.2:8080/mangamania/chapter"+queryParams);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");


                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        // new TypeToken<   ....>
                        Gson gson = new Gson();
                        List<Chapter> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Chapter>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Could not get chapters" + responseCode;
                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception 1: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
    }


    //http://localhost:8080/mangamania/chapter/search/mangaid/?id=6625222636c99ae69dedd9d4
    public void getChaptersOfManga(ExecutorService srv, Handler uiHandler, String mangaId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "?id=" + URLEncoder.encode(mangaId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/chapter/search/mangaid/"+queryParams);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);


                int responseCode = urlConnection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        // new TypeToken<   ....>
                        Gson gson = new Gson();
                        List<Chapter> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Chapter>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0;
                    message.obj = "Could not get mangas" + responseCode;
                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception 1: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
    }
}
