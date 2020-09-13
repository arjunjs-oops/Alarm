package com.arjun.alaram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.arjun.alaram.Fragments.alarm;
import com.arjun.alaram.Fragments.clock;
import com.arjun.alaram.Fragments.timer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity {

    public static final String HOME ="home";
    public static final String OTHERS = "others";
    private BottomNavigationView bottomNavigationView;
    private Fragment alarm;
    private Fragment timer;
    private Fragment clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         alarm = new alarm();
         clock = new clock();
         timer = new timer();
        bottomNavigationView = findViewById(R.id.bni);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.host,new alarm(),"HOME").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

    }
    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.alarm:
                    viewFragment(alarm,HOME);
                    break;
                case R.id.clock:
                    viewFragment(clock,OTHERS);
                    break;
                case R.id.timer:
                    viewFragment(timer,OTHERS);
                    break;
            }
            return true;
        }
    };






    private void viewFragment(Fragment fragment, String name){
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.host, fragment);
        //  Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();

        // If the fragment is **not** "home type", save it to the stack
        if( name.equals( OTHERS) ) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if( fragmentManager.getBackStackEntryCount() <= count){
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(OTHERS, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }
}