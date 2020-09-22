package com.arjun.alaram.Broadcast;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.arjun.alaram.MainActivity;
import com.arjun.alaram.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.arjun.alaram.Broadcast.NC.channelID;


public class RingtonePlayingService extends Service {

    int hour, minute;
    private Timer timer = new Timer();
    long calendar;
    NC nc;
    NCLOW nbl;
    Ringtone singleton;
    NotificationCompat.Builder  nb;
    PendingIntent pending_intent;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Upcoming Alarm")
                    .setContentText("Time will be here").build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Got Intent Extras
        hour = intent.getIntExtra("hour", 0);
        minute = intent.getIntExtra("minute", 0);
        calendar = intent.getLongExtra("calender",0);

        //Two Notification Channels Instantiated
        nbl = new NCLOW(getBaseContext());
        nc = new NC(getBaseContext());
        singleton = Singleton.getAlarmOnce(getBaseContext());


        //Intent from Foreground Service to the Activity
        final Intent my_intent = new Intent(this, MainActivity.class);
        pending_intent = PendingIntent.getActivity(getApplicationContext(), 0, my_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //Notification Builder for long running Foreground Service
        NotificationCompat.Builder nco = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nco = nbl.getChannelNotification()
                    .setContentTitle("Upcoming Alarm")
                    .setContentText(hour+":"+minute)
                    .setSmallIcon(R.drawable.ic_baseline_alarm_add_24)
                    .addAction(R.drawable.ic_baseline_alarm_add_24,"Cancel",pending_intent)
                    .setAutoCancel(true);
        }
        //notify
        nbl.getManager().notify(1, nco.build());


        registerReceiver(stopServiceReceiver, new IntentFilter("myFilter"));
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, new Intent("myFilter"), PendingIntent.FLAG_UPDATE_CURRENT);
        //Notification Builder after the alarm is ready

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nb = nc.getChannelNotification()
                    .setContentTitle("Hello ")
                    .setContentText("Alarm is ringing")
                    .setSmallIcon(R.drawable.ic_baseline_alarm_add_24)
                    .addAction(R.drawable.ic_baseline_alarm_add_24,"Stop",contentIntent)
                    .setAutoCancel(true);



        }
        assert nb != null;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==hour&&
                Calendar.getInstance().get(Calendar.MINUTE)==minute){
                    Log.e("Alarm is ready to fire", "run: " );
                    nbl.getManager().cancel(1);
                    nc.getManager().notify(1, nb.build());
                    singleton.play();
                    timer.cancel();
                    timer.purge();
                }

            }
        },0,3000);
        //start alarm notice


        return super.onStartCommand(intent,flags,startId);
    }




    @Override
    public void onDestroy() {
        timer.cancel();
        singleton.stop();
        unregisterReceiver(stopServiceReceiver);
        nc.getManager().cancel(1);
        Log.e("TAG", "onDestroy: Service is stopped" );
        super.onDestroy();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //We need to declare the receiver with onReceive function as below
    protected BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };

}
