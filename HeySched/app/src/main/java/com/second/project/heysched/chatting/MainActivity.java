package com.second.project.heysched.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.second.project.heysched.R;
import com.second.project.heysched.chatting.fragment.AccountFragment;
import com.second.project.heysched.chatting.fragment.ChatFragment;
import com.second.project.heysched.chatting.fragment.PeopleFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chatting);

        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new PeopleFragment()).commit();

        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_drawer_nav);
        selectActivity();

        BottomNavigationView bottomNavigationView=findViewById(R.id.mainactivity_bottomnavigationview);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_people :
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new PeopleFragment()).commit(); // 만약 fragment오류 뜨면 v4.fragment로 사용해야한대
                        return true;

                    case R.id.action_chat:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new ChatFragment()).commit(); // 만약 fragment오류 뜨면 v4.fragment로 사용해야한대
                        return true;
                    case R.id.action_account:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new AccountFragment()).commit(); // 만약 fragment오류 뜨면 v4.fragment로 사용해야한대
                        return true;
                }

                return false;
            }
        });
        passPushTokenToServer();
    }
    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token= FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> map=new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map); //여기서 setValue하면 기존 데이터 날라감 그래서 update해야함
    }

    public void selectActivity() {
        Log.d("navigationdetect",        navigationView.getId()+"");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                switch (id) {
                    case R.id.drawer_menu_calendar:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, com.second.project.heysched.MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
}
