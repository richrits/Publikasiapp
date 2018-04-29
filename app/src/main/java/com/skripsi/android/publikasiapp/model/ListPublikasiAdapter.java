package com.skripsi.android.publikasiapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skripsi.android.publikasiapp.R;

import java.util.List;

public class ListPublikasiAdapter extends RecyclerView.Adapter<ListPublikasiAdapter.ViewHolder>{
private Context context;
private List<Publikasi> publikasiList;

    public ListPublikasiAdapter(Context context, List<Publikasi> publikasiList) {
        this.context = context;
        this.publikasiList = publikasiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_publikasi_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(publikasiList.get(position).getTitle());
        Glide.with(context).load(publikasiList.get(position).getCover()).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return publikasiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView cover;

        public ViewHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            cover = (ImageView) itemView.findViewById(R.id.cover);
        }
    }
}

