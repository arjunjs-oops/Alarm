package com.arjun.alaram.Broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.arjun.alaram.R;
import com.arjun.alaram.utils.NC;
import com.arjun.alaram.utils.SingleTone;

import java.util.logging.Handler;

public class Receiver extends BroadcastReceiver {
    Ringtone manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        NC nc = new NC(context);
        {
            String title = intent.getStringExtra("title");
            Uri rm = Uri.parse(String.valueOf(intent.getParcelableExtra("Ringtone")));


            Notification.Builder nb = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                nb = nc.getChannelNotification()
                        .setContentTitle(title).setContentText("You have a new Alarm ")
                .setAutoCancel(true);
            }
            assert nb != null;
            nc.getManager().notify(1, nb.build());
            nb.setSound(rm);

        }

    }
}
