package com.example.mangamania_app.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangamania_app.R;
import com.example.mangamania_app.model.Manga;
import com.example.mangamania_app.viewModel.MangaViewModel;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    Context context;
    List<Manga> mangaList;
    MangaViewModel mangaViewModel;

    MangaClickListener listener;

    interface  MangaClickListener {
        void mangaClicked(Manga manga);
    }

    public MangaAdapter(Context context,MangaViewModel mangaViewModel) {
        this.context = context;
        this.mangaViewModel = new ViewModelProvider((AppCompatActivity)context).get(MangaViewModel.class);
        mangaList = mangaViewModel.getMangaList().getValue();
    }

    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root=  LayoutInflater.from(context).inflate(R.layout.manga_row, parent, false);

        return new MangaViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        if (mangaList == null) {
            mangaList = mangaViewModel.getMangaList().getValue();
        }
        Manga manga = mangaList.get(position);
        holder.txtManga.setText(manga.getTitleEn().toString());
        holder.txtAuthor.setText(manga.getInformation().getAuthors().get(0).getName().toString());

        holder.row.setOnClickListener(v -> {
            if (listener != null) {
                listener.mangaClicked(manga);
            }
            mangaViewModel.setSelectedManga(manga);
        });

        // Bind manga data to ViewHolder views here
    }

    @Override
    public int getItemCount() {
        if (mangaList == null) {
            return 0;
        }
        return mangaList.size();
    }

    public void setListener(MangaClickListener listener) {
        this.listener = listener;
    }

    class MangaViewHolder extends RecyclerView.ViewHolder {

        TextView txtManga;
        TextView txtAuthor;
        ImageView img;
        ConstraintLayout row;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            row = (ConstraintLayout) itemView;
            txtManga = itemView.findViewById(R.id.txtDate);
            txtAuthor = itemView.findViewById(R.id.txtName3);

            // img = itemView.findViewById(R.id.);
        }
    }
}