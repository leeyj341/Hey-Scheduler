package com.second.project.heysched.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.second.project.heysched.R;

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
                String sDate = "2020-05-06 00:58:00";

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

                Date date = null;
                try {
                    date = df .parse(sDate);
                    Log.d("test",date + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String month = monthFormat.format(date);
                String day = dayFormat.format(date);
                String hour = hourFormat.format(date);
                String minute = minuteFormat.format(date);

                Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                int id = Integer.valueOf(month+day+hour+minute);


                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    manager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                } else {
                    manager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                }

                //JobScheduler.Start(AlarmActivity.this);
            }
        });
    }
}
