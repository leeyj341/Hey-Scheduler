package com.second.project.heysched.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.second.project.heysched.calendar.adapter.PlanItem;
import com.second.project.heysched.map.MapLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class AlarmStart {
    Context context;
    Date date;
    long timeToGo;
    PlanItem planItem;
    String address;

    public AlarmStart(Context context) {
        this.context = context;
    }

    public void set(String startdatetime, PlanItem item, String myAddress) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        planItem = item;
        address = myAddress;
        int id = 0;
        try {
            date = df.parse(startdatetime);
            Log.d("test", startdatetime);
            Log.d("test", myAddress);
            Log.d("test", item.toString() + "");
            new TimeAsyncTask().execute(myAddress, item.getLocation()); //출발 장소, 도착 장소
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class TimeAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            MapLocation mapLocation = new MapLocation(context);

            String path = getPath(mapLocation.getLatLngFromAddress(strings[0]),
                    mapLocation.getLatLngFromAddress(strings[1]), date.getTime());
            BufferedReader in;
            StringBuffer sb;
            JSONObject json;
            int result = 0;
            try {
                URL url = new URL(path);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    sb = new StringBuffer();
                    String data = null;
                    while((data = in.readLine()) != null) {
                        data = data.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
                        sb.append(data);
                    }
                    //
                    json = new JSONObject(sb.toString());
                    result = json.getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("duration")
                            .getInt("value");
                    Log.d("test", result + "");
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer time) {
            super.onPostExecute(time);
            timeToGo = time * 1000;
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");

            String month = monthFormat.format(date);
            String day = dayFormat.format(date);
            String hour = hourFormat.format(date);
            String minute = minuteFormat.format(date);

            Intent intent = new Intent("com.project.heysched.ALARM_START");
            Log.d("test", intent + "");
            intent.putExtra("month", "month");
            intent.putExtra("day", day);
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);
            intent.putExtra("planItem", planItem);
            intent.putExtra("address", address);
            new AlarmReceiver().onReceive(context, intent);

            int id = Integer.valueOf(month+day+hour+minute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //테스트
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime() - timeToGo - 600000);
            Log.d("test", df.format(calendar.getTime()) +  " ================================================================ ");

            manager.set(AlarmManager.RTC_WAKEUP, date.getTime() - timeToGo - 600000, pendingIntent);
        }

        public String getPath(LatLng origin, LatLng destination, long time) {
            StringBuffer path = new StringBuffer("https://maps.googleapis.com/maps/api/directions/json?");
            path.append("origin=").append(origin.latitude).append(",").append(origin.longitude).append("&");
            path.append("destination=").append(destination.latitude).append(",").append(destination.longitude).append("&");
            path.append("mode=transit&");
            path.append("arrival_time=").append(time).append("&");
            path.append("language=ko&");
            path.append("key=").append("AIzaSyABcoK6IL4ctXEp3TOXQ_fxrKN8v0eP9MI");
            return path.toString();
        }
    }
}
