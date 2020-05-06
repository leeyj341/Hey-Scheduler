package com.second.project.heysched.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.second.project.heysched.R;
import com.second.project.heysched.calendar.PlanDetailIncludeWeatherActivity;
import com.second.project.heysched.calendar.adapter.PlanItem;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 102;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("test", intent.toString() + "ㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ");
        String month = intent.getStringExtra("month");
        String day = intent.getStringExtra("day");
        String hour = intent.getStringExtra("hour");
        String minute = intent.getStringExtra("minute");
        PlanItem planItem = intent.getParcelableExtra("planItem");
        //Log.d("test",month + "");

        //Log.d("test", planItem.toString());

        Intent alarmIntent = new Intent(context, PlanDetailIncludeWeatherActivity.class);
        alarmIntent.putExtra("planItem", planItem);
        //int requestCode = Integer.valueOf(month + day + hour + minute + Math.random());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"noti_plan")
                .setSmallIcon(R.drawable.ic_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(month + "월 " + day + "일 " + hour + "시 " + minute + "분 " + "일정이 있습니다.")
                .setContentText("테스트")
                .setFullScreenIntent(pendingIntent, true)
                .setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = builder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        Log.d("test", "dpdodjwjfurfhkrenuwkcimqjn::::::::::::::::::::::::");

        manager.notify(REQUEST_CODE, noti);
    }
}
