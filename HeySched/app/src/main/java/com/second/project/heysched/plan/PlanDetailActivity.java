package com.second.project.heysched.plan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.second.project.heysched.R;
import com.second.project.heysched.calendar.adapter.PlanItem;

public class PlanDetailActivity extends AppCompatActivity {
    TextView title;
    ImageView picker;
    TextView startdatetime;
    TextView enddatetime;
    TextView location;
    TextView friends;
    TextView content;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_plan);
        setViews();
        Intent intent = getIntent();
        PlanItem planItem = intent.getParcelableExtra("planVO");
        String title = planItem.getTitle();
        String picker = planItem.getColor();
        String startdatetime = planItem.getStartdatetime();
        String enddatetime = planItem.getEnddatetime();
        String location = planItem.getLocation();
        //String friends = planItem.get;
        String content = planItem.getContent();

        this.title.setText(title);
        this.picker.setColorFilter(Color.parseColor(picker));
        this.startdatetime.setText(startdatetime);
        this.enddatetime.setText(enddatetime);
        this.location.setText(location);
        this.content.setText(content);

    }

    public void setViews() {
        title = findViewById(R.id.plan_title);
        picker = findViewById(R.id.color_picker);
        startdatetime = findViewById(R.id.plan_start_date);
        enddatetime = findViewById(R.id.plan_end_date);
        location = findViewById(R.id.plan_location);
        friends = findViewById(R.id.plan_friends);
        content = findViewById(R.id.plan_content);

        bottomNavigationView = findViewById(R.id.detail_modify_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.detail_plan_modify) {

                }
                return false;
            }
        });
    }
}
