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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.mangamania_app.databinding.FragmentSignUpPageBinding;
import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.repo.UserRepository;

import java.util.concurrent.ExecutorService;


public class SignUpPage extends Fragment {

   private FragmentSignUpPageBinding binding;
    private ExecutorService srv;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.i("CORRECT", "inside handleMessage"+ msg.what);
            if (msg.what == 1) {
                Log.i("CORRECT", "I should be HERE");
                ErrorResponse<String> response = (ErrorResponse<String>) msg.obj;
                if ("OK".equals(response.getStatus())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", binding.newUserName.getText().toString());
                    bundle.putString("password", binding.newPassWord.getText().toString());

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                    navController.navigate(R.id.action_signUpPage_to_loginPage2,bundle);

                } else {
                    Toast.makeText(getActivity(), "Sign up failed: " + msg.obj, Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(getActivity(), "Sign up failed: " + msg.obj, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignUpPageBinding.inflate(getLayoutInflater());
        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;

        binding.btnSignUp.setOnClickListener(view -> {
            String username = binding.newUserName.getText().toString();
            String mail = binding.newMail.getText().toString();
            String password = binding.newPassWord.getText().toString();
            UserRepository repo = new UserRepository();
            repo.register(srv,handler,username,mail,password);

        });


        binding.btnSwitchToLogin.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_signUpPage_to_loginPage2);

        });


        return binding.getRoot();

    }
}