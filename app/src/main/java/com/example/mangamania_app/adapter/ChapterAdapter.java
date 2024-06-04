package com.example.mangamania_app.adapter;

import static android.os.FileUtils.copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.mangamania_app.R;
import com.example.mangamania_app.model.Chapter;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.viewModel.ChapterViewModel;
import com.example.mangamania_app.viewModel.MangaViewModel;


import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    Context context;
    Manga selectedManga;
    List<Chapter> chapterList;
    ChapterViewModel chapterViewModel;
    ChapterClickListener listener;

    NavController navController;

    MangaViewModel mangaViewModel;

    interface  ChapterClickListener {
        void chapterClicked(Chapter chapter);
    }


    public ChapterAdapter(Context context,ChapterViewModel chapterViewModel, NavController navController) {
        this.context = context;
        this.navController = navController;
        this.mangaViewModel = new ViewModelProvider((AppCompatActivity)context).get(MangaViewModel.class);
        this.selectedManga = mangaViewModel.getSelectedManga().getValue();

        this.chapterViewModel = new ViewModelProvider((AppCompatActivity)context).get(ChapterViewModel.class);
        chapterList = chapterViewModel.getChaptersList().getValue();
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root=  LayoutInflater.from(context).inflate(R.layout.chapter_row, parent, false);

        return new ChapterAdapter.ChapterViewHolder(root);
    }

    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder holder, int position) {
        if (chapterList == null) {
            chapterList = chapterViewModel.getChaptersList().getValue();
        }

        if (chapterList == null) {
            chapterList = chapterViewModel.getChaptersList().getValue();

            Chapter chapter = chapterList.get(position);

            holder.chapterRow.setOnClickListener(v -> {
                chapterViewModel.setSelectedChapter(chapter);
                navController.navigate(R.id.action_mangaPage_to_commentsPage);
            });

            holder.txtChapterName.setText(chapter.getChapterName().toString());

            holder.chapterRow.setOnClickListener(v->{

                Toast.makeText(context, chapterList.get(position).getChapterName(), Toast.LENGTH_SHORT).show();


            });
        }

}

    @Override
    public int getItemCount() {
        if (chapterList == null) {
            return 0;
        }
        return chapterList.size();
    }

    public void setListener(ChapterClickListener listener) {
        this.listener = listener;
    }


    class ChapterViewHolder extends RecyclerView.ViewHolder {

        TextView txtChapterName;
        ConstraintLayout chapterRow;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterRow = (ConstraintLayout) itemView;
            txtChapterName = itemView.findViewById(R.id.txtChapterName);

            // img = itemView.findViewById(R.id.);
        }
    }
}
