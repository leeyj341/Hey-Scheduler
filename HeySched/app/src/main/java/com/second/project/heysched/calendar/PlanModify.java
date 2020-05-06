package com.second.project.heysched.calendar;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.second.project.heysched.calendar.adapter.PlanItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlanModify extends AsyncTask<PlanItem, Void, String> {
    @Override
    protected String doInBackground(PlanItem... planItems) {
        String result = "";
        try {
            URL url = new URL("http://70.12.230.57:8088/heyScheduler/calendar/modify.do");
            Gson gson = new Gson();
            Log.d("test", planItems[0].toString());
            String json = gson.toJson(planItems[0]);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
