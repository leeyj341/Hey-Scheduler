package com.second.project.heysched.plan.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.R;
import com.second.project.heysched.plan.AddPlanActivity;

import java.util.List;


public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder> {
    Context context;
    int res_id;
    List<PlaceItem> data;

    private OnItemClickListener itemClickListener = null;
    public SearchPlaceAdapter(Context context, int res_id, List<PlaceItem> data) {
        this.context = context;
        this.res_id = res_id;
        this.data = data;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // list item 클릭 이벤트 처리를 activity에서 수행할 수 있게
    // Listener 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    //
    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView place_title;
        TextView place_location;
        TextView place_hash;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            place_title = itemView.findViewById(R.id.place_title);
            place_location = itemView.findViewById(R.id.place_location);
            place_hash = itemView.findViewById(R.id.place_hash);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(itemClickListener!=null){
                            itemClickListener.onItemClick(v, position);
                        }

                    }
                }
            });
        }
    }
}

