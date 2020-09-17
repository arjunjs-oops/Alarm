package com.arjun.alaram.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.arjun.alaram.R;
import com.arjun.alaram.databinding.FragmentTimerBinding;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Timer;
import java.util.TimerTask;

public class timer extends Fragment implements View.OnClickListener {

    private Boolean isRunning ;
    private static long timeLeftIn = 60000;
    private CountDownTimer timerTask;
    FragmentTimerBinding mainBinding;
    private View rootView;
    private long toEndTime;

    private static final String TAG = "timer";
    public timer() {
    }





    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: Saving");
        outState.putBoolean("isRunning",isRunning);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainBinding = FragmentTimerBinding.inflate(inflater);
        return mainBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainBinding.end.setOnClickListener(this);
        mainBinding.end.setClickable(false);
        mainBinding.start.setOnClickListener(this);
        isRunning=false;
        startAnimation();

        //Sliding Bar Custom Pointer Text
        mainBinding.setTimer.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return formattedTime((int) value);
            }
        });
        mainBinding.setTimer.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                mainBinding.time.setAnimation(null);
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                startAnimation();

            }
        });
        mainBinding.setTimer.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                int i = (int) slider.getValue();
                String s = formattedTime((int) (i));
                mainBinding.time.setText(s);
                mainBinding.progressBar.setProgress((int) value / 6);
            }

        });

    }

    private void startAnimation() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(350); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        mainBinding.time.startAnimation(anim);
    }

    //Formatted String For Slider Material
    private String formattedTime(int i) {
        int min = i / 60;
        int sec = i - min * 60;
        String second = String.valueOf(sec);
        if (second.equals("0")) {
            second = "00";
        } else if (sec <= 9) {
            second = "0" + second;
        }
        return min + ":" + second;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                if(!isRunning) {
                    setButtonProperty(false,0.4f,1f);
                    mainBinding.time.setAnimation(null);
                    timeLeftIn = (int) (mainBinding.setTimer.getValue() * 1000);
                    if(mainBinding.setTimer.getValue()==0){
                        Toast.makeText(getActivity(), "Can't Start Timer in 0 second", Toast.LENGTH_SHORT).show();
                        setButtonProperty(true,1f,0.4f);
                        return;
                    }

                }
                setTimer();
                return;
            case R.id.end:
                isRunning=false;
                timerTask.cancel();
                mainBinding.time.setText(formattedTime((int) mainBinding.setTimer.getValue()));
                setButtonProperty(true,1f,0.4f);
                startAnimation();

        }

    }

    private void setButtonProperty(boolean b, float forStart,float forEnd) {
        mainBinding.start.setClickable(b);
        mainBinding.start.setAlpha(forStart);
        mainBinding.setTimer.setClickable(b);
        mainBinding.setTimer.setFocusable(b);
        mainBinding.setTimer.setEnabled(b);
        mainBinding.end.setAlpha(forEnd);
        mainBinding.end.setClickable(!b);

    }


    private void setTimer() {
        toEndTime = System.currentTimeMillis()+timeLeftIn;
        timerTask = new CountDownTimer(timeLeftIn, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftIn = l;
                String s = formattedTime((int) (l / 1000));
                mainBinding.time.setText(s);
                setButtonProperty(false, 0.4f, 1f);
                mainBinding.progressBar.setProgress((int) (l / 6000));
            }

            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onFinish() {
                isRunning=false;
                startAnimation();
                setButtonProperty(true, 1f, 0.4f);
            }

        }.start();
        isRunning = true;

    }
}