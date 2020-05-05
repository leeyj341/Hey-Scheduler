package com.second.project.heysched.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.second.project.heysched.MainActivity;
import com.second.project.heysched.R;

public class AlarmReceiver extends BroadcastReceiver {
    final int REQUEST_CODE = 102;

    @Override
    public void onReceive(Context context, Intent intent) {
        String month = intent.getStringExtra("month");
        String day = intent.getStringExtra("day");
        String hour = intent.getStringExtra("hour");
        String minute = intent.getStringExtra("minute");

        Log.d("test", "나는 리시버 아하하하하하");

        Intent alarmIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"noti_plan")
                .setSmallIcon(R.drawable.ic_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(false)
                .setContentTitle(month + "월 " + day + "일 " + hour + "시 " + minute + "분 " + "일정이 있습니다.")
                .setContentIntent(pendingIntent)
                .setContentText("테스트")
                .setFullScreenIntent(pendingIntent, true)
                .setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = builder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        Log.d("test", "dpdodjwjfurfhkrenuwkcimqjn::::::::::::::::::::::::");

        manager.notify(Integer.valueOf(month+day+hour+minute), noti);
    }
}
