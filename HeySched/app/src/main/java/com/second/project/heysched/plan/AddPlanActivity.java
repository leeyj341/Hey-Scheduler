package com.second.project.heysched.plan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.second.project.heysched.R;
import com.second.project.heysched.alarm.AlarmStart;
import com.second.project.heysched.calendar.adapter.PlanItem;
import com.second.project.heysched.chatting.model.Usermodel;
import com.second.project.heysched.plan.adapter.InvitedProfileAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class AddPlanActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    // views
    EditText plan_title;
    ImageView color_picker;
    EditText plan_start_date;
    EditText plan_start_time;
    EditText plan_end_date;
    EditText plan_end_time;
    InputMethodManager imm;
    TextView plan_friends;
    EditText memo;
    ImageView recommand_btn;
    TextView plan_location;
    RecyclerView invited_img_layout;
    LayoutInflater inflater;

    Button ok_btn;
    Button cancle_btn;

    String sColor="#F15F5F";
    String place_location="";
    String startdatetime="";
    String enddatetime="";
    ArrayList<String> guest_ids;

    // data model
    PlanItem plan;

    // intent code
    public static final int SEARCH_LOCATION_BTN=1000;
    public static final int INVITE_FRIENDS_BTN=2000;

    String addr="";

    InvitedProfileAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plan);
        FirebaseUser myuid=FirebaseAuth.getInstance().getCurrentUser();


        plan_title = findViewById(R.id.plan_title);
        color_picker = findViewById(R.id.color_picker);
        plan_start_date = findViewById(R.id.plan_start_date);
        plan_start_time = findViewById(R.id.plan_start_time);
        plan_end_date = findViewById(R.id.plan_end_date);
        plan_end_time = findViewById(R.id.plan_end_time);
        recommand_btn = findViewById(R.id.recommand_btn);
        plan_location = findViewById(R.id.plan_location);
        //find_location = findViewById(R.id.find_location);
        plan_friends = findViewById(R.id.plan_friends);
        invited_img_layout = findViewById(R.id.invited_img_layout);
        memo = findViewById(R.id.memo);

        // 확인
        ok_btn = findViewById(R.id.ok);
        // 취소
        cancle_btn = findViewById(R.id.cancle);

        plan_start_date.setShowSoftInputOnFocus(false);
        plan_end_date.setShowSoftInputOnFocus(false);
        plan_start_time.setShowSoftInputOnFocus(false);
        plan_end_time.setShowSoftInputOnFocus(false);

        //친구 초대시 사진 보여주기
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 색
        color_picker.setOnClickListener(this);

        // 날짜 선택시 키보드 없어지게
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 날짜 다이얼로그 띄우기
        plan_start_date.setOnFocusChangeListener(this);
        plan_start_time.setOnFocusChangeListener(this);
        plan_end_date.setOnFocusChangeListener(this);
        plan_end_time.setOnFocusChangeListener(this);

        // 장소 찾기
        recommand_btn.setOnClickListener(this);

        // 친구 초대
        plan_friends.setOnClickListener(this);


        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);

        /*PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)

                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("plz...","Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("plz...","An error occurred: " + status);
            }
        });*/
        FirebaseDatabase.getInstance().getReference().child("users").child(myuid.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usermodel me=dataSnapshot.getValue(Usermodel.class);
                addr = me.address;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_picker:
                openColorPicker();
                break;

            case R.id.recommand_btn:
                Log.d("clickEvent","clicked!");
                findLocation();
                break;

            case R.id.plan_friends:
                inviteFriends();
                break;
            case R.id.ok:
                save();
                break;
            case R.id.cancle:
                finish();
                break;

        }
    }

    private void save(){
        Log.d("insertToSTS","AddPlanActivity save");
        //db에 저장
        plan = new PlanItem();
        plan.setTitle(plan_title.getText().toString());
        plan.setStartdatetime(startdatetime);
        plan.setEnddatetime(enddatetime);
        plan.setLocation(place_location);
        plan.setContent(memo.getText().toString());
        plan.setColor(sColor);
        plan.setGuest_id(guest_ids);
        plan.setHost_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new PlanInsert().execute(plan);

        //알람 등록
        setAlarm(startdatetime, plan);

        Log.d("insertToSTS","AddPlanActivity done");
        finish();
    }

    public void setAlarm(String startdatetime, PlanItem item) {
        if(addr.equals("")) return;
        Log.d("test", addr);
        Log.d("test", item.toString());
        new AlarmStart(this).set(startdatetime, item, addr);
    }

    private void inviteFriends(){
        Intent intent = new Intent(getApplicationContext(),InviteFriendActivity.class);
        startActivityForResult(intent, INVITE_FRIENDS_BTN);
    }

    private void findLocation(){
        //Intent intent = new Intent(getApplicationContext(), SearchLocationActivity.class);
        Intent intent = new Intent(getApplicationContext(), SearchPlaceActivity.class);
        startActivityForResult(intent, SEARCH_LOCATION_BTN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            // 선택한 장소 받기
            case SEARCH_LOCATION_BTN:
                if(resultCode==RESULT_OK){
                    String place_title = data.getStringExtra("place_title");
                    place_location = data.getStringExtra("place_location");

                    plan_location.setText(place_title);
                }
                break;
            // 선택한 친구목록 받기
            case INVITE_FRIENDS_BTN:
                if(resultCode==RESULT_OK){
                    //체크한 친구들 화면에 추가
                    ArrayList<String> user_uids = data.getStringArrayListExtra("invited_user_ids");
                    ArrayList<String> user_imgs = data.getStringArrayListExtra("invited_user_imgs");
                    invited_img_layout.removeAllViews();
                    // Log!! return data check!
                    for(int i =0;i<user_uids.size();i++){
                        Log.d("invited_friends","uid : "+ user_uids.get(i)+"  profile : "+user_imgs.get(i));
                        Toast.makeText(this, "uid : "+ user_uids.get(i)+"  profile : "+user_imgs.get(i),Toast.LENGTH_SHORT).show();
                    }
                    guest_ids = user_uids;

                    // 초대한 친구 프로필 화면에 뿌리기
                    adapter = new InvitedProfileAdapter(this, R.layout.invited_imgview, user_imgs);

                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.HORIZONTAL);
                    invited_img_layout.setLayoutManager(llm);
                    invited_img_layout.setAdapter(adapter);
                    invited_img_layout.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                            super.getItemOffsets(outRect, view, parent, state);
                            outRect.right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getApplicationContext().getResources().getDisplayMetrics());
                            outRect.left = 0;
                        }
                    });



                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.plan_start_date:
            case R.id.plan_end_date:
                if (hasFocus) {
                    hideKeyboard();
                    showDateDialog((EditText) v);
                    v.clearFocus();
                }
                break;
            case R.id.plan_start_time:
            case R.id.plan_end_time:
                if (hasFocus) {
                    hideKeyboard();
                    showTimeDialog((EditText) v);
                    v.clearFocus();
                }
                break;
        }
    }

    // color picker
    private void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        color_picker.setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        // insert용
                        sColor = String.format("#%06X", color);
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    // date dialog
    private void showDateDialog(final EditText v) {
        Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR);
        int nMon = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String strDate = String.valueOf(year) + "-";
                        strDate += String.valueOf(monthOfYear + 1) + "-";
                        strDate += String.valueOf(dayOfMonth);

                        v.setText(strDate);

                        switch (v.getId()) {
                            case R.id.plan_start_date:
                                startdatetime=strDate;
                                plan_start_time.requestFocus();
                                break;
                            case R.id.plan_end_date:
                                enddatetime=strDate;
                                plan_end_time.requestFocus();
                                break;
                        }

                    }

                };

        if ((!v.getText().equals("")) && v.getText().length() > 0) {
            String[] selected_date = v.getText().toString().split("/");

            nYear = Integer.parseInt(selected_date[0]);
            nMon = Integer.parseInt(selected_date[1]);
            nDay = Integer.parseInt(selected_date[2]);

        }
        DatePickerDialog oDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mDateSetListener, nYear, nMon, nDay);
        oDialog.show();


    }

    // time dialog
    private void showTimeDialog(final EditText v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strTime = String.valueOf(hourOfDay) + ":";
                String min = "";
                if (minute < 10) {
                    min = "0" + String.valueOf(minute);
                } else {
                    min = String.valueOf(minute);
                }
                strTime += min;
                v.setText(strTime);
                switch (v.getId()){
                    case R.id.plan_start_time:
                        startdatetime+=" "+strTime;
                    case R.id.plan_end_time:
                        enddatetime+=" "+strTime;
                }
            }
        };

        if ((!v.getText().equals("")) && v.getText().length() > 0) {



            String[] selected_time = v.getText().toString().split(":");

            int hour = Integer.parseInt(selected_time[0]);
            int min = Integer.parseInt(selected_time[1]);

            TimePickerDialog tPd = new TimePickerDialog(
                    this,
                    android.R.style.Theme_DeviceDefault_DayNight,
                    onTimeSetListener,
                    hour,
                    min,
                    true);
            tPd.show();
        } else {
            TimePickerDialog tPd = new TimePickerDialog(
                    this,
                    android.R.style.Theme_DeviceDefault_DayNight,
                    onTimeSetListener,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true);
            tPd.show();
        }
        if (v.getId() == R.id.plan_end_time) {
            //timeCheck();
        }

    }

    private void timeCheck() {
        if ((!plan_start_time.getText().equals("")) && plan_start_time.getText().length() > 0) {
            String[] start = plan_start_time.getText().toString().split(":");
            String[] end = plan_end_time.getText().toString().split(":");

            if (Integer.parseInt(start[0]) > Integer.parseInt(end[0])) {
                int time = Integer.parseInt(end[0]) - 1;
                plan_start_time.setText(time + ":" + start[1]);
            }
        }
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(plan_start_date.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(plan_end_date.getWindowToken(), 0);
    }

}
