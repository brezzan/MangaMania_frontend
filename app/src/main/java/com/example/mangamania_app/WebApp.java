package com.example.mangamania_app;

import android.app.Application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebApp extends Application {
    public ExecutorService srv = Executors.newCachedThreadPool();
}