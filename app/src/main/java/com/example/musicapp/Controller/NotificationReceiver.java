package com.example.musicapp.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.musicapp.View.PlayerActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            PlayerActivity playerActivity = PlayerActivity.instance;
            if (playerActivity != null){
                if (action.equals("previous")) {
                    //playerActivity.playPreviousSong();
                } else if (action.equals("play")) {
//                    playerActivity.play();
                } else if (action.equals("next")) {
                    //playerActivity.playNextSong();
                }
            }
        }
    }
}
