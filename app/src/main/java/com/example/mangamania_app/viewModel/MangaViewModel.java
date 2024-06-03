package com.example.mangamania_app.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangamania_app.model.Manga;

import java.util.List;

public class MangaViewModel extends ViewModel {

    private MutableLiveData<List<Manga>> mangaList = new MutableLiveData<>();
    private MutableLiveData<Manga> selectedManga = new MutableLiveData<>();

    private MutableLiveData<List<Manga>> favoriteMangaList = new MutableLiveData<>();
    private MutableLiveData<Manga> selectedFavoriteManga = new MutableLiveData<>();  // selectedManga yeter mi ?


    public MutableLiveData<List<Manga>> getMangaList() {

        return mangaList;
    }

    public void setMangaList(List<Manga> mangas) {

        this.mangaList.setValue(mangas);
        Log.i("okay "," "+this.getMangaList().getValue().size());
    }

    public MutableLiveData<Manga> getSelectedManga() {
        return selectedManga;
    }

    public void setSelectedManga(Manga manga) {

        selectedManga.setValue(manga);
    }

    public MutableLiveData<List<Manga>> getFavoriteMangaList() {
        return favoriteMangaList;
    }

    public void setFavoriteMangaList(List<Manga> favoriteMangaList) {
        this.favoriteMangaList.setValue(favoriteMangaList);
    }

    public MutableLiveData<Manga> getSelectedFavoriteManga() {
        return selectedFavoriteManga;
    }

    public void setSelectedFavoriteManga(Manga selectedFavoriteManga) {
        this.selectedFavoriteManga.setValue(selectedFavoriteManga);
    }
}