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

    MangaClickListener listener;

    MangaViewModel viewModel;

    interface  MangaClickListener{
        public void mangaClicked(Manga manga);
    }

    public MangaAdapter(Context context) {
        this.context = context;

        viewModel = new ViewModelProvider((AppCompatActivity)context).get(MangaViewModel.class);

        this.mangaList = viewModel.getMangaList().getValue();
    }


    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root=  LayoutInflater.from(context).inflate(R.layout.manga_row,parent,false);
        MangaViewHolder holder = new MangaViewHolder(root);

        return holder;
    }


    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {

        Manga manga = mangaList.get(position);

        // holder.row.setText(person.getName() +  " " + person.getLastname());
        //holder.txtNumber.setText(person.getWorkPhone());

        //continue with interactions

        holder.row.setOnClickListener(v->{
            /*
            Toast.makeText(context, person.getName() +  " " + person.getLastname(),
                    Toast.LENGTH_SHORT).show();

                    */

            if(listener!=null){
                listener.mangaClicked(manga);
            }

            viewModel.setSelectedManga(manga);


        });


    }

    @Override
    public int getItemCount() {
        return viewModel.getMangaList().getValue().size();
    }

    public void setListener(MangaClickListener listener) {
        this.listener = listener;
    }


    class MangaViewHolder extends RecyclerView.ViewHolder{

        TextView txtManga;
        TextView txtAuthor;
        ImageView img;
        ConstraintLayout row;


        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            row = (ConstraintLayout) itemView;
            txtManga = itemView.findViewById(R.id.txtMangaName);
            txtAuthor = itemView.findViewById(R.id.txtAuthorName);
            // img = itemView.findViewById(R.id.);



        }
    }

}