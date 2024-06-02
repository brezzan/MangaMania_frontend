package com.example.mangamania_app.model;

import java.util.List;


public class Favorite {


    private String id;


    private User user;  // @DBRef


    private List<Manga> favoriteMangas;   //     @DBRef

    public Favorite(User user, List<Manga> favoriteMangas) {
        super();
        this.user = user;
        this.favoriteMangas = favoriteMangas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Manga> getFavoriteMangas() {
        return favoriteMangas;
    }

    public void setFavoriteMangas(List<Manga> favoriteMangas) {
        this.favoriteMangas = favoriteMangas;
    }


}