package com.arjun.alaram.Broadcast;

import android.app.AlarmManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;

public abstract class Singleton {
    public static Ringtone ringtone=null;
    public static Ringtone getAlarmOnce(Context context){
        if(ringtone==null){
            ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        }
        return ringtone;
    }
}
