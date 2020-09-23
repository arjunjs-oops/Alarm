package com.arjun.alaram.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.arjun.alaram.Broadcast.RingtonePlayingService;
import com.arjun.alaram.Broadcast.Singleton;
import com.arjun.alaram.R;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;


public class alarm extends Fragment{
   public static boolean isActive = true;
    AlarmManager alarm_manager;
    TextView alarm_state;
    TimePicker timePicker;
    RelativeLayout layout;
    Boolean pendingIntent;
    Calendar calendar;
    private static final String NamePreference="AlarmAppPreference";
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    Intent my_intent;
    int hour,minute;
    BroadcastReceiver stopServiceReceiver;
    Button alarm_off, alarm_on ;


    public alarm() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            try {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        }


    }



    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_alarm, container, false);

        timePicker =view.findViewById(R.id.timePicker);
        layout =view.findViewById(R.id.parent);
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarm_state = (TextView) view.findViewById(R.id.alarm_state);
        alarm_off = (Button) view.findViewById(R.id.alarm_off);
        alarm_on = (Button) view.findViewById(R.id.alarm_on);
        preferences = getActivity().getSharedPreferences(NamePreference,Context.MODE_PRIVATE);
        editor = preferences.edit();
       pendingIntent = preferences.getBoolean("pendingIntent",false);
         calendar = Calendar.getInstance();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alarm_off.setOnClickListener(alarm_offListener);
        alarm_on.setOnClickListener(alarm_onListener);
        alarm_state.setText(preferences.getString("Time","Did You set the alarm"));
        my_intent = new Intent(getActivity().getApplicationContext(), RingtonePlayingService.class);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.arjun.alarm.java");
        stopServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("From Service", "onReceive: Data Arrived");
                getActivity().stopService(my_intent);
                alarm_state.setText("Alarm cancelled");
            }
        };
        getActivity().registerReceiver(stopServiceReceiver,filter);

    }

    Button.OnClickListener alarm_offListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (pendingIntent) {
                getActivity().stopService(new Intent(getActivity(), RingtonePlayingService.class));
                alarm_state.setText("Alarm Off!");
                editor.putString("Time", null);
                editor.putBoolean("pendingIntent", false);
                editor.commit();
            }
        }
    };

    Button.OnClickListener alarm_onListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Set calendar based on user input
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
             hour = timePicker.getHour();
             minute = timePicker.getMinute();
            String hour_string = String.valueOf(hour);
            String minute_string = String.valueOf(minute);
            // Handles to format time data
            if (hour > 12) hour_string = String.valueOf(hour - 12);
            if (minute < 10) minute_string = "0" + String.valueOf(minute);
            String quote = "Alarm set to: " + hour_string + ":" + minute_string;
            alarm_state.setText(quote);
            editor.putString("Time",quote);
            editor.commit();
            editor.putBoolean("pendingIntent",true);
            pendingIntent=true;
            editor.commit();
            // Create intent for AlarmReceiver class, send only once

            callService(my_intent,hour,minute);





        }

    };

    private void callService(Intent my_intent,int hour, int minute) {
        Objects.requireNonNull(getActivity()).stopService(my_intent);
        my_intent.putExtra("hour", hour);
        my_intent.putExtra("minute", minute);
        my_intent.putExtra("calender", calendar.getTimeInMillis());
        Log.e("No service", "callService: No service Intent" + my_intent.hasExtra("calender"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(my_intent);
        }
    }

    @Override
    public void onDestroy() {
        my_intent.putExtra("isDestroyed",true);
        callService(my_intent,hour,minute);
        super.onDestroy();
    }
}

