package com.second.project.heysched;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.second.project.heysched.dbtest.DBTestActivity;
import com.second.project.heysched.map.MapActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_map);

        Button button1 = findViewById(R.id.find_way_transit);
        Button button2 = findViewById(R.id.find_way_walk);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("mode", "transit");
                //일정정보에서 일정 시작 시간 받아야 함
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DBTestActivity.class);
                startActivity(intent);
            }
        });
        /*button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("mode", "Driving");
                startActivity(intent);
            }
        });*/


    }
}
