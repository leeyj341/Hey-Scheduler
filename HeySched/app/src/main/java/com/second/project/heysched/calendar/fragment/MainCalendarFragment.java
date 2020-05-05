package com.second.project.heysched.calendar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.second.project.heysched.R;
import com.second.project.heysched.calendar.EventDecorator;
import com.second.project.heysched.calendar.PlanTask;
import com.second.project.heysched.calendar.SaturdayDecorator;
import com.second.project.heysched.calendar.SundayDecorator;
import com.second.project.heysched.plan.AddPlanActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCalendarFragment extends Fragment implements OnMonthChangedListener,
                                                            OnDateSelectedListener {
    FloatingActionButton button;
    RecyclerView recyclerView;

    MaterialCalendarView materialCalendarView;
    CalendarDay selectedDate;
    List<Integer> colorList;

    public MainCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        setCalendar(view);
        String endDate = getYear() + "/" + getMonth() + "/" + getLastDayOfMonth(CalendarDay.today());
        new CalendarSimulator().execute(getYear() + "/" + getMonth() + "/0" + 1, endDate);
        new PlanTask(getActivity(), R.layout.plan_row, recyclerView).execute(getYear() + "/" + getMonth() + "/" + getToday());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setCalendar(View view) {
        materialCalendarView = view.findViewById(R.id.main_calendar);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date;
                Log.d("test", selectedDate + "");
            }
        });
        //materialCalendarView.setTopbarVisible(false);

        materialCalendarView.setSelectedDate(CalendarDay.today());

        materialCalendarView.state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                .setMinimumDate(CalendarDay.from(getYear() - 1, getMonth(), 1))
                .setMaximumDate(CalendarDay.from(getYear() + 1, getMonth(), 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        materialCalendarView.setPadding(0,-20,0,30);
        //materialCalendarView.setDateTextAppearance();

        materialCalendarView.addDecorators(new SaturdayDecorator(),
                new SundayDecorator());

        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setOnDateChangedListener(this);


        //materialCalendarView.addDecorator(new EventDecorator(getActivity(), R.color.color1, selectedDate));
    }

    public void setView(View view) {
        button = view.findViewById(R.id.main_floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlanActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.schedList);

    }


    public int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(calendar.YEAR);
    }
    public int getMonth() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(calendar.MONTH) + 1;
    }
    public int getLastDayOfMonth(CalendarDay date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(date.getYear(), date.getMonth() - 1, date.getDay());
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public int getToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(calendar.DATE);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        String startDate = date.getYear() + "/" + date.getMonth() + "/0" + date.getDay();
        String endDate = date.getYear() + "/" + date.getMonth() + "/" + getLastDayOfMonth(date);
        new CalendarSimulator().execute(startDate, endDate);
        if(date.getMonth() == CalendarDay.today().getMonth()){
            materialCalendarView.setSelectedDate(CalendarDay.today());
            startDate = date.getYear() + "/" + date.getMonth() + "/" + CalendarDay.today().getDay();
        } else {
            materialCalendarView.setSelectedDate(date.getDate());
        }
        new PlanTask(getActivity(), R.layout.plan_row, recyclerView).execute(startDate);

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String startDate = date.getYear() + "/" + date.getMonth() + "/" + date.getDay();
        Log.d("test", startDate);
        new PlanTask(getActivity(), R.layout.plan_row, recyclerView).execute(startDate);
    }

    class CalendarSimulator extends AsyncTask<String, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(String... date) {
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<CalendarDay>();
            ArrayList<CalendarDay> dotDates = new ArrayList<CalendarDay>();

            //디비에서 불러온 플랜 리스트를 띄움
            URL url = null;
            JSONObject json = new JSONObject();
            String result = "";
            try {
                json.put("startdatetime", date[0]);
                json.put("enddatetime",date[1]);
                /*Log.d("test", date[0]);
                Log.d("test", date[1]);*/

                url = new URL("http://172.30.1.46:8088/heyScheduler/calendar/select.do");

                OkHttpClient client = new OkHttpClient();
                String calendarInfo = json.toString();

                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), calendarInfo))
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                JSONArray array = new JSONArray(result);

                int dayCount = 0;
                int curDay = 1;
                if(colorList != null) {
                    colorList.clear();
                }
                colorList = new ArrayList<Integer>();
                for(int i = 0; i < array.length(); i++) {
                    StringTokenizer stk =
                            new StringTokenizer(array.getJSONObject(i).getString("startdatetime"),"-");
                    int year = Integer.parseInt(stk.nextToken());
                    int month = Integer.parseInt(stk.nextToken());
                    int day = Math.abs(Integer.parseInt(stk.nextToken(" ")));
                    String time = stk.nextToken();
                    /*Log.d("test", year + "," + month + "," + day + "," + time);*/

                    dates.add(CalendarDay.from(year, month, day));
                    if(curDay == day && dayCount < 3) {
                        colorList.add(Color.parseColor(array.getJSONObject(i).getString("color")));
                        dotDates.add(CalendarDay.from(year, month, day));
                        dayCount++;
                    } else if(curDay != day) {
                        colorList.add(Color.parseColor(array.getJSONObject(i).getString("color")));
                        dotDates.add(CalendarDay.from(year, month, day));
                        dayCount = 1;
                        curDay = day;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return dotDates;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            //List<Integer> colorList = new ArrayList<Integer>();
            if(calendarDays.size() == 0) return;

            int curDay = calendarDays.get(0).getDay();
            ArrayList<Integer> newColorList = new ArrayList<Integer>();
            ArrayList<CalendarDay> newDayList = new ArrayList<CalendarDay>();
            for (int i = 0; i < calendarDays.size(); i++) {
                if(curDay != calendarDays.get(i).getDay()) {
                    /*Log.d("test", newColorList.size()+"");
                    Log.d("test", newDayList.size()+"");
                    Log.d("test", i +"번째");*/
                    materialCalendarView.addDecorator(new EventDecorator(getActivity(), newColorList, newDayList));
                    newColorList = new ArrayList<Integer>();
                    newDayList = new ArrayList<CalendarDay>();
                    curDay = calendarDays.get(i).getDay();
                }
                newColorList.add(colorList.get(i));
                newDayList.add(calendarDays.get(i));
            }
            materialCalendarView.addDecorator(new EventDecorator(getActivity(), newColorList, newDayList));

        }
    }
}
