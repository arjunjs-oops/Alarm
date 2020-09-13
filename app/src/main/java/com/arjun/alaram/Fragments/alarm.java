package com.arjun.alaram.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.aware.AttachCallback;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.arjun.alaram.Broadcast.Receiver;
import com.arjun.alaram.DFragmet.Custom;
import com.arjun.alaram.POJO.Data;
import com.arjun.alaram.R;
import com.arjun.alaram.RV.Adapter;
import com.arjun.alaram.Room.PVM;
import com.arjun.alaram.utils.TimePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class alarm extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        Adapter.onClickIO,Custom.onInputSelected{
    RecyclerView recyclerView;
    Adapter adapter;
    SwitchMaterial material;
    FloatingActionButton actionButton;

    private int position;
    PVM pvm;
    private  Ringtone r;
    List<Data> dataArrayList = new ArrayList<>();


    public alarm() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pvm =  ViewModelProviders.of(this).get(PVM.class);
        setRecyclerView();
        pvm.getAllData().observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {
                adapter.setDynamically(data);
                dataArrayList = data;
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_alarm, container, false);
        recyclerView = view.findViewById(R.id.parent_alarm);
        actionButton = view.findViewById(R.id.open);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getContext(), alert);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        actionButton.setOnClickListener(floatingClickListener);
        material.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!compoundButton.isChecked()){
                    if(r.isPlaying()){
                        r.stop();
                    }
                }
            }
        });



    }

    FloatingActionButton.OnClickListener floatingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Custom custom = new Custom();
            custom.setTargetFragment(alarm.this,1);
            custom.show(getParentFragmentManager(),"Input");

        }
    };




    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,i);
        calendar.set(Calendar.MINUTE,i1);
        calendar.set(Calendar.SECOND,0);
         Data data =  dataArrayList.get(position);
         data.setCalender(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
         pvm.updateData(data);
        createAlarm(calendar);



    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void setSound() {

            r.play();
    }

    private void createAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(),Receiver.class);
//        intent.putExtra("title",dataArrayList.get(position).getTitle());
        setSound();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity()
                ,dataArrayList.get(position).getUid(),
                intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);;
    }


    private void setRecyclerView(){
        adapter = new Adapter(this);
        adapter.setDynamically(dataArrayList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            pvm.deleteData(dataArrayList.get((position)));
            if(r.isPlaying()){
                r.stop();
            }
        }
    };






    @Override
    public void setImageOnClick(SwitchMaterial data, int position) {
        TimePicker picker = new TimePicker();
        picker.setTargetFragment(alarm.this, 1);
        picker.show(getParentFragmentManager(), "clock_date_picker");
        this.position = position;
        material = data;


    }



    @Override
    public void onOkPressed(String title, Calendar date) {
        pvm.insertData(new Data(DateFormat.getTimeInstance().format(date.getTime()),title));
        createAlarm(date);
    }
}