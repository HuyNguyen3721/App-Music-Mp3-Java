package com.example.appplaymusicmp3;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.appplaymusicmp3.adapter.Adapter_recycle_list_Album;
import com.example.appplaymusicmp3.databinding.LayoutListalbumBinding;
import com.example.appplaymusicmp3.item.Item_detail_album;
import com.example.appplaymusicmp3.item.Item_song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Fragment_ListAlbum extends Fragment implements Adapter_recycle_list_Album.IOnClickItem {
    private LayoutListalbumBinding binding;
    private ArrayList<Item_song> arrListItemAlbum;
    private String urlImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arrListItemAlbum = new ArrayList<>();
        if (getArguments() != null) {
            loadDataAlbum(getArguments().getString("url"));
            loadRclView(getArguments().getString("url"));
        }
        binding = LayoutListalbumBinding.inflate(inflater, container, false);
        Adapter_recycle_list_Album adapter = new Adapter_recycle_list_Album(this);
        binding.rclListAlbum.setLayoutManager(new LinearLayoutManager(binding.rclListAlbum.getContext()));
        binding.rclListAlbum.setAdapter(adapter);
        return binding.getRoot();
    }

    private void loadDataAlbum(String url) {
        ArrayList<String> arr = new ArrayList<>();
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String , Void , Item_detail_album> asyn  = new AsyncTask<String , Void , Item_detail_album>() {
            @Override
            protected Item_detail_album doInBackground(String... strings) {
                String linkCrawl = strings[0];
                Document doc = null;
                try {
                    doc = Jsoup.connect(linkCrawl).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements d = doc.select("div.col-md-4").select("div.card-details");
                String linkImage = d.select("div#companion_cover").select("img").attr("src");
                String nameSong = d.select("div.card-body").select("h2.card-title").text();
                for(Element i  : d.select("div.card-body").select("ul.list-unstyled").select("li")){
                    arr.add(i.select("a").text());
                }
                return new Item_detail_album(linkImage,nameSong,arr.get(0),arr.get(1));
            }

            @Override
            protected void onPostExecute(Item_detail_album item_detail_album) {
                Glide.with(binding.imageAlbum.getContext()).load(item_detail_album.getLinkImage()).into(binding.imageAlbum);
                urlImage = item_detail_album.getLinkImage();
                binding.txtnameSong.setText(item_detail_album.getName());
                binding.txtsingerName.setText(item_detail_album.getSingerName());
                binding.txtCreater.setText(item_detail_album.getCreatorName());
                binding.notifyChange();
            }
        };
        asyn.execute(url);
    }

    private void loadRclView(String url) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String , Void ,  ArrayList<Item_song>> asyn = new AsyncTask<String , Void ,  ArrayList<Item_song>>() {

            @Override
            protected ArrayList<Item_song> doInBackground(String... strings) {
                String linkCrawl  =  strings[0];
                Document doc  = null;
                try {
                    doc = Jsoup.connect(linkCrawl).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Item_song> arr = new ArrayList<>();
                for( Element d  :  doc.select("div.card-footer")){
                    String nameSong  =  d.select("div.name").select("a").text();
                    String singerName  =  d.select("div.author").select("div.author-ellepsis").select("a").text();
                    String linkMusic =   d.select("div.name").select("a").attr("href");
                    arr.add(new Item_song(urlImage,nameSong,singerName,linkMusic));
                }
                return arr;
            }

            @Override
            protected void onPostExecute(ArrayList<Item_song> item_songs) {
                arrListItemAlbum.addAll(item_songs);
                Objects.requireNonNull(binding.rclListAlbum.getAdapter()).notifyDataSetChanged();
            }
        };
        asyn.execute(url);
    }

    @Override
    public void onclickListAlbum(int pos) {
        ((MainActivity)getActivity()).fragment_playmusic(pos,arrListItemAlbum);
    }

    @Override
    public Item_song getDataListAlbum(int pos) {
        return arrListItemAlbum.get(pos);
    }

    @Override
    public int getCountListAlbum() {
        return arrListItemAlbum.size();
    }
}
