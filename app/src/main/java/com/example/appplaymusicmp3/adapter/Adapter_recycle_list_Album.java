package com.example.appplaymusicmp3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusicmp3.databinding.LayoutItemListAlbumBinding;
import com.example.appplaymusicmp3.item.Item_song;

public class Adapter_recycle_list_Album extends RecyclerView.Adapter<Adapter_recycle_list_Album.Viewholer>{
    private IOnClickItem inter ;
    public Adapter_recycle_list_Album(IOnClickItem inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholer(LayoutItemListAlbumBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholer holder, int position) {
            holder.binding.setData(inter.getDataListAlbum(position));
            holder.itemView.setOnClickListener(view -> inter.onclickListAlbum(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return inter.getCountListAlbum();
    }

    public static  class Viewholer extends RecyclerView.ViewHolder{
        private LayoutItemListAlbumBinding binding ;
        public Viewholer(LayoutItemListAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface IOnClickItem{
        void onclickListAlbum(int pos);
        Item_song getDataListAlbum(int pos);
        int getCountListAlbum();
    }
}
