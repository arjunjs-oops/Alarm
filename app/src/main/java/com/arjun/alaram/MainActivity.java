package com.arjun.alaram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bni);
        getSupportFragmentManager().beginTransaction().replace(R.id.host,new alarm()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

    }
    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment = null;
            switch (item.getItemId()) {
                case R.id.alarm:
                    mFragment = new alarm();
                    break;
                case R.id.clock:
                    mFragment = new clock();
                    break;
                case R.id.timer:
                    mFragment = new timer();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.host,mFragment).commit();
            return true;
        }
    };
}