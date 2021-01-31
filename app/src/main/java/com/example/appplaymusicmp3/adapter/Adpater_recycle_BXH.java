package com.example.appplaymusicmp3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusicmp3.databinding.LayoutItemBxhBinding;
import com.example.appplaymusicmp3.item.Item_BXH;

public class Adpater_recycle_BXH extends RecyclerView.Adapter<Adpater_recycle_BXH.ViewHolder> {
    private IOnClickBXH inter;

    public Adpater_recycle_BXH(IOnClickBXH inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutItemBxhBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setData(inter.getDataBXH(position));
        holder.itemView.setOnClickListener(view -> inter.onClickBXH(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return inter.getCountBXH();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemBxhBinding binding;

        public ViewHolder(LayoutItemBxhBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface IOnClickBXH {
        void onClickBXH(int pos);

        int getCountBXH();

        Item_BXH getDataBXH(int pos);
    }
}
