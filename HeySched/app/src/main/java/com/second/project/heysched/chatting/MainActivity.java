package com.second.project.heysched.chatting;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chatting);

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
}
