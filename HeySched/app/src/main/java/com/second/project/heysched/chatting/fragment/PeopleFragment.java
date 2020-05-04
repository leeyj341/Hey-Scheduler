package com.second.project.heysched.chatting.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.second.project.heysched.R;
import com.second.project.heysched.chatting.chat.MessageActivity;
import com.second.project.heysched.chatting.model.Usermodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_people,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.peoplefragment_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());



        FloatingActionButton floatingActionButton=view.findViewById(R.id.peoplefragment_floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),SelectFriendActivity.class));
            }
        });

        FloatingActionButton floatingComment_Btn=view.findViewById(R.id.peoplefragment_commentButton);
        floatingComment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v.getContext());
            }
        });

        return view;
    }

    void showDialog(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.dialog_comment,null);
        final EditText editText=view.findViewById(R.id.commentDialog_edittext);
        builder.setView(view).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String,Object> stringObjectMap=new HashMap<>();

                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                stringObjectMap.put("comment",editText.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(stringObjectMap);

            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Usermodel> usermodels;
        public PeopleFragmentRecyclerViewAdapter() {
            usermodels=new ArrayList<>();
            final String myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //누적데이터를 없애기 위해서 클리어 (지웠다가 확인해보면 정확히 알수있다.)
                    usermodels.clear();

                    //서버에서 온 데이터를 담는다.
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                        //내 아이디는 친구리스트에 안담는것
                        Usermodel usermodel=snapshot.getValue((Usermodel.class));
                        if(usermodel.uid.equals(myUid)){

                            continue;
                        }

                        usermodels.add(snapshot.getValue(Usermodel.class));
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);


            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            Glide.with(holder.itemView.getContext()).load(usermodels.get(position).profileImageUrl).apply(new RequestOptions().circleCrop()).into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(usermodels.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), MessageActivity.class);

                    // 위에 인텐트 선언했으므로 클릭한 상대방의 채팅방(MessageActivity)으로 옮긴다
                    intent.putExtra("destinationUid",usermodels.get(position).uid);

                    ActivityOptions activityOptions=null;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions=ActivityOptions.makeCustomAnimation(v.getContext(),R.anim.fromright,R.anim.toleft);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }
            });
            if (usermodels.get(position).comment!=null){
                ((CustomViewHolder) holder).textView_comment.setText(usermodels.get(position).comment);
            }else{
                ((CustomViewHolder) holder).textView_comment.setText("");
            }


        }

        @Override
        public int getItemCount() {
            return usermodels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public TextView textView_comment;

            public CustomViewHolder(View view) {
                super(view);
                imageView=view.findViewById(R.id.frienditem_imageview);
                textView=view.findViewById(R.id.frienditem_textview);
                textView_comment=view.findViewById(R.id.frienditem_textview_comment);
            }
        }
    }
}