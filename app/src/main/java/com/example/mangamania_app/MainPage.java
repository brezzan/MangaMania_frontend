package com.example.mangamania_app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mangamania_app.adapter.MangaAdapter;
import com.example.mangamania_app.databinding.FragmentMainPageBinding;
import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.model.Token;
import com.example.mangamania_app.repo.MangaRepository;
import com.example.mangamania_app.repo.UserRepository;
import com.example.mangamania_app.viewModel.LoginViewModel;
import com.example.mangamania_app.viewModel.MangaViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;


public class MainPage extends Fragment {

    private FragmentMainPageBinding binding;
    private LoginViewModel loginViewModel;
    private MangaViewModel mangaViewModel;
    private ExecutorService srv;
    private Handler logoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getActivity(), "Logout successful", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigate(R.id.action_mainPage_to_profilePage);
            } else {
                Toast.makeText(getActivity(), "Logout failed: " + msg.obj, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });

    private Handler mangahandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                List<Manga> response= (List<Manga>) msg.obj;
                if ( !response.isEmpty()) {

                    mangaViewModel.setMangaList(response);
                    Toast.makeText(getActivity(), "manga count = " + mangaViewModel.getMangaList().getValue().size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "MANGAS COULD NOT UPLOAD: " , Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(),  "Could not get mangas: " + msg.obj, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mangaViewModel = new ViewModelProvider(requireActivity()).get(MangaViewModel.class);

        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;

        MangaRepository mangaRepo = new MangaRepository();
        mangaRepo.getManga(srv,mangahandler);

/*
// manga list of manga_row
        MangaAdapter adp = new MangaAdapter(this.getContext());

        binding.mangaRecycler.setAdapter(adp);


        binding.mangaRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mangaViewModel.getSelectedManga().observe(this.getActivity(),manga->{

            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_mainPage_to_mangaPage);

        });
*/


        binding.btnLogout.setOnClickListener(view -> {

            String tokenString  = loginViewModel.getLoginToken().getValue().getData().getToken();

            Log.i("token",tokenString);
            UserRepository repo = new UserRepository();
            repo.logout(srv, logoutHandler, tokenString);
        });




        return binding.getRoot();
    }
}
