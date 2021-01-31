package com.example.appplaymusicmp3.item;

import android.os.Parcel;
import android.os.Parcelable;

public class Item_song  implements Parcelable {
    private  String uriImage ;
    private  String songName ;
    private  String singer ;
    private String link_music ;
    public Item_song(String uriImage, String songName, String singer, String link_music) {
        this.uriImage = uriImage;
        this.songName = songName;
        this.singer = singer;
        this.link_music = link_music;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLink_music() {
        return link_music;
    }

    public void setLink_music(String link_music) {
        this.link_music = link_music;
    }

    protected Item_song(Parcel in) {
        uriImage = in.readString();
        songName = in.readString();
        singer = in.readString();
        link_music = in.readString();
    }

    public static final Creator<Item_song> CREATOR = new Creator<Item_song>() {
        @Override
        public Item_song createFromParcel(Parcel in) {
            return new Item_song(in);
        }

        @Override
        public Item_song[] newArray(int size) {
            return new Item_song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uriImage);
        parcel.writeString(songName);
        parcel.writeString(singer);
        parcel.writeString(link_music);
    }
}
