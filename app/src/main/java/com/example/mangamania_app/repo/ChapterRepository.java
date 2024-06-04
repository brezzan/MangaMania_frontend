package com.example.mangamania_app.repo;
import static java.lang.Integer.parseInt;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mangamania_app.model.Chapter;
import com.example.mangamania_app.repo.JSONtoModelFunctions;

import com.example.mangamania_app.model.Manga;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
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



import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ChapterRepository {


    public void getChapterById(ExecutorService srv, Handler uiHandler,String id) {

        srv.execute(() -> {
            HttpURLConnection urlConnection = null;
            try {

                String queryParams = "/?id=" + URLEncoder.encode(id, "UTF-8") ;

                URL url = new URL("http://10.0.2.2:8080/mangamania/chapter"+queryParams);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
                StringBuilder buffer = new StringBuilder();
                int chr = 0;
                while((chr=reader.read())!=-1){
                    buffer.append((char)chr);
                }

                JSONtoModelFunctions converter = new JSONtoModelFunctions();

                List<Chapter> data = new ArrayList<>();
                data = converter.jsontoChapterList(buffer);

                Message message = uiHandler.obtainMessage();
                message.what = 1; // success
                message.obj = data;
                uiHandler.sendMessage(message);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    //http://localhost:8080/mangamania/chapter/search/mangaid/?id=6625222636c99ae69dedd9d4
    public void getChaptersOfManga(ExecutorService srv, Handler uiHandler, String mangaId) {

        srv.execute(() -> {
                try {

                    String queryParams = "?id=" + URLEncoder.encode(mangaId, "UTF-8") ;

                    URL url = new URL("http://10.0.2.2:8080/mangamania/chapter/search/mangaid/"+queryParams);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
                    StringBuilder buffer = new StringBuilder();
                    int chr = 0;
                    while((chr=reader.read())!=-1){
                        buffer.append((char)chr);
                    }
                    Log.i("is it empty",""+buffer.toString().substring(0,2));
                    JSONtoModelFunctions converter = new JSONtoModelFunctions();

                    List<Chapter> data = new ArrayList<>();
                    data = converter.jsontoChapterList(buffer);

                    Message message = uiHandler.obtainMessage();
                    message.what = 1; // success
                    message.obj = data;
                    uiHandler.sendMessage(message);

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });
    }
}
