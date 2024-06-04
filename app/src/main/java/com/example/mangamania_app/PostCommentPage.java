package com.example.mangamania_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mangamania_app.databinding.FragmentPostCommentPageBinding;


public class PostCommentPage extends Fragment {

    private FragmentPostCommentPageBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostCommentPageBinding.inflate(getLayoutInflater());

        binding.cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_postCommentPage_to_commentsPage);
        });

        //missing actually adding the post into comments page rn it functions the same as cancel button
        binding.postButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_postCommentPage_to_commentsPage);
        });
        return binding.getRoot();
    }
}