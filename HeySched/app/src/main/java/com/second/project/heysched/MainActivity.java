package com.second.project.heysched;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.second.project.heysched.chatting.SplashActivity;
import com.second.project.heysched.plan.fragment.DetailCalendarFragment;
import com.second.project.heysched.calendar.fragment.MainCalendarFragment;

public class MainActivity extends AppCompatActivity {
    static final int ACTIVITY_CHAT = 11;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    MainCalendarFragment mainCalendarFragment;
    DetailCalendarFragment detailCalendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        selectActivity();
        selectFragment();
    }

    public void setViews() {
        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_drawer_nav);
        bottomNavigationView = findViewById(R.id.main_bottom_nav);

        mainCalendarFragment = new MainCalendarFragment();
        detailCalendarFragment = new DetailCalendarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.calendar_fragment, mainCalendarFragment).commit();
    }

    public void selectActivity() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                switch (id) {
                    case R.id.drawer_menu_chat:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivityForResult(intent, ACTIVITY_CHAT);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    public void selectFragment() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (id) {
                    case R.id.calendar_main:
                        item.setChecked(true);
                        transaction.replace(R.id.calendar_fragment, mainCalendarFragment).commit();
                        break;
                    case R.id.calendar_detail:
                        item.setChecked(true);
                        transaction.replace(R.id.calendar_fragment, detailCalendarFragment).commit();
                        break;
                }
                return false;
            }
        });
    }

}
