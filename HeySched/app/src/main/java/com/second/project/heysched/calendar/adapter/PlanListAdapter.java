package com.second.project.heysched.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.R;
import com.second.project.heysched.plan.PlanDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    Context context;
    int row_res_id;
    List<PlanItem> data;

    public PlanListAdapter(Context context, int row_res_id, List<PlanItem> data) {
        this.context = context;
        this.row_res_id = row_res_id;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(row_res_id,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setColorFilter(Color.parseColor(data.get(position).color));
        holder.title.setText(data.get(position).title);
        holder.content.setText(data.get(position).content);
        holder.invisiblePlanNo.setText(data.get(position).plan_no);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView content;
        TextView invisiblePlanNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.plan_color);
            title = itemView.findViewById(R.id.plan_title);
            content = itemView.findViewById(R.id.plan_content);
            invisiblePlanNo = itemView.findViewById(R.id.plan_invisible_no);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    String plan_no = data.get(pos).plan_no;
                    //Log.d("test", pos +"");
                    new PlanDetailTask().execute(plan_no);

                }

                class PlanDetailTask extends AsyncTask<String, Void, PlanItem> {

                    @Override
                    protected PlanItem doInBackground(String... values) {
                        PlanItem planItem = null;
                        try {
                            URL url = new URL("http://172.20.10.11:8088/heyScheduler/calendar/selectPlanDetail.do");
                            JSONObject object = new JSONObject();
                            String result = "";

                            object.put("plan_no", values[0]);

                            OkHttpClient client = new OkHttpClient();
                            String plan_no = object.toString();

                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(RequestBody.create(MediaType.parse("application/json"), plan_no))
                                    .build();

                            Response response = client.newCall(request).execute();
                            result = response.body().string();
                            Log.d("test", result);
                            JSONObject planDetail = new JSONObject(result);
                            if(planDetail.getString("plan_no") != null) {
                                planItem = new PlanItem(planDetail.getString("plan_no"),
                                        planDetail.getString("title"),
                                        planDetail.getString("startdatetime"),
                                        planDetail.getString("loc_x"),
                                        planDetail.getString("loc_y"),
                                        planDetail.getString("content"),
                                        planDetail.getString("location"),
                                        planDetail.getString("enddatetime"),
                                        planDetail.getString("color"),
                                        planDetail.getString("host_id")
                                );
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return planItem;
                    }

                    @Override
                    protected void onPostExecute(PlanItem planItem) {
                        super.onPostExecute(planItem);
                        if(planItem == null) return;
                        Intent intent = new Intent(context, PlanDetailActivity.class);
                        intent.putExtra("planVO", planItem);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
