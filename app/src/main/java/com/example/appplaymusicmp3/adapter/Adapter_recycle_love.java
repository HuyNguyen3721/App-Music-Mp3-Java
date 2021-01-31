package com.example.appplaymusicmp3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusicmp3.databinding.LayoutItemAlbumBinding;
import com.example.appplaymusicmp3.item.Item_song;

public class Adapter_recycle_love extends RecyclerView.Adapter<Adapter_recycle_love.ViewHolder> {
    public IOnClickLove inter;

    public Adapter_recycle_love(IOnClickLove inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutItemAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setData(inter.getDataLove(position));
        holder.itemView.setOnClickListener(view -> inter.onClickLove(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return inter.getCountItemLove();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemAlbumBinding binding;

        public ViewHolder(LayoutItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface IOnClickLove {
        void onClickLove(int pos);

        int getCountItemLove();

        Item_song getDataLove(int position);
    }
}
