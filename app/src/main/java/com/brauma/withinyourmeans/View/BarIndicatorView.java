package com.brauma.withinyourmeans.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.brauma.withinyourmeans.Model.Bar;
import com.brauma.withinyourmeans.R;

import java.util.ArrayList;
import java.util.Collections;

public class BarIndicatorView extends View {
    private ArrayList<Bar> bars;
    private int mainValue;
    private int mainBarColor;
    private Paint paint;
    private Context context;

    public BarIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        bars = new ArrayList<>();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BarIndicatorView,
                0, 0);
        try {
            mainBarColor = a.getInteger(R.styleable.BarIndicatorView_mainBarColor, Color.BLACK);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int currentX = 0;

        paint.setColor(mainBarColor);
        paint.setStyle(Paint.Style.FILL);
        Log.e("BAR", String.valueOf(bars.size()));

        canvas.drawRect(0, 0, width, height, paint);

        for(int i = 0; i < bars.size(); i++){
            double ratio = (double)(bars.get(i).getValue()) / (double)(mainValue);
            int barWidth = (int) (ratio * (double) width);

            paint.setColor(bars.get(i).getColor());

            canvas.drawRect(currentX, 0, barWidth + currentX, height, paint);

            currentX = currentX + barWidth;
        }
    }

    public ArrayList<Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
        Collections.sort(this.bars);
    }

    public int getMainValue() {
        return mainValue;
    }

    public void setMainValue(int mainValue) {
        this.mainValue = mainValue;
    }

    public int getMainBarColor() {
        return mainBarColor;
    }

    public void setMainBarColor(int colorOfMainBar) {
        this.mainBarColor = colorOfMainBar;
        invalidate();
        requestLayout();
    }
}
