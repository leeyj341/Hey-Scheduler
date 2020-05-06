package com.second.project.heysched.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.second.project.heysched.R;
import com.second.project.heysched.calendar.adapter.PlanItem;
import com.second.project.heysched.map.MapActivity;

public class PlanDetailIncludeWeatherActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PLAN_DETAIL_INCLUDE_WEATHER = 3001;

    TextView title;
    TextView location;
    TextView content;
    TextView friends;
    Button navigationButton;
    PlanItem planItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_detail);
        setViews();

        Intent receiveIntent = getIntent();
        planItem = receiveIntent.getParcelableExtra("planItem");

        title.setText(planItem.getTitle());
        location.setText(planItem.getLocation());
        content.setText(planItem.getContent());
        //friends.setText(planItem.);

    }

    public void setViews() {
        title = findViewById(R.id.plan_title);
        location = findViewById(R.id.plan_location);
        content = findViewById(R.id.plan_content);
        friends = findViewById(R.id.plan_friends);
        navigationButton = findViewById(R.id.navigation);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.navigation) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("arrivalTime", planItem.getStartdatetime());
            intent.putExtra("latitude", planItem.getLoc_x());
            intent.putExtra("longitude", planItem.getLoc_y());
            startActivityForResult(intent, PLAN_DETAIL_INCLUDE_WEATHER);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLAN_DETAIL_INCLUDE_WEATHER) {
            if(resultCode == RESULT_OK) {

            }
        }
    }
}
