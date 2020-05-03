package com.second.project.heysched.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.second.project.heysched.R;

import java.util.List;

public class EventDecorator implements DayViewDecorator {
    private Context context;
    private final Drawable drawable;
    private List<Integer> colors;
    private List<CalendarDay> dates;

    public EventDecorator(Context context, List<Integer> colors, List<CalendarDay> dates) {
        this.context = context;
        this.drawable = context.getResources().getDrawable(R.drawable.color_btn);
        this.colors = colors;
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.setSelectionDrawable(drawable);
        view.addSpan(new CustomMultipleDotSpan(8f, colors));
    }
}
