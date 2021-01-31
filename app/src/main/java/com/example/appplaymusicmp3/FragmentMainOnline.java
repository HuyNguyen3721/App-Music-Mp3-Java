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

import com.example.appplaymusicmp3.adapter.Adapter_recycleList;
import com.example.appplaymusicmp3.adapter.Adapter_recycle_album;
import com.example.appplaymusicmp3.adapter.Adapter_recycle_love;
import com.example.appplaymusicmp3.adapter.Adpater_recycle_BXH;
import com.example.appplaymusicmp3.databinding.LayoutMainOnlineBinding;
import com.example.appplaymusicmp3.item.Data_object;
import com.example.appplaymusicmp3.item.Item_BXH;
import com.example.appplaymusicmp3.item.Item_song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentMainOnline extends Fragment implements Adapter_recycle_love.IOnClickLove,
        Adpater_recycle_BXH.IOnClickBXH,
        Adapter_recycle_album.IOnClickAlbum, Adapter_recycleList.IOnClickList {
    private ArrayList<Item_song> arrlove;
    private ArrayList<Item_song> arralbum;
    private ArrayList<Item_song> arrlistsong;
    private ArrayList<Item_BXH> arrbxh;
    private LayoutMainOnlineBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arralbum = new ArrayList<>();
        arrlove = new ArrayList<>();
        arrbxh = new ArrayList<>();
        arrlistsong = new ArrayList<>();
        loadData();
        binding = LayoutMainOnlineBinding.inflate(inflater, container, false);
        //
        binding.rclULOVE.setLayoutManager(new LinearLayoutManager(binding.rclULOVE.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Adapter_recycle_love adapterlove = new Adapter_recycle_love(this);
        binding.rclULOVE.setAdapter(adapterlove);
        //
        binding.rclAlbum.setLayoutManager(new LinearLayoutManager(binding.rclAlbum.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Adapter_recycle_album adapteralbum = new Adapter_recycle_album(this);
        binding.rclAlbum.setAdapter(adapteralbum);
        //
        binding.rclBXH.setLayoutManager(new LinearLayoutManager(binding.rclBXH.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Adpater_recycle_BXH adapterbxh = new Adpater_recycle_BXH(this);
        binding.rclBXH.setAdapter(adapterbxh);
        //
        binding.rclListSong.setLayoutManager(new LinearLayoutManager(binding.rclBXH.getContext()));
        Adapter_recycleList adapterlistsong = new Adapter_recycleList();
        adapterlistsong.setiOnClickList(this);
        binding.rclListSong.setAdapter(adapterlistsong);
        //
        binding.txtMore.setOnClickListener(view -> {
            ((MainActivity) getActivity()).fragmnet_list_song();
        });
        return binding.getRoot();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        AsyncTask<String, Void, Data_object> asyn = new AsyncTask<String, Void, Data_object>() {
            @Override
            protected Data_object doInBackground(String... strings) {
                String linkCrawl = strings[0];
                Document doc = null;
                try {
                    doc = Jsoup.connect(linkCrawl).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Item_song> arr = new ArrayList<>();
                ArrayList<Item_song> arrlist = new ArrayList<>();
                ArrayList<Item_BXH> arrbxh = new ArrayList<>();
                arrbxh.add(new Item_BXH("https://data.chiasenhac.com/imgs/bxh/BXHNhacVietNam_245x140.png"));
                arrbxh.add(new Item_BXH("https://data.chiasenhac.com/imgs/bxh/BXHVideo_245x140.png"));
                arrbxh.add(new Item_BXH("https://data.chiasenhac.com/imgs/bxh/BXHNhacUs-UK_245x140.png"));
                arrbxh.add(new Item_BXH("https://data.chiasenhac.com/imgs/bxh/BXHNhacHoa_245x140.png"));
                arrbxh.add(new Item_BXH("https://data.chiasenhac.com/imgs/bxh/BXHNhacHan_245x140.png"));
                //
                if (doc != null) {
                    for (Element d : doc.select("div.tab-content").select("div.tab-pane")
                            .select("ul.list-unstyled").select("li.media")) {
                        String name =
                                d.select("li.media").select("div.media-left").select("a").attr("title");
                        String linkImg =
                                d.select("li.media").select("div.media-left").select("a").select("img")
                                        .attr("src");
                        String linkMusic =
                                "https://vi.chiasenhac.vn" + d.select("li.media").select("div.media-left")
                                        .select("a").attr("href");
                        StringBuilder songer = new StringBuilder();
                        for (Element i : d.select("li.media").select("div.media-body")
                                .select("div.align-items-center").select("div.author")) {
                            songer.append(i.select("a").text());
                        }
                        arrlist.add(new Item_song(linkImg, name, songer.toString(), linkMusic));
                    }
                    //
                    for (Element d : doc.select("div.col-md-9").select("div.row10px").select("div.col")) {
                        try {
                            String link = d.select("div.col").select("div.card").select("div.card-header")
                                    .attr("style");
                            int index = link.indexOf('(');
                            int index2 = link.lastIndexOf(')');
                            String linkImage = link.substring(index + 1, index2);
                            String name = d.select("div.col").select("div.card").select("div.card-body")
                                    .select("h3").select("a").text();
                            String singer = "";
                            for (Element i : d.select("div.col").select("div.card-body").select("p")
                                    .select("a")) {
                                singer += i.select("a").text();
                            }
                            String linkMusic =
                                    "https://vi.chiasenhac.vn" + d.select("div.col").select("div.card ")
                                            .select("div.card-header").select("a").attr("href");
                            arr.add(new Item_song(linkImage, name, singer, linkMusic));
                            if (arr.size() == 10) {
                                break;
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                }
                return new Data_object(arr, arrbxh, arrlist);
            }

            @Override
            protected void onPostExecute(Data_object data_object) {
                for (int i = 0; i < 5; i++) {
                    arralbum.add(data_object.getArr().get(i));
                }
                for (int i = 5; i < 10; i++) {
                    arrlove.add(data_object.getArr().get(i));
                }
                arrlistsong.addAll(data_object.getArrlist());
                arrbxh.addAll(data_object.getArrBXH());
                Objects.requireNonNull(binding.rclULOVE.getAdapter()).notifyDataSetChanged();
                Objects.requireNonNull(binding.rclListSong.getAdapter()).notifyDataSetChanged();
                Objects.requireNonNull(binding.rclAlbum.getAdapter()).notifyDataSetChanged();
                Objects.requireNonNull(binding.rclBXH.getAdapter()).notifyDataSetChanged();
            }
        };
        asyn.execute("https://chiasenhac.vn/");
    }

    @Override
    public void onClickList(int pos) {
        ((MainActivity) getActivity()).fragment_playmusic(pos, arrlistsong);
    }

    @Override
    public int getCount() {
        return arrlistsong.size();
    }

    @Override
    public Item_song getData(int pos) {
        return arrlistsong.get(pos);
    }

    @Override
    public void onClickAlbum(int position) {
        ((MainActivity) getActivity()).fragment_album(arralbum.get(position).getLink_music());
    }

    @Override
    public int getCountItemAlbum() {
        return arralbum.size();
    }

    @Override
    public Item_song getDataAlbum(int position) {
        return arralbum.get(position);
    }

    @Override
    public void onClickLove(int pos) {
        ((MainActivity) getActivity()).fragment_albumlove(arrlove.get(pos).getLink_music());
    }

    @Override
    public int getCountItemLove() {
        return arrlove.size();
    }

    @Override
    public Item_song getDataLove(int position) {
        return arrlove.get(position);
    }

    @Override
    public void onClickBXH(int pos) {

    }

    @Override
    public int getCountBXH() {
        return arrbxh.size();
    }

    @Override
    public Item_BXH getDataBXH(int pos) {
        return arrbxh.get(pos);
    }

    public ArrayList<Item_song> getArrlove() {
        return arrlove;
    }

    public void setArrlove(ArrayList<Item_song> arrlove) {
        this.arrlove = arrlove;
    }

    public ArrayList<Item_song> getArralbum() {
        return arralbum;
    }

    public void setArralbum(ArrayList<Item_song> arralbum) {
        this.arralbum = arralbum;
    }

    public ArrayList<Item_song> getArrlistsong() {
        return arrlistsong;
    }

    public void setArrlistsong(ArrayList<Item_song> arrlistsong) {
        this.arrlistsong = arrlistsong;
    }

    public ArrayList<Item_BXH> getArrbxh() {
        return arrbxh;
    }

    public void setArrbxh(ArrayList<Item_BXH> arrbxh) {
        this.arrbxh = arrbxh;
    }
}
