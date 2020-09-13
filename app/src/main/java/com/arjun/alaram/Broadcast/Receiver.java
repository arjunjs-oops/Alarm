package com.arjun.alaram.Broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.arjun.alaram.utils.NC;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NC nc = new NC(context);
        {
            String title = intent.getStringExtra("title");

            Notification.Builder nb = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                nb = nc.getChannelNotification()
                        .setContentTitle(title)
                ;
            }
            nc.getManager().notify(1, nb.build());
        }
    }
}
