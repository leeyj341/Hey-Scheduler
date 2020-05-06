package com.second.project.heysched.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.second.project.heysched.R;
import com.second.project.heysched.calendar.adapter.PlanItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Button button = findViewById(R.id.alarm_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDate = "2020-05-06 09:33:00";
                PlanItem planItem = new PlanItem("1",
                        "발표한다",
                        "2020-05-06 12:25:00",
                        "37.5006789",
                        "127.03524589999999",
                        "수정완료",
                        "스타벅스 역삼역점",
                        "2020-05-06 14:00:00",
                        "#FFCE93D8",
                        FirebaseAuth.getInstance().getCurrentUser().getUid());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                String month = null;
                String day = null;
                String hour = null;
                String minute = null;
                Date date = null;
                int id = 0;
                try {
                    date = df.parse(sDate);
                    Log.d("test", date + "");
                    month = monthFormat.format(date);
                    day = dayFormat.format(date);
                    hour = hourFormat.format(date);
                    minute = minuteFormat.format(date);

                    Intent intent = new Intent("com.project.heysched.ALARM_START");
                    Log.d("test", intent + "");
                    intent.putExtra("month", "month");
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    intent.putExtra("planItem", planItem);
                    new AlarmReceiver().onReceive(AlarmActivity.this, intent);

                    id = Integer.valueOf(month+day+hour+minute);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                //JobScheduler.Start(AlarmActivity.this);
            }
        });
    }
}
