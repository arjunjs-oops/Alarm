package com.arjun.alaram;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arjun.alaram.utils.TimePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;


public class alarm extends Fragment implements TimePickerDialog.OnTimeSetListener{
    private static final  String TAG = "Alarm";
    TextView text;
    public alarm() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_alarm, container, false);
        text = view.findViewById(R.id.toShow);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton actionButton = view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker picker = new TimePicker();
                picker.setTargetFragment(alarm.this, 0);
                picker.show(getFragmentManager(), "date picker");
            }
        });
    }




    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        text.setText("The Time is "+i+i1);

    }
}