package com.example.mangamania_app.repo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Token;
import com.example.mangamania_app.model.Manga;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
public class MangaRepository {

    // http://localhost:8080/mangamania/manga
    public void getManga(ExecutorService srv, Handler uiHandler) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/manga");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                // Read response
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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {
                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {
                    // Handle the error response
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

    // if selected manga works in adapter, we may not need thiis , not complete
    public void getMangaById(ExecutorService srv, Handler uiHandler,String id) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?id=" + URLEncoder.encode(id, "UTF-8") ;
                URL url = new URL("http://10.0.2.2:8080/mangamania/manga");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                // Read response
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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {
                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {
                    // Handle the error response
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

    //http://localhost:8080/mangamania/manga/search/en/
    public void searchMangaByEn(ExecutorService srv, Handler uiHandler, String titleEn) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/mangamania/manga/search/en/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("titleEn", titleEn);


                // Read response
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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {
                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {
                    // Handle the error response
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

    public void searchMangaByOv(ExecutorService srv, Handler uiHandler, String titleOv) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/mangamania/manga/search/ov/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("titleEn", titleOv);

                // Read response
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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []
                        if (! jsonResponse.isEmpty()) {
                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        }
                    }
                } else {
                    // Handle the error response
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
