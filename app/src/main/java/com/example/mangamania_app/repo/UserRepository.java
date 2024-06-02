package com.example.mangamania_app.repo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Token;
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
import java.util.concurrent.ExecutorService;


public class UserRepository {

    public void login(ExecutorService srv, Handler uiHandler, String username, String password) {
        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/login");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);

                // body
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", username);
                jsonParam.put("password", password);


                try (OutputStream os = urlConnection.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                    writer.write(jsonParam.toString());
                    writer.flush();
                }

                // Read response
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }


                        Gson gson = new Gson();
                        ErrorResponse<Token> jsonResponse;
                        try {
                            jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<Token>>(){}.getType());
                        } catch (JsonSyntaxException e) {
                            // Handle JSON parsing error
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
                            Log.i("Dee", message.obj.toString());
                        }
                    }
                } else {

                    Message message = uiHandler.obtainMessage();
                    message.what = 0; // failure
                    message.obj = "Login failed: " + responseCode;

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



    public void logout(ExecutorService srv, Handler uiHandler, String token) {
        Log.i("token",token);
        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:8080/mangamania/logout");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("token", token);
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
                        ErrorResponse<String> jsonResponse = gson.fromJson(response.toString(), new TypeToken<ErrorResponse<String>>() {}.getType());

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
                    message.obj = "Logout failed 2: " + responseCode;
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


    public void register(ExecutorService srv, Handler uiHandler, String username, String mail, String password) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("http://10.0.2.2:8080/mangamania/register");


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", username);
                jsonParam.put("mail", mail);
                jsonParam.put("password", password);



                try (OutputStream os = urlConnection.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                    writer.write(jsonParam.toString());
                    writer.flush();
                }


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