package com.second.project.heysched.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomMultipleDotSpan implements LineBackgroundSpan {
    private float radius;
    private List<Integer> colors;

    public CustomMultipleDotSpan(List<Integer> colors) {
        this.colors = colors;
    }

    public CustomMultipleDotSpan(float radius, List<Integer> colors) {
        this.radius = radius;
        this.colors = colors;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int total = colors.size() > 2 ? 3 : colors.size();
        int leftMost = (total - 1) * 12;

        for (int i = 0; i < total; i++) {
            if(colors.get(i) != 0) {
                //paint.setColor(Color.CYAN);
                paint.setColor(colors.get(i));
            }
            canvas.drawCircle((left + right) / 2 - (float)leftMost, bottom + radius, radius, paint);
            leftMost -= 24;
        }
    }
}
