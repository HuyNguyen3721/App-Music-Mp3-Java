package com.example.appplaymusicmp3;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appplaymusicmp3.broadCast.MybroadcastFragmentplay;
import com.example.appplaymusicmp3.databinding.LayoutPlaymusicBinding;
import com.example.appplaymusicmp3.interfaceMusic.IclickNotification;
import com.example.appplaymusicmp3.item.Item_song;
import com.example.appplaymusicmp3.service.ServiceMusic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fragment_playmusic extends Fragment implements View.OnClickListener, IclickNotification {
    private LayoutPlaymusicBinding binding;
    private ArrayList<Item_song> arr;
    private ServiceMusic service;
    private int pos;
    private MybroadcastFragmentplay mybroadcast;

    public Fragment_playmusic(ArrayList<Item_song> arr) {
        this.arr = arr;
    }

    public Fragment_playmusic() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pos = getArguments().getInt("pos");
        openServiceUnBound();
        connection();
        binding = LayoutPlaymusicBinding.inflate(inflater, container, false);
        binding.setDatamusic(arr.get(pos));
        binding.ivplay.setImageResource(R.drawable.pause);
        binding.circleiv.startAnimation(
                AnimationUtils.loadAnimation(
                        binding.circleiv.getContext(),
                        R.anim.iv_circle
                )
        );
        return binding.getRoot();
    }

    private void connection() {
        //yeu cau ket noi
        Intent intent = new Intent(getContext(), ServiceMusic.class);
        ServiceConnection conection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = ((ServiceMusic.Mybinder) iBinder).getServiceMusic();
                getLinkMp3();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        getContext().bindService(intent, conection, Context.BIND_AUTO_CREATE);
    }

    private void openServiceUnBound() {
        Intent intent = new Intent(getContext(), ServiceMusic.class);
        intent.setClass(getContext(), ServiceMusic.class);
        //bat service unbound
        //moi lan goi startService thi chac chan vao onStartCommand
        getContext().startService(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private void getLinkMp3() {
        service.setFragment_playmusic(this);
        if (arr.get(pos).getLink_music().startsWith("https")) {
            AsyncTask<String, Void, String> asyn = new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... strings) {
                    String linkCrawl = strings[0];
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(linkCrawl).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return doc.select("ul.list-unstyled").select("li")
                            .select("a.download_item").get(1).attr("href");
                }

                @Override
                protected void onPostExecute(String s) {
                    service.playMusicMp3(pos, arr, s);
                }
            };
            asyn.execute(arr.get(pos).getLink_music());
        } else {
            service.playMusicMp3(pos, arr, arr.get(pos).getLink_music());
        }
    }
    @SuppressLint("SimpleDateFormat")
    public void musicPrepare() {
        SimpleDateFormat simpledataformat = new SimpleDateFormat("mm:ss");
            binding.txtTotalTime.setText(simpledataformat.format(service.getMediaPlayer().getDuration()));
            binding.seekBarMusic.setMax(service.getMediaPlayer().getDuration());
            binding.ivplay.setImageResource(R.drawable.pause);
            clickplaymusic();
            clickseekbar();
            updateTimeSong();
    }

    private void updateTimeSong() {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpledataformat = new SimpleDateFormat("mm:ss");
                binding.txtTimeMusic.setText(simpledataformat.format(service.getMediaPlayer().getCurrentPosition()));
                binding.seekBarMusic.setProgress(service.getMediaPlayer().getCurrentPosition());
                handle.postDelayed(this, 200);
            }
        }, 100);
    }

    private void clickseekbar() {
        binding.seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                service.getMediaPlayer().seekTo(binding.seekBarMusic.getProgress());
            }
        });

    }

    private void clickplaymusic() {
        binding.ivplay.setOnClickListener(this);
        binding.ivnext.setOnClickListener(this);
        binding.ivback.setOnClickListener(this);
    }

    private void rigisterBroadcast() {
        mybroadcast = new MybroadcastFragmentplay();
        mybroadcast.setInter(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction("BACKJ");
        intent.addAction("NEXTJ");
        intent.addAction("PLAYJ");
        intent.addAction("PAUSEJ");
        intent.addAction("AUTO_NEXTJ");
        getContext().registerReceiver(mybroadcast, intent);
    }

    @Override
    public void onStart() {
        rigisterBroadcast();
        super.onStart();
        //
        if (service != null) {
            arr = service.getArr();
            pos = service.getPos();
            binding.setDatamusic(arr.get(pos));
            if (service.getMediaPlayer().isPlaying()) {
                binding.ivplay.setImageResource(R.drawable.pause);
            } else {
                binding.ivplay.setImageResource(R.drawable.play);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivplay:
                if (service.getMediaPlayer().isPlaying()) {
                    binding.ivplay.setImageResource(R.drawable.play);
                    service.getMediaPlayer().pause();
                } else {
                    binding.ivplay.setImageResource(R.drawable.pause);
                    service.getMediaPlayer().start();
                }
                service.createNotification(pos, arr, service.getMediaPlayer().isPlaying());
                break;
            case R.id.ivnext:
                if (pos + 1 > arr.size() - 1) {
                    pos = 0;
                } else {
                    pos += 1;
                }
                binding.setDatamusic(arr.get(pos));
                getLinkMp3();
                break;
            case R.id.ivback:
                if (pos - 1 < 0) {
                    pos = arr.size() - 1;
                } else {
                    pos -= 1;
                }
                binding.setDatamusic(arr.get(pos));
                getLinkMp3();
                break;
            default:
                break;
        }
    }

    //
    @Override
    public void onMNext() {
        if (pos + 1 > arr.size() - 1) {
            pos = 0;
        } else {
            pos += 1;
        }
        binding.setDatamusic(arr.get(pos));
        binding.ivplay.setImageResource(R.drawable.pause);
    }

    @Override
    public void onMPause() {
        binding.ivplay.setImageResource(R.drawable.play);
    }

    @Override
    public void onMPlay() {
        binding.ivplay.setImageResource(R.drawable.pause);
    }

    @Override
    public void onMBack() {
        if (pos - 1 < 0) {
            pos = arr.size() - 1;
        } else {
            pos -= 1;
        }
        binding.setDatamusic(arr.get(pos));
        binding.ivplay.setImageResource(R.drawable.pause);
    }

    @Override
    public void onStop() {
        getContext().unregisterReceiver(mybroadcast);
        super.onStop();
    }
}
