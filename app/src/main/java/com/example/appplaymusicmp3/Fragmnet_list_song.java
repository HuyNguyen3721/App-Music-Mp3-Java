package com.example.appplaymusicmp3;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appplaymusicmp3.adapter.Adapter_recycleList;
import com.example.appplaymusicmp3.databinding.LayoutListSongBinding;
import com.example.appplaymusicmp3.item.Item_song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Fragmnet_list_song extends Fragment implements Adapter_recycleList.IOnClickList {
    private LayoutListSongBinding binding;
    private ArrayList<Item_song> arrDataSongFull;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arrDataSongFull = new ArrayList<>();
        getDataFull("https://chiasenhac.vn/mp3/vietnam.html?tab=album-2020");
        for (int i = 2; i <= 10; i++) {
            getDataFull("https://chiasenhac.vn/mp3/vietnam.html?tab=album-2020&page=" + i);
        }
        binding = LayoutListSongBinding.inflate(inflater, container, false);
        //
        binding.rclList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        Adapter_recycleList adapter = new Adapter_recycleList();
        adapter.setiOnClickList(this);
        binding.rclList.setAdapter(adapter);
        //
        binding.btnPlayrandom.setOnClickListener(view -> {
            Random ran = new Random();
            int random = ran.nextInt(arrDataSongFull.size());
            ((MainActivity) getActivity()).fragment_playmusic(random, arrDataSongFull);
        });
        return binding.getRoot();
    }

    private void getDataFull(String url) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, Void, ArrayList<Item_song>> asyn = new AsyncTask<String, Void, ArrayList<Item_song>>() {
            @Override
            protected ArrayList<Item_song> doInBackground(String... strings) {
                String linkCrawl = strings[0];
                Document doc = null;
                try {
                    doc = Jsoup.connect(linkCrawl).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Item_song> arr = new ArrayList<>();
                if (doc != null) {
                    for (Element d : doc.select("div.row10px").select("div.col")) {
                        String link =
                                d.select("div.col").select("div.card ").select("div.card-header")
                                        .attr("style");
                        int sizes = link.split("\\(").length;
                        int index = link.split("\\(")[(sizes - 1)].lastIndexOf(')');
                        String linkImage = link.split("\\(")[(sizes - 1)].substring(0, index);
                        String name =
                                d.select("div.col").select("div.card-body").select("h3")
                                        .select("a").text();
                        String singer = "";
                        for (Element i : d.select("div.col").select("div.card-body").select("p")
                                .select("a")) {
                            singer += i.select("a").text();
                        }
                        String linkMusic = "https://vi.chiasenhac.vn" + d.select("div.col").select("div.card ").select("div.card-header")
                                .select("a").attr("href");
                        arr.add(new Item_song(linkImage, name, singer, linkMusic));
                    }
                }
                return arr;
            }

            @Override
            protected void onPostExecute(ArrayList<Item_song> item_songs) {
                arrDataSongFull.addAll(item_songs);
                Objects.requireNonNull(binding.rclList.getAdapter()).notifyDataSetChanged();
                binding.edtsearchSong.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        arrDataSongFull.clear();
                        for (Item_song i : item_songs) {
                            if (i.getSongName().startsWith(binding.edtsearchSong.getText().toString())) {
                                arrDataSongFull.add(i);
                            }
                        }
                        binding.rclList.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        };
        asyn.execute(url);
    }

    @Override
    public void onClickList(int pos) {
        ((MainActivity)getActivity()).fragment_playmusic(pos, arrDataSongFull);
    }

    @Override
    public int getCount() {
        return   arrDataSongFull.size();
    }

    @Override
    public Item_song getData(int pos) {
        return arrDataSongFull.get(pos);
    }
}
