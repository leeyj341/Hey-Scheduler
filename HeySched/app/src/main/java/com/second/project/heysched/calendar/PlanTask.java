package com.second.project.heysched.calendar;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.calendar.adapter.PlanItem;
import com.second.project.heysched.calendar.adapter.PlanListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlanTask extends AsyncTask<String, Void, List<PlanItem>> {
    Context context;
    int row_res_id;
    RecyclerView view;

    public PlanTask(Context context, int row_res_id, RecyclerView view) {
        this.context = context;
        this.row_res_id = row_res_id;
        this.view = view;
    }

    @Override
    protected List<PlanItem> doInBackground(String... values) {
        List<PlanItem> list = new ArrayList<PlanItem>();

        try {
            URL url = new URL("http://172.30.1.46:8088/heyScheduler/calendar/selectPlansOnDay.do");
            JSONObject object = new JSONObject();
            String result = "";

            object.put("startdatetime", values[0]);

            OkHttpClient client = new OkHttpClient();
            String startDate = object.toString();

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"),startDate))
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();
            //Log.d("test", result);
            JSONArray array = new JSONArray(result);

            PlanItem item;
            for (int i = 0; i < array.length(); i++) {
                item = new PlanItem(array.getJSONObject(i).getString("plan_no"),
                        array.getJSONObject(i).getString("title"),
                        array.getJSONObject(i).getString("content"),
                        array.getJSONObject(i).getString("color"));
                list.add(item);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }

    @Override
    protected void onPostExecute(List<PlanItem> planItems) {
        super.onPostExecute(planItems);

        PlanListAdapter adapter = new PlanListAdapter(context, row_res_id, planItems);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        view.setHasFixedSize(true);
        view.setLayoutManager(manager);
        view.setAdapter(adapter);
    }


}
