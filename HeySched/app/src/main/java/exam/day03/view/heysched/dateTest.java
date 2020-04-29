package exam.day03.view.heysched;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateTest extends AppCompatActivity {
    EditText plan_title;
    ImageView color_picker;
    EditText plan_start_date;
    EditText plan_end_date;
    TextView plan_location;
    InputMethodManager imm;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plan);

        plan_title = findViewById(R.id.plan_title);
        color_picker = findViewById(R.id.color_picker);
        plan_start_date = findViewById(R.id.plan_start_date);
        plan_end_date = findViewById(R.id.plan_end_date);
        plan_location = findViewById(R.id.plan_location);
        plan_end_date.setShowSoftInputOnFocus(false);
        plan_start_date.setShowSoftInputOnFocus(false);

        color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 날짜 선택시 키보드 없어지게
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        // 날짜 다이얼로그 띄우기
        plan_start_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard();
                    showDateDialog((EditText) v);
                }
            }
        });
        plan_end_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard();
                    showDateDialog((EditText) v);
                }
            }
        });



    }

    public void showDateDialog(final EditText v) {
        Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR);
        int nMon = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String strDate = String.valueOf(year) + "/";
                        strDate += String.valueOf(monthOfYear + 1) + "/";
                        strDate += String.valueOf(dayOfMonth);


                        String[] time = showTimeDialog();
                        strDate += time[0];

                        v.setText(strDate);


                    }

                };

        DatePickerDialog oDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mDateSetListener, nYear, nMon, nDay);
        oDialog.show();


    }

    public String[] showTimeDialog(){
        SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm");
        //time[0] : hour
        //time[1] : min
        String[] time = dayTime.format(new Date(System.currentTimeMillis())).split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        final String[] strTime = {""};
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                strTime[0] = String.valueOf(hourOfDay)+":";
                strTime[0] += String.valueOf(hourOfDay);
            }
        };
        TimePickerDialog tPd = new TimePickerDialog(this, android.R.style.Theme_DeviceDefault_DayNight,onTimeSetListener, hour, min,true);
        tPd.show();
        return strTime;
    }

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(plan_start_date.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(plan_end_date.getWindowToken(), 0);
    }


}
