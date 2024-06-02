package com.example.mangamania_app.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Token;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<ErrorResponse<Token>> loginToken = new MutableLiveData<>();

    public MutableLiveData<ErrorResponse<Token>> getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(ErrorResponse<Token> loginToken) {
        this.loginToken.setValue(loginToken);
    }
}
