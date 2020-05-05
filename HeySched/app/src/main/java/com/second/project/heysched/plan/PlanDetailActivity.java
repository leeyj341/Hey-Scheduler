package com.second.project.heysched.plan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.second.project.heysched.R;
import com.second.project.heysched.calendar.PlanModifyActivity;
import com.second.project.heysched.calendar.adapter.PlanItem;

public class PlanDetailActivity extends AppCompatActivity {
    public static final int PLAN_DETAIL = 2001;

    TextView title;
    ImageView picker;
    TextView startdatetime;
    TextView enddatetime;
    TextView location;
    TextView friends;
    TextView content;

    PlanItem planItem;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_plan);
        setViews();
        Intent intent = getIntent();
        planItem = intent.getParcelableExtra("planVO");

        this.title.setText(planItem.getTitle());
        this.picker.setColorFilter(Color.parseColor(planItem.getColor()));
        this.startdatetime.setText(planItem.getStartdatetime());
        this.enddatetime.setText(planItem.getEnddatetime());
        this.location.setText(planItem.getLocation());
        this.content.setText(planItem.getContent());

    }

    public void setViews() {
        title = findViewById(R.id.plan_title);
        picker = findViewById(R.id.color_picker);
        startdatetime = findViewById(R.id.plan_start_date);
        enddatetime = findViewById(R.id.plan_end_date);
        //location = findViewById(R.id.plan_location);
        friends = findViewById(R.id.plan_friends);
        content = findViewById(R.id.plan_content);

        bottomNavigationView = findViewById(R.id.detail_modify_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.detail_plan_modify) {
                    Intent intent = new Intent(PlanDetailActivity.this, PlanModifyActivity.class);
                    intent.putExtra("planItem", planItem);
                    startActivityForResult(intent, PLAN_DETAIL);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLAN_DETAIL) {
            if(resultCode == RESULT_OK) {
                PlanItem resultPlanItem = data.getParcelableExtra("returnData");
                this.title.setText(resultPlanItem.getTitle());
                this.picker.setColorFilter(Color.parseColor(resultPlanItem.getColor()));
                this.startdatetime.setText(resultPlanItem.getStartdatetime());
                this.enddatetime.setText(resultPlanItem.getEnddatetime());
                this.location.setText(resultPlanItem.getLocation());
                this.content.setText(resultPlanItem.getContent());
            }
        }
    }
}
