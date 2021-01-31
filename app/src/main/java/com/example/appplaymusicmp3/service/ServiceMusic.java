package com.example.appplaymusicmp3.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.appplaymusicmp3.Fragment_playmusic;
import com.example.appplaymusicmp3.MainActivity;
import com.example.appplaymusicmp3.R;
import com.example.appplaymusicmp3.broadCast.MybroadcastService;
import com.example.appplaymusicmp3.item.Item_song;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceMusic extends Service {
    private MediaPlayer mediaPlayer;
    private int pos;
    private ArrayList<Item_song> arr;
    private MybroadcastService mybroadcastService;
    private NotificationManager notificationManager;
    private Fragment_playmusic fragment_playmusic;
    //
    public static class Mybinder extends Binder {
        private final ServiceMusic serviceMusic;

        public Mybinder(ServiceMusic serviceMusic) {
            this.serviceMusic = serviceMusic;
        }

        public ServiceMusic getServiceMusic() {
            return serviceMusic;
        }
    }

    // service tra ve du lieu dang binder
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Mybinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mybroadcastService ==null){
            mybroadcastService = new MybroadcastService();
            mybroadcastService.setServiceMusic(this);
        }
        IntentFilter intentfiler = new IntentFilter();
        intentfiler.addAction("BACKJ");
        intentfiler.addAction("NEXTJ");
        intentfiler.addAction("PLAYJ");
        intentfiler.addAction("PAUSEJ");
        intentfiler.addAction("AUTO_NEXTJ");
        this.registerReceiver(mybroadcastService, intentfiler);
        return START_NOT_STICKY;
    }

    public void playMusicMp3(int pos, ArrayList<Item_song> arr, String linkMp3) {
        this.pos = pos;
        this.arr = arr;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener((mediaPlayer1, i, i1) -> true);
        if (linkMp3.startsWith("https")) {
            try {
                mediaPlayer.setDataSource(linkMp3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mediaPlayer.setDataSource(this, Uri.parse(linkMp3));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
            mediaPlayer.start();
            if(fragment_playmusic != null){
                fragment_playmusic.musicPrepare();
            }
        });
        mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
            Intent intent = new Intent("AUTO_NEXTJ");
            sendBroadcast(intent);
        });
        createNotification(pos, arr, true);
    }

    public void createNotification(int pos, ArrayList<Item_song> arr, Boolean isplaying) {
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        createChanle();
        // create content of notificaton ; remoteview
        RemoteViews remoteview = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteview.setTextViewText(R.id.txtname, arr.get(pos).getSongName());
        remoteview.setTextViewText(R.id.txtsinger, arr.get(pos).getSinger());
        remoteview.setImageViewBitmap(
                R.id.ivplayNoti,
                BitmapFactory.decodeResource(
                        getResources(),
                        (isplaying) ? R.drawable.baseline_pause_purple_500_48dp : R.drawable.baseline_play_arrow_purple_500_48dp
                )
        );
        Notification notification = new NotificationCompat.Builder(this, "No")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.iconnotifi)
                .setCustomBigContentView(remoteview)
                .build();

        if (arr.get(pos).getUriImage() != null) {
            Glide.with(this).asBitmap().load(
                    arr.get(pos).getUriImage()
            ).into(
                    new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            remoteview.setImageViewBitmap(R.id.ivImageSong, resource);
                            startForeground(1, notification);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
        pendingIntent(remoteview, isplaying);
        startForeground(1, notification);
    }

    private void pendingIntent(RemoteViews rv, Boolean isplaying) {
        Intent intenBack = new Intent();
        intenBack.setAction("BACKJ");
        PendingIntent pendingIntentBack =
                PendingIntent.getBroadcast(this, 0, intenBack, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.ivbackNoti, pendingIntentBack);
        if (isplaying) {
            Intent intenPause = new Intent();
            intenPause.setAction("PAUSEJ");
            PendingIntent pendingIntentPause =
                    PendingIntent.getBroadcast(this, 0, intenPause, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.ivplayNoti, pendingIntentPause);
        } else {
            Intent intenPlay = new Intent();
            intenPlay.setAction("PLAYJ");
            PendingIntent pendingIntentPlay =
                    PendingIntent.getBroadcast(this, 0, intenPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.ivplayNoti, pendingIntentPlay);
        }
        Intent intenNext = new Intent();
        intenNext.setAction("NEXTJ");
        PendingIntent pendingIntentNext =
                PendingIntent.getBroadcast(this, 0, intenNext, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.ivnextNoti, pendingIntentNext);
        Intent intentOpen = new Intent(this, MainActivity.class);
        intentOpen.putExtra("Pos", pos);
        intentOpen.putExtra("Arr", arr);
        PendingIntent pendingIntentOpen = PendingIntent.getActivity(this, 1, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.layoutNoti, pendingIntentOpen);
    }

    private void createChanle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel("No", "Music", importance);
            mChannel.setDescription("No");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ArrayList<Item_song> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Item_song> arr) {
        this.arr = arr;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    @Override
    public void onDestroy() {
        this.unregisterReceiver(mybroadcastService);
        super.onDestroy();
    }

    public Fragment_playmusic getFragment_playmusic() {
        return fragment_playmusic;
    }

    public void setFragment_playmusic(Fragment_playmusic fragment_playmusic) {
        this.fragment_playmusic = fragment_playmusic;
    }
}
