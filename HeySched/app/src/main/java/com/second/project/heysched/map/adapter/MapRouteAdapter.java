package com.second.project.heysched.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.R;

import org.w3c.dom.Text;

import java.util.List;

public class MapRouteAdapter extends RecyclerView.Adapter<MapRouteAdapter.ViewHolder> {
    Context context;
    int row_res_id;
    List<MapRouteItem> data;

    public MapRouteAdapter(Context context, int row_res_id, List<MapRouteItem> data) {
        this.context = context;
        this.row_res_id = row_res_id;
        this.data = data;
    }

    @NonNull
    @Override
    public MapRouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(row_res_id,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapRouteAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(data.get(position).imageResource);
        holder.instruction.setText(data.get(position).instruction);
        holder.duration.setText(data.get(position).duration);
        holder.distance.setText(data.get(position).distance);
        String endStop = "";
        if((endStop = data.get(position).endStop) != null) {
            holder.endStop.setText(endStop);
        } else {
            holder.endStop.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView instruction;
        TextView duration;
        TextView distance;
        TextView endStop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.map_route_icon2);
            instruction = itemView.findViewById(R.id.map_route_instruction2);
            duration = itemView.findViewById(R.id.map_route_duration2);
            distance = itemView.findViewById(R.id.map_route_distance2);
            endStop = itemView.findViewById(R.id.map_route_endStop);
        }
    }
}
