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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.mangamania_app.adapter.ChapterAdapter;
import com.example.mangamania_app.adapter.MangaAdapter;
import com.example.mangamania_app.databinding.FragmentMainPageBinding;
import com.example.mangamania_app.databinding.FragmentMangaPageBinding;
import com.example.mangamania_app.databinding.FragmentMangaPageBinding;

import com.example.mangamania_app.model.Chapter;
import com.example.mangamania_app.model.ErrorResponse;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.model.Token;
import com.example.mangamania_app.repo.ChapterRepository;
import com.example.mangamania_app.repo.MangaRepository;
import com.example.mangamania_app.repo.UserRepository;
import com.example.mangamania_app.viewModel.ChapterViewModel;
import com.example.mangamania_app.viewModel.LoginViewModel;
import com.example.mangamania_app.viewModel.MangaViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MangaPage extends Fragment {

    private FragmentMangaPageBinding binding;
    private LoginViewModel loginViewModel;
    private MangaViewModel mangaViewModel;
    private ChapterViewModel chapterViewModel;
    private ExecutorService srv;

    private Handler chapterHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                List<Chapter> response = (List<Chapter>) msg.obj;

                chapterViewModel.setChaptersList(response);

                return true;
            } else {
                Toast.makeText(getActivity(), "Could not get chapters: " + msg.obj, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    });


    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;
        binding = FragmentMangaPageBinding.inflate(inflater, container, false);
        mangaViewModel = new ViewModelProvider(requireActivity()).get(MangaViewModel.class); // Use activity scope
        chapterViewModel = new ViewModelProvider(requireActivity()).get(ChapterViewModel.class); // Use activity scope
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        ChapterRepository chapterRepo = new ChapterRepository();

        Manga selectedManga = mangaViewModel.getSelectedManga().getValue();

        if (selectedManga != null) {
            binding.commentPageText.setText(selectedManga.getTitleEn());
        } else {
            binding.commentPageText.setText("selected manga null");
        }

        String url_ = selectedManga.getPictureUrl();

        Glide.with(requireContext())
                .load(url_)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(R.drawable.black_image)
                .error(R.drawable.black_image)
                .centerCrop()
                .into(binding.imgManga2);

        chapterRepo.getChaptersOfManga(srv,chapterHandler,mangaViewModel.getSelectedManga().getValue().getMangaId().toString());
        Log.i("so far so god", "----------------------------------------------");

        //ArrayAdapter<Chapter> adp =new ArrayAdapter<>(getContext(), R.layout.manga_row,R.id.txtAuthor2,chapterViewModel.getChaptersList().getValue());

        chapterViewModel.getChaptersList().observe(getViewLifecycleOwner(), chapters -> {
            if (chapters == null || chapters.equals(new ArrayList<Chapter>())) {
                binding.commentPageText.setText("There are not any chapters for this manga");
            }
            else {

               ChapterAdapter adp = new ChapterAdapter(this.getContext(), chapterViewModel,navController);
                binding.chapterRecycler.setAdapter(adp);
                binding.chapterRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            }
        });

        return binding.getRoot();


//        mangaViewModel.getSelectedManga().observe(getViewLifecycleOwner(), manga -> {
//               binding.commentPageText.setText(manga.getTitleEn().toString());
//        });

        }
        /*
        WebApp app = (WebApp) requireActivity().getApplication();
        srv = app.srv;
        mangaViewModel = new ViewModelProvider(this).get(MangaViewModel.class);
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

        Manga selectedManga = mangaViewModel.getSelectedManga().getValue();

        ChapterRepository chapterRepo = new ChapterRepository();
        //chapterRepo.getChapterById(srv,chapterHandler,mangaViewModel.getSelectedManga().getValue().getMangaId().toString());

        binding = FragmentMangaPageBinding.inflate(inflater, container, false);
        binding.commentPageText.setText(selectedManga.getTitleEn().toString());

        //ArrayAdapter<Chapter> adp =new ArrayAdapter<>(getContext(), R.layout.manga_row,R.id.txtAuthor2,chapterViewModel.getChaptersList().getValue());


        //binding.chapterListView.setAdapter(adp);

//        binding.chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),
//                        chapterViewModel.getChaptersList().getValue().get(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        return binding.getRoot();
    }
    */

    }



