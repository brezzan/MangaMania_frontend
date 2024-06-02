package com.example.mangamania_app;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mangamania_app.databinding.FragmentLoginPageBinding;
import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Token;
import com.example.mangamania_app.repo.UserRepository;
import com.example.mangamania_app.viewModel.LoginViewModel;

import java.util.concurrent.ExecutorService;


public class LoginPage extends Fragment {

    private FragmentLoginPageBinding binding;
    private LoginViewModel loginViewModel;
    private ExecutorService srv;
     private Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    ErrorResponse<Token> response = (ErrorResponse<Token>) msg.obj;
                    if ("OK".equals(response.getStatus())) {
                        Token token = response.getData();
                        loginViewModel.setLoginToken(response);
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                        navController.navigate(R.id.action_loginPage_to_mainPage);
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Login failed: " + response.getData(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("FALSE", "I should not be HERE");
                    Toast.makeText(getActivity(), "Login failed: " + msg.obj, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
     });


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginPageBinding.inflate(inflater, container, false);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;

        if (getArguments() != null) {
            String username = getArguments().getString("username");
            String password = getArguments().getString("password");
            if (username != null && password != null) {
                binding.userName.setText(username);
                binding.passWord.setText(password);
            }
        }

        binding.btnLogin.setOnClickListener(view -> {
            String username = binding.userName.getText().toString();
            String password = binding.passWord.getText().toString();
            UserRepository repo = new UserRepository();
            repo.login(srv, handler, username, password);
        });

        binding.btnSwitchToSignUp.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_loginPage_to_signUpPage);
        });

        return binding.getRoot();
    }
}

/*
Toast.makeText(getActivity(), "Login Button clicked", Toast.LENGTH_SHORT).show();

            Token t = new Token(binding.userName.getText().toString(), LocalDateTime.now());
            LoginViewModel tokenModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
            tokenModel.setLoginToken(t);
 */