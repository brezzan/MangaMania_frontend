package com.example.mangamania_app.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.model.Chapter;

import java.util.List;
public class ChapterViewModel extends ViewModel{

    private MutableLiveData<List<Chapter>> chaptersList = new MutableLiveData<>();
    private MutableLiveData<Chapter> selectedChapter = new MutableLiveData<>();

    public MutableLiveData<List<Chapter>> getChaptersList() {
        return chaptersList;
    }

    public void setChaptersList(List<Chapter> chaptersList) {
        this.chaptersList.setValue(chaptersList);
    }

    public MutableLiveData<Chapter> getSelectedChapter() {
        return selectedChapter;
    }

    public void setSelectedChapter(Chapter selectedChapter) {
        this.selectedChapter.setValue(selectedChapter);
    }

}
