package com.example.appplaymusicmp3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appplaymusicmp3.item.Item_song;
import com.example.appplaymusicmp3.adapter.Adapter_recycleList;
import com.example.appplaymusicmp3.databinding.LayoutMainOfflineBinding;

import java.util.ArrayList;
import java.util.Random;

public class FragmentMainOffline extends Fragment implements Adapter_recycleList.IOnClickList {
    private LayoutMainOfflineBinding binding;
    private ArrayList<Item_song> arr ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        arr =  new ArrayList<>();
        binding = LayoutMainOfflineBinding.inflate(inflater, container, false);
        binding.rclList.setLayoutManager( new LinearLayoutManager(binding.rclList.getContext()));
        Adapter_recycleList adapter_recycleList =  new Adapter_recycleList();
        adapter_recycleList.setiOnClickList(this);
        binding.rclList.setAdapter(adapter_recycleList);
        binding.btnPlayrandom.setOnClickListener(view -> {
            Random ran = new Random();
            int random =  ran.nextInt(arr.size());
            ((MainActivity)getActivity()).fragment_playmusic(random, arr);
        });
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void getDataMusicOffline() {
        ArrayList<Item_song> result = new ArrayList<>();
        arr.clear();
//        uri dia chi cua bang
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ;
        Cursor cursor = getContext().getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String path = cursor.getString(
                    cursor.getColumnIndex("_data")
            );
            String[] arrname = path.split("/");
            String name = arrname[arrname.length - 1];
            result.add(
                   new Item_song(
                            "https://media.idownloadblog.com/wp-content/uploads/2018/03/Apple-Music-icon-003.jpg",
                            name,
                            "",
                            path
                    )
            );
            cursor.moveToNext();
        }
        cursor.close();
        arr.addAll(result);
        binding.txtTotalSongs.setText(arr.size()+"");
        binding.rclList.getAdapter().notifyDataSetChanged();
        binding.edtsearchSong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                arr.clear();
                for (Item_song i  :  result) {
                    if (i.getSongName().startsWith(binding.edtsearchSong.getText().toString())) {
                        arr.add(i);
                    }
                }
                binding.rclList.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDataMusicOffline();
        } else {
            Toast.makeText(getContext(), "Bạn không cấp quyền truy cập", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClickList(int pos) {
        ((MainActivity) getActivity()).fragment_playmusic(pos,arr);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Item_song getData(int pos) {
        return arr.get(pos);
    }
}
