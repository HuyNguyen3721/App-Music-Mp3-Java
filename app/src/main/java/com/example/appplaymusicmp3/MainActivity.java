package com.example.appplaymusicmp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.appplaymusicmp3.databinding.ActivityMainBinding;
import com.example.appplaymusicmp3.item.Item_song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fragment_chosse_opption_music();
        if (getIntent().getParcelableArrayListExtra("Arr") != null) {
            fragment_playmusic(getIntent().getIntExtra("Pos", 0), getIntent().getParcelableArrayListExtra("Arr"));
        }
    }

    public void fragment_chosse_opption_music() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Fragment_chosse_option_music frag = new Fragment_chosse_option_music();
        tran.add(R.id.fragmentmain, frag);
        tran.addToBackStack(null).commit();
    }

    public void fragmentmaninOnline() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        FragmentMainOnline frag = new FragmentMainOnline();
        tran.replace(R.id.fragmentmain, frag);
        tran.addToBackStack(null).commit();
    }

    public void fragmentMainOffline() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        FragmentMainOffline frag = new FragmentMainOffline();
        tran.replace(R.id.fragmentmain, frag);
        tran.addToBackStack(null).commit();
    }

    public void fragmnet_list_song() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Fragmnet_list_song frag = new Fragmnet_list_song();
        tran.replace(R.id.fragmentmain, frag);
        tran.addToBackStack(null).commit();
    }

    public void fragment_playmusic(int pos, ArrayList<Item_song> arr) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Fragment_playmusic frag = new Fragment_playmusic(arr);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        frag.setArguments(bundle);
        tran.replace(R.id.fragmentmain, frag).addToBackStack(null).commit();
    }

    public void fragment_album(String url) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Fragment_ListAlbum frag = new Fragment_ListAlbum();
        Bundle bunble = new Bundle();
        bunble.putString("url", url);
        frag.setArguments(bunble);
        tran.replace(R.id.fragmentmain, frag).addToBackStack(null).commit();
    }

    public void fragment_albumlove(String url) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        Fragment_ListAlbumLove frag = new Fragment_ListAlbumLove();
        Bundle bunble = new Bundle();
        bunble.putString("url", url);
        frag.setArguments(bunble);
        tran.replace(R.id.fragmentmain, frag).addToBackStack(null).commit();
    }
}