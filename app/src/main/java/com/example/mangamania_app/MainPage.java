package com.example.mangamania_app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
                List<Manga> response = (List<Manga>) msg.obj;
                mangaViewModel.setMangaList(response);

                List<Manga> res = mangaViewModel.getMangaList().getValue();
                if (res == null || res.isEmpty() ) {
                    Toast.makeText(getActivity(), "From handle message : No mangas found", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "From handle message : "+ res.size(), Toast.LENGTH_SHORT).show();
                }

                return true;
            } else {
                Toast.makeText(getActivity(), "Could not get mangas: " + msg.obj, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    });

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;

        mangaViewModel = new ViewModelProvider(requireActivity()).get(MangaViewModel.class); // Use activity scope

        MangaRepository mangaRepo = new MangaRepository();
        mangaRepo.getManga(srv, mangahandler);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        binding = FragmentMainPageBinding.inflate(inflater, container, false);


        binding.btnLogout.setOnClickListener(view -> {
                String tokenString = loginViewModel.getLoginToken().getValue().getData().getToken();
                UserRepository repo = new UserRepository();
                repo.logout(srv, logoutHandler, tokenString);
                //navController.navigate(R.id.action_mainPage_to_mangaPage);
        });


        // mainpage den profile gidiyor.
        binding.btnProfilePage.setOnClickListener(view -> {

            navController.navigate(R.id.action_mainPage_to_profilePage);
        });

        mangaViewModel.getMangaList().observe(getViewLifecycleOwner(), mangas -> {
            if (mangas == null || mangas.isEmpty()) {
                binding.mainAppPageText.setText("0");
            } else {

                MangaAdapter adp = new MangaAdapter(this.getContext(), mangaViewModel,navController);
                binding.mangaRecycler.setAdapter(adp);
                binding.mangaRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

            }
        });



        return binding.getRoot();
    }

}

