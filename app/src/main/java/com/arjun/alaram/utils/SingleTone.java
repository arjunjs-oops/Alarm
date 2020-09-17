package com.arjun.alaram.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public  class SingleTone {
   Ringtone  singleTone;
    public  Ringtone instance(final Context context, Uri uri){
        singleTone = RingtoneManager.getRingtone(context,uri);
        return singleTone;
    }
}
