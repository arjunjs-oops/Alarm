package com.arjun.alaram.Broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import static com.arjun.alaram.Broadcast.NC.channelID;
import android.util.Log;

import com.arjun.alaram.MainActivity;


public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent my_intent = new Intent(context, MainActivity.class);
        PendingIntent  pending_intent = PendingIntent.getActivity(context, 0, my_intent,PendingIntent.FLAG_UPDATE_CURRENT);


    }
}
