package com.second.project.heysched.plan;

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

public class PlanInsert extends AsyncTask<PlanItem, Void, String> {

    @Override
    protected String doInBackground(PlanItem... planItems) {
        String result = "";
        try {
            URL url = new URL("http://70.12.230.57:8088/heyScheduler/calendar/insert.do");
            Gson gson = new Gson();
            String json = gson.toJson(planItems[0]);
            Log.d("insertToSTS",planItems[0].getTitle());
            Log.d("insertToSTS",planItems[0].getStartdatetime());
            Log.d("insertToSTS",planItems[0].getEnddatetime());
            Log.d("insertToSTS",planItems[0].getLocation());
            Log.d("insertToSTS",planItems[0].getContent());
            Log.d("insertToSTS",planItems[0].getColor());
            Log.d("insertToSTS",planItems[0].getHost_id());
            for(int i = 0;i<planItems[0].getGuest_id().size();i++){
                Log.d("insertToSTSF",planItems[0].getGuest_id().get(i));
            }

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
