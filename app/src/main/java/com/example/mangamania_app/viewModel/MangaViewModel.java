package com.example.mangamania_app.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangamania_app.model.Manga;

import java.util.List;

public class MangaViewModel extends ViewModel {

    private MutableLiveData<List<Manga>> mangaList = new MutableLiveData<>();
    private MutableLiveData<Manga> selectedManga = new MutableLiveData<>();
    public MutableLiveData<List<Manga>> getMangaList() {
        return mangaList;
    }

    public void setMangaList(List<Manga> mangas) {
        this.mangaList.setValue(mangas);
    }

    public MutableLiveData<Manga> getSelectedManga() {
        return selectedManga;
    }

    public void setSelectedManga(Manga manga) {

        selectedManga.setValue(manga);
    }

}