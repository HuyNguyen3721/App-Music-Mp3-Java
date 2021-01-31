package com.example.appplaymusicmp3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusicmp3.databinding.LayoutItemAlbumBinding;
import com.example.appplaymusicmp3.item.Item_song;

public class Adapter_recycle_album extends  RecyclerView.Adapter<Adapter_recycle_album.ViewHolder> {
        private IOnClickAlbum inter ;
    public Adapter_recycle_album(IOnClickAlbum inter) {
        this.inter = inter;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutItemAlbumBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setData(inter.getDataAlbum(position));
        holder.itemView.setOnClickListener(view -> inter.onClickAlbum(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return inter.getCountItemAlbum();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        private  LayoutItemAlbumBinding binding;
        public ViewHolder(LayoutItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
   public interface IOnClickAlbum {
        void onClickAlbum(int position);
        int getCountItemAlbum();
        Item_song getDataAlbum(int position);
    }
}
