package com.arjun.alaram.DFragmet;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import com.arjun.alaram.R;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Custom extends DialogFragment {
    public interface  onInputSelected{
        void onOkPressed(String title,Calendar date);
    }
    onInputSelected mOnInputSelected;
    Button ok,cancel;
    EditText title;
    TextView textView;
    TimePicker timePicker;
    String date;
    Calendar calendar = Calendar.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.promtalarm,container,false);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);
        timePicker =view.findViewById(R.id.spinnerPicker);
        title = view.findViewById(R.id.userTitle);
        textView = view.findViewById(R.id.userTime);
        textView.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()));

        return view;
    }

    Button.OnClickListener clickListenerOK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userInput = title.getText().toString();
            if (userInput.equals("")|| textView.getText().equals("")) {
                title.setError("Enter the title");
            } else {
                date = android.icu.text.DateFormat.getTimeInstance().format(calendar.getTime());
                mOnInputSelected.onOkPressed(userInput, calendar);
                getDialog().dismiss();
            }
        }

    };

    Button.OnClickListener clickListenerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();


        }
    };
    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i1) {
            String min = i1<=9?"0"+i1:String.valueOf(i1);
                if(i==0||i<12){ textView.setText(i+":"+min+" AM");
                }
                else{
                    int hour =i-12;
                    textView.setText(hour+":"+min+" PM");


                }
                calendar.set(Calendar.MINUTE,i1);
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.SECOND, 0);
            }


    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ok.setOnClickListener(clickListenerOK);
        timePicker.setOnTimeChangedListener(timeChangedListener);
        cancel.setOnClickListener(clickListenerCancel);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         mOnInputSelected = (onInputSelected) getTargetFragment();
    }
}
