package com.example.mangamania_app.repo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Token;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.model.Favorite;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
public class MangaRepository {

    public void getManga(ExecutorService srv, Handler uiHandler) {
        srv.execute(()->{

            try {
                URL url = new URL("http://10.0.2.2:8080/mangamania/manga");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
                StringBuilder buffer = new StringBuilder();
                int chr = 0;
                while((chr=reader.read())!=-1){
                    buffer.append((char)chr);
                }

                JSONArray arr = new JSONArray(buffer.toString());
                List<Manga> data = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {

                    JSONObject currentObj = arr.getJSONObject(i);

                    String mangaId = currentObj.optString("mangaId","");
                    String titleOv = currentObj.optString("titleOv","");
                    String titleEn = currentObj.optString("titleEn","");
                    String synopsis = currentObj.optString("synopsis","");

                    JSONObject alternativeTitlesObj = currentObj.optJSONObject("alternativeTitles");
                    Manga.AlternativeTitles alternativeTitles = new Manga.AlternativeTitles();
                    if (alternativeTitlesObj != null) {
                        alternativeTitles.setJapanese(alternativeTitlesObj.optString("japanese", ""));
                        alternativeTitles.setEnglish(alternativeTitlesObj.optString("english", ""));
                    }

                    JSONObject informationObj = currentObj.optJSONObject("information");
                    Manga.Information information = new Manga.Information();
                    if (informationObj != null) {
                        // Safely handle nested arrays
                        List<Manga.Type> informationType = extractTypes(informationObj.optJSONArray("types"));
                        List<Manga.Type> informationGenre = extractTypes(informationObj.optJSONArray("genres"));
                        List<Manga.Type> informationTheme = extractTypes(informationObj.optJSONArray("themes"));
                        List<Manga.Type> informationDemographic = extractTypes(informationObj.optJSONArray("demographics"));
                        List<Manga.Type> informationSerialization = extractTypes(informationObj.optJSONArray("serializations"));
                        List<Manga.Type> informationAuthors = extractTypes(informationObj.optJSONArray("authors"));

                        information.setVolumes(informationObj.optString("volumes", ""));
                        information.setChapters(informationObj.optString("chapters", ""));
                        information.setStatus(informationObj.optString("status", ""));
                        information.setPublished(informationObj.optString("published", ""));
                        information.setTypes(informationType);
                        information.setGenres(informationGenre);
                        information.setThemes(informationTheme);
                        information.setDemographics(informationDemographic);
                        information.setSerializations(informationSerialization);
                        information.setAuthors(informationAuthors);
                    }

                    JSONObject statisticsObj = currentObj.optJSONObject("statistics");
                    Manga.Statistics statistics = new Manga.Statistics();
                    if (statisticsObj != null) {
                        statistics.setScore(statisticsObj.optString("score", ""));
                        statistics.setRanked(statisticsObj.optString("ranked", ""));
                        statistics.setPopularity(statisticsObj.optString("popularity", ""));
                        statistics.setMembers(statisticsObj.optString("members", ""));
                        statistics.setFavorites(statisticsObj.optString("favorites", ""));
                    }

                    JSONArray charactersArray = currentObj.optJSONArray("characters");
                    List<Manga.Character> characters = new ArrayList<>();
                    if (charactersArray != null) {
                        for (int j = 0; j < charactersArray.length(); j++) {
                            JSONObject characterObj = charactersArray.optJSONObject(j);
                            if (characterObj != null) {
                                Manga.Character character = new Manga.Character();
                                character.setName(characterObj.optString("name", ""));
                                character.setPictureUrl(characterObj.optString("pictureUrl", ""));
                                character.setMyanimelistUrl(characterObj.optString("myanimelistUrl", ""));
                                characters.add(character);
                            }
                        }
                    }

                    String pictureUrl = currentObj.optString("pictureUrl", "");

                    Manga manga = new Manga(mangaId, titleOv, titleEn, synopsis, alternativeTitles, information, statistics, characters, pictureUrl);
                    data.add(manga);
                }


                Message message = uiHandler.obtainMessage();
                message.what = 1; // success
                message.obj = data;
                uiHandler.sendMessage(message);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        });

    }

    private List<Manga.Type> extractTypes(JSONArray typeArray) {
        List<Manga.Type> types = new ArrayList<>();
        if (typeArray != null ) {
            for (int i = 0; i < typeArray.length(); i++) {
                JSONObject typeObject = typeArray.optJSONObject(i);
                if (typeObject != null) {
                    Manga.Type type = new Manga.Type();
                    type.setName(typeObject.optString("name", ""));
                    type.setUrl(typeObject.optString("url", ""));
                    types.add(type);
                }
            }
        }
        return types;
    }

    // if selected manga works in adapter, we may not need thiis , not complete
    public void getMangaById(ExecutorService srv, Handler uiHandler,String id) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?id=" + URLEncoder.encode(id, "UTF-8") ;
                URL url = new URL("http://10.0.2.2:8080/mangamania/manga"+queryParams);
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
                        Manga jsonResponse = gson.fromJson(response.toString(), new TypeToken<Manga>() {}.getType());

                        // manga object  or {}

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);

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

    //http://localhost:8080/mangamania/manga/search/en/
    public void searchMangaByEn(ExecutorService srv, Handler uiHandler, String titleEn) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?titleEn=" + URLEncoder.encode(titleEn, "UTF-8") ;
                URL url = new URL("http://10.0.2.2:8080/mangamania/mangamania/manga/search/en"+queryParams);

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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);

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

    public void searchMangaByOv(ExecutorService srv, Handler uiHandler, String titleOv) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                String queryParams = "/?titleOv=" + URLEncoder.encode(titleOv, "UTF-8") ;
                URL url = new URL("http://10.0.2.2:8080/mangamania/mangamania/manga/search/ov"+queryParams);

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
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        // list of manga or []

                            // Send success message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);

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



/*
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

    public void getManga(ExecutorService srv, Handler uiHandler) {
        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:8080/mangamania/manga");
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

                        Gson gson = new Gson();
                        List<Manga> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Manga>>() {}.getType());

                        Log.d("MangaRepository", "Manga List: " + jsonResponse); // Log the response

                        Message message = uiHandler.obtainMessage();
                        message.what = 1; // success
                        message.obj = jsonResponse;
                        uiHandler.sendMessage(message);
                    }
                } else {
                    handleFailure(uiHandler, "Could not get mangas, response code: " + responseCode);
                }
            } catch (Exception e) {
                handleFailure(uiHandler, "Exception: " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
    }
    */