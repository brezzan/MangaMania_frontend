package com.example.mangamania_app.adapter;

import static android.os.FileUtils.copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.mangamania_app.R;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.viewModel.MangaViewModel;


import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    Context context;
    List<Manga> mangaList;
    MangaViewModel mangaViewModel;
    NavController navController;

    public MangaAdapter(Context context, MangaViewModel mangaViewModel, NavController navController) {
        this.context = context;
        this.mangaViewModel = new ViewModelProvider((AppCompatActivity) context).get(MangaViewModel.class);
        this.navController = navController;
        mangaList = mangaViewModel.getMangaList().getValue();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.manga_row, parent, false);
        return new MangaViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        if (mangaList == null) {
            mangaList = mangaViewModel.getMangaList().getValue();
        }
        Manga manga = mangaList.get(position);

        holder.row.setOnClickListener(v -> {
            mangaViewModel.setSelectedManga(manga); // Set the selected manga
            navController.navigate(R.id.action_mainPage_to_mangaPage);
        });

        holder.txtManga.setText(manga.getTitleEn());
        holder.txtAuthor.setText(manga.getInformation().getAuthors().get(0).getName());

        String url_ = manga.getPictureUrl();

        Glide.with(context)
                .load(url_)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(R.drawable.black_image)
                .error(R.drawable.black_image)
                .centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (mangaList == null) {
            return 0;
        }
        return mangaList.size();
    }

    class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView txtManga;
        TextView txtAuthor;
        ImageView img;
        ConstraintLayout row;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            row = (ConstraintLayout) itemView;
            img = itemView.findViewById(R.id.imgManga);
            txtManga = itemView.findViewById(R.id.txtChapterName);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
        }
    }
}