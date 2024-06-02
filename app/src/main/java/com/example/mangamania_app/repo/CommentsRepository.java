package com.example.mangamania_app.repo;

import android.os.Handler;
import android.os.Message;

import com.example.mangamania_app.model.Chapter;
import com.example.mangamania_app.model.Comment;
import com.example.mangamania_app.model.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CommentsRepository {

    //http://localhost:8080/mangamania/comment/search/chapterid/?id={id}

    public void getCommentsForChapter(ExecutorService srv, Handler uiHandler, String chapterId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?id=" + URLEncoder.encode(chapterId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/search/chapterid"+queryParams);

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
                        List<Comment> jsonResponse = gson.fromJson(response.toString(), new TypeToken<List<Comment>>() {}.getType());

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

    //http://localhost:8080/mangamania/comment/save
    // pass and empty string for rootcomment if not reply to someoneelse
    public void saveComment(ExecutorService srv, Handler uiHandler,String token,String commentText, String chapterId, String rootComment) {

        /*
        {"commentText": "…", "rootComment": "…", "chapterId": "…"} (replying to someone) or
        {"commentText": "…", "chapterId": "…"}   (standalone comment)
        */

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/save");


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("token", token);
                urlConnection.setDoOutput(true);


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("commentText", commentText);
                jsonParam.put("rootComment", rootComment);
                jsonParam.put("chapterId", chapterId);

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }


                        Gson gson = new Gson();
                        ErrorResponse<String> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());
                        } catch (JsonSyntaxException e) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = "JSON Parsing error: " + e.getMessage();

                            uiHandler.sendMessage(message);
                            return;
                        }


                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse.getData();
                            uiHandler.sendMessage(message);

                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Signup failed: " + responseCode;

                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });



    }



    public void likeComment(ExecutorService srv, Handler uiHandler,String token,String commentId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "?commentId=" + URLEncoder.encode(commentId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/like/"+queryParams);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
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

                        Gson gson = new Gson();
                        ErrorResponse<String> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());
                        } catch (JsonSyntaxException e) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = "JSON Parsing error: " + e.getMessage();

                            uiHandler.sendMessage(message);
                            return;
                        }


                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse.getData();
                            uiHandler.sendMessage(message);

                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Signup failed: " + responseCode;

                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });



    }

    public void dislikeComment(ExecutorService srv, Handler uiHandler,String token,String commentId) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "?commentId=" + URLEncoder.encode(commentId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/dislike/"+queryParams);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
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


                        Gson gson = new Gson();
                        ErrorResponse<String> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());
                        } catch (JsonSyntaxException e) {
                            // Handle JSON parsing error
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = "JSON Parsing error: " + e.getMessage();

                            uiHandler.sendMessage(message);
                            return;
                        }

                        // Check response status and data
                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse.getData();
                            uiHandler.sendMessage(message);

                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Signup failed: " + responseCode;

                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });

    }

    public void alterComment(ExecutorService srv, Handler uiHandler,String token,String commentId, String commentText) {

        // {"commentId":"…", "commentText":  "…"}

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {


                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/alter/");

                // Create HttpURLConnection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("token", token);
                urlConnection.setDoOutput(true);

                // Create JSON object with user credentials
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("commentId", commentId);
                jsonParam.put("commentText", commentText);


                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }


                        Gson gson = new Gson();
                        ErrorResponse<String> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());
                        } catch (JsonSyntaxException e) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = "JSON Parsing error: " + e.getMessage();

                            uiHandler.sendMessage(message);
                            return;
                        }

                        // Check response status and data
                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {
                            // Send the error message back to the main thread
                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse.getData();
                            uiHandler.sendMessage(message);

                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Signup failed: " + responseCode;

                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });



    }

    public void deleteComment(ExecutorService srv, Handler uiHandler,String token,String commentId) {

        // {"commentId":"…", "commentText":  "…"}

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                String queryParams = "?commentId=" + URLEncoder.encode(commentId, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/comment/delete/"+queryParams);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
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


                        Gson gson = new Gson();
                        ErrorResponse<String> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());
                        } catch (JsonSyntaxException e) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = "JSON Parsing error: " + e.getMessage();

                            uiHandler.sendMessage(message);
                            return;
                        }


                        if ("OK".equals(jsonResponse.getStatus())) {

                            Message message = uiHandler.obtainMessage();
                            message.what = 1; // success
                            message.obj = jsonResponse;
                            uiHandler.sendMessage(message);
                        } else {

                            Message message = uiHandler.obtainMessage();
                            message.what = 0; // failure
                            message.obj = jsonResponse.getData();
                            uiHandler.sendMessage(message);

                        }
                    }
                } else {
                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Signup failed: " + responseCode;

                    uiHandler.sendMessage(message);
                }
            } catch (Exception e) {
                Message message = uiHandler.obtainMessage();
                message.what = 0; // failure
                message.obj = "Exception: " + e.getMessage();
                uiHandler.sendMessage(message);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });



    }
}

