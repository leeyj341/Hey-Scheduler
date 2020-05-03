package com.second.project.heysched.plan.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.R;

import java.util.List;


public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    int res_id;
    List<PlaceItem> data;

    public SearchPlaceAdapter(Context context, int res_id, List<PlaceItem> data) {
        this.context = context;
        this.res_id = res_id;
        this.data = data;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "row ",Toast.LENGTH_SHORT).show();
        /*switch (v.getId()){*/
        /*    case R.id.*/
        /*}*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(res_id, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.place_title.setText(data.get(position).place_title);
        holder.place_location.setText(data.get(position).place_location);
        holder.place_hash.setText(data.get(position).place_hash);
        holder.place_title.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView place_title;
        TextView place_location;
        TextView place_hash;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place_title = itemView.findViewById(R.id.place_title);
            place_location = itemView.findViewById(R.id.place_location);
            place_hash = itemView.findViewById(R.id.place_hash);
        }
    }
}
