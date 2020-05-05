package com.second.project.heysched.plan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.second.project.heysched.R;

import java.util.List;

public class InvitedProfileAdapter extends RecyclerView.Adapter<InvitedProfileAdapter.profileHolder> {
    Context context;
    int res_id;
    List<String> data;

    public InvitedProfileAdapter(Context context, int res_id, List<String> data) {
        this.context = context;
        this.res_id = res_id;
        this.data = data;
    }

    @NonNull
    @Override
    public profileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(res_id,null);
        return new profileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profileHolder holder, int position) {
        Glide.with(context).load(data.get(position)).apply(new RequestOptions().circleCrop()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class profileHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public profileHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.invited_profile);
        }
    }
}
