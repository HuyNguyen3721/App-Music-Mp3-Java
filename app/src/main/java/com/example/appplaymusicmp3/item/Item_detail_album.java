package com.example.appplaymusicmp3.item;

public class Item_detail_album {
    private  String linkImage ;
    private  String name ;
    private  String singerName;
    private  String creatorName ;

    public Item_detail_album(String linkImage, String name, String singerName, String creatorName) {
        this.linkImage = linkImage;
        this.name = name;
        this.singerName = singerName;
        this.creatorName = creatorName;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
