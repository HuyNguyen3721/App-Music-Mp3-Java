package com.example.appplaymusicmp3.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.appplaymusicmp3.interfaceMusic.IclickNotification;

public class MybroadcastFragmentplay extends BroadcastReceiver {
    private IclickNotification inter;
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "PLAYJ":
                if (inter != null) {
                    inter.onMPlay();
                }
                break;
            case "PAUSEJ":
                if (inter != null) {
                    inter.onMPause();
                }

                break;
            case "BACKJ":
                if (inter != null) {
                    inter.onMBack();
                }
                break;
            case "NEXTJ":
            case "AUTO_NEXTJ":
                if (inter != null) {
                    inter.onMNext();
                }
                break;
            default:
                break;
        }
    }

    public IclickNotification getInter() {
        return inter;
    }

    public void setInter(IclickNotification inter) {
        this.inter = inter;
    }
}
