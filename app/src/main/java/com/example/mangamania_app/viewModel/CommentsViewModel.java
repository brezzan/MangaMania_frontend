package com.example.mangamania_app.viewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mangamania_app.model.Comment;

import java.util.List;

public class CommentsViewModel extends ViewModel{


    private MutableLiveData<List<Comment>> commentsList = new MutableLiveData<>();
    private MutableLiveData<Comment> selectedComment = new MutableLiveData<>();

    public MutableLiveData<List<Comment>> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comment> commentsList) {
        this.commentsList.setValue(commentsList);
    }

    public MutableLiveData<Comment> getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment.setValue(selectedComment);
    }
}
