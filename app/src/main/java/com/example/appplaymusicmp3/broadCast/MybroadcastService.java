package com.example.appplaymusicmp3.broadCast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.appplaymusicmp3.interfaceMusic.IclickNotification;
import com.example.appplaymusicmp3.item.Item_song;
import com.example.appplaymusicmp3.service.ServiceMusic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class MybroadcastService extends BroadcastReceiver {
    private ServiceMusic serviceMusic;

    public MybroadcastService(){
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "PLAYJ":
                if (serviceMusic != null) {
                    serviceMusic.getMediaPlayer().start();
                    serviceMusic.createNotification(
                            serviceMusic.getPos(),
                            serviceMusic.getArr(),
                            serviceMusic.getMediaPlayer().isPlaying()
                    );
                }
                break;
            case "PAUSEJ":
                if (serviceMusic != null && serviceMusic.getMediaPlayer().isPlaying()) {
                    serviceMusic.getMediaPlayer().pause();
                    serviceMusic.createNotification(
                            serviceMusic.getPos(),
                            serviceMusic.getArr(),
                            serviceMusic.getMediaPlayer().isPlaying()
                    );
                }
                break;
            case "BACKJ":
                if (serviceMusic != null) {
                    if (serviceMusic.getPos() - 1 < 0) {
                        serviceMusic.setPos(serviceMusic.getArr().size() - 1);
                    } else {
                        serviceMusic.setPos(serviceMusic.getPos() - 1);
                    }
                    getLinkMp3(serviceMusic.getPos(), serviceMusic.getArr());
                    serviceMusic.createNotification(
                            serviceMusic.getPos(),
                            serviceMusic.getArr(),
                            true
                    );
                }
                break;
            case "NEXTJ":
            case "AUTO_NEXTJ":
                if (serviceMusic != null) {
                    nextMusic();
                }
                break;
            default:
                break;
        }
    }

    private void nextMusic() {
        if (serviceMusic.getPos() + 1 > serviceMusic.getArr().size() - 1) {
            serviceMusic.setPos(0);
        } else {
            serviceMusic.setPos(serviceMusic.getPos() + 1);
        }
        getLinkMp3(serviceMusic.getPos(), serviceMusic.getArr());
        serviceMusic.createNotification(
                serviceMusic.getPos(),
                serviceMusic.getArr(),
                true
        );
    }

    private void getLinkMp3(int pos, ArrayList<Item_song> arr) {
        if (arr.get(pos).getLink_music().startsWith("https")) {
            @SuppressLint("StaticFieldLeak")
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
                    if (doc == null) {
                        return "";
                    } else {
                        return doc.select("ul.list-unstyled").select("li")
                                .select("a.download_item").get(1).attr("href");
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    serviceMusic.playMusicMp3(pos, arr, s);
                }
            };
            asyn.execute(arr.get(pos).getLink_music());
        } else {
            serviceMusic.playMusicMp3(pos, arr, arr.get(pos).getLink_music());
        }
    }
    public ServiceMusic getServiceMusic() {
        return serviceMusic;
    }

    public void setServiceMusic(ServiceMusic serviceMusic) {
        this.serviceMusic = serviceMusic;
    }
}
