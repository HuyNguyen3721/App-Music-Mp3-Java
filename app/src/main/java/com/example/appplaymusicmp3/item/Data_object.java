package com.example.appplaymusicmp3.item;

import java.util.ArrayList;

public class Data_object {
    private ArrayList<Item_song> arr ;
    private ArrayList<Item_BXH> arrBXH;
    private ArrayList<Item_song> arrlist ;

    public Data_object(ArrayList<Item_song> arr, ArrayList<Item_BXH> arrBXH, ArrayList<Item_song> arrlist) {
        this.arr = arr;
        this.arrBXH = arrBXH;
        this.arrlist = arrlist;
    }

    public ArrayList<Item_song> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Item_song> arr) {
        this.arr = arr;
    }

    public ArrayList<Item_BXH> getArrBXH() {
        return arrBXH;
    }

    public void setArrBXH(ArrayList<Item_BXH> arrBXH) {
        this.arrBXH = arrBXH;
    }

    public ArrayList<Item_song> getArrlist() {
        return arrlist;
    }

    public void setArrlist(ArrayList<Item_song> arrlist) {
        this.arrlist = arrlist;
    }
}
