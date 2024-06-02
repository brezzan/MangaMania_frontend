package com.example.mangamania_app.repo;

import android.os.Handler;
import android.os.Message;

import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Manga;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class FavoriteRepository {

    // get favorite farklı tipte cevap veriyorrrr  düzelttt

    public void getFavoriteManga(ExecutorService srv, Handler uiHandler, String token) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/manga/favorite");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("token", token);
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
                        // BURASI FARKLI TYPE - DEGİSTİRRR------------------------------------------------------------------------------------------
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {
                            // Send success message back to the main thread
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

    public void favoriteManga(ExecutorService srv, Handler uiHandler, String token, String mangaId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                // query param is like this
                String queryParams = "/?mangaId=" + URLEncoder.encode(mangaId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/manga/favorite"+queryParams);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("token", token);
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
                        ErrorResponse<String> jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());


                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);}
                        else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
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

    public void unfavoriteManga(ExecutorService srv, Handler uiHandler, String token, String mangaId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {


                String queryParams = "/?mangaId=" + URLEncoder.encode(mangaId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/manga/unfavorite"+queryParams);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("token", token);
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
                        ErrorResponse<String> jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());


                        if ("OK".equals(jsonResponse.getStatus())) {
                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);}
                        else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
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
