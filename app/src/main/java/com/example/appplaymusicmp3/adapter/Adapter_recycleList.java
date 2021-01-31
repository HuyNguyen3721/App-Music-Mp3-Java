package com.example.appplaymusicmp3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusicmp3.item.Item_song;
import com.example.appplaymusicmp3.databinding.LayoutItemSongBinding;

public class Adapter_recycleList extends RecyclerView.Adapter<Adapter_recycleList.ViewHolder>  {
    private  IOnClickList iOnClickList ;
    public void setiOnClickList(IOnClickList iOnClickList) {
        this.iOnClickList = iOnClickList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemSongBinding binding  = LayoutItemSongBinding.inflate(LayoutInflater.from(parent.getContext()),parent ,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.setData(iOnClickList.getData(position));
            holder.itemView.setOnClickListener(view -> {
            iOnClickList.onClickList(holder.getAdapterPosition());
            });
    }

    @Override
    public int getItemCount() {
        return iOnClickList.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private  LayoutItemSongBinding binding ;
        public ViewHolder(LayoutItemSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

   public  interface IOnClickList{
        void onClickList(int pos);
        int getCount() ;
        Item_song getData(int pos);
    }
}
