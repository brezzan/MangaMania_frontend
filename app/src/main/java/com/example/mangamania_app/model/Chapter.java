package com.example.mangamania_app.model;

public class Chapter {


    private String chapterId;
    private String chapterName;
    private int chapterNumber;
    private String description;

    private Manga manga;  // foreign key to manga @dbref but ı do not know how this will handle it

    public Chapter(String chapterName, int chapterNumber, String description, Manga manga) {
        super();

        this.chapterName = chapterName;
        this.chapterNumber = chapterNumber;
        this.description = description;
        this.manga = manga;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Manga getManga() {
        return manga;
    }

    public void setManga(Manga manga) {
        this.manga = manga;
    }



}