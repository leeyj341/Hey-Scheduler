package com.second.project.heysched.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.second.project.heysched.MainActivity;
import com.second.project.heysched.R;

public class AlarmService extends Service {
    final int REQUEST_CODE = 102;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int month = intent.getIntExtra("month",1);
        int day = intent.getIntExtra("day",1);
        int hour = intent.getIntExtra("hour",1);
        int minute = intent.getIntExtra("minute",1);


        Intent alarmIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"noti_plan")
                .setSmallIcon(R.drawable.ic_icon)
                .setContentTitle(month + "월 " + day + "일 " + hour + "시 " + minute + "분 " + "일정이 있습니다.")
                .setContentIntent(pendingIntent)
                .setContentText("테스트")
                .setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification noti = builder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify(Integer.valueOf(month+day+hour+minute), noti);

        return START_NOT_STICKY;
    }
}
