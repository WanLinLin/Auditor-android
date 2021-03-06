package com.example.auditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.auditor.ShowScoreActivity;

/**
 * Created by Wan Lin on 15/8/4.
 * An octave or perfect octave is the interval between one musical pitch and another with half or
 * double its frequency.
 */
public class OctaveView extends View {
    private Paint mPaint;
    private String octave;
    private int dotCount;
    private int dotRadius;
    private int space;
    private int padding;

    private int width;
    private int height;

    public OctaveView(Context context) {
        super(context);
        init();
    }

    public OctaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OctaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);

        width = ShowScoreActivity.NoteChildViewDimension.OCTAVE_VIEW_WIDTH;
        height = ShowScoreActivity.NoteChildViewDimension.OCTAVE_VIEW_HEIGHT;

        padding = Math.round(height * 0.1f);
        dotRadius = Math.round(height * 0.083f);
        space = Math.round((height - 2 * dotRadius - padding) / 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(octave.equals("")) setMeasuredDimension(0, 0);
        else {
            width = ShowScoreActivity.NoteChildViewDimension.OCTAVE_VIEW_WIDTH;
            height = ShowScoreActivity.NoteChildViewDimension.OCTAVE_VIEW_HEIGHT;

            padding = Math.round(height * 0.1f);
            dotRadius = Math.round(height * 0.083f);
            space = Math.round((height - 2 * dotRadius - padding) / 3);

            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(octave.equals("")) return;
        if(Integer.parseInt(octave) == 4) return;

        super.onDraw(canvas);

        int center_x = getWidth() / 2;
        int center_y = getHeight();
        float tab = space;

        if(Integer.parseInt(octave) > 4 && Integer.parseInt(octave) <= 8) {
            tab = -tab;
            center_y -= dotRadius;
        }
        else if(Integer.parseInt(octave) < 4 && Integer.parseInt(octave) >= 0){
            center_y = padding + dotRadius;
        }

        for(int i = 0; i < dotCount; i++) {
            canvas.drawCircle(center_x, center_y, dotRadius, mPaint);
            center_y += tab;
        }
    }

    public void setOctave(String octave) {
        this.octave = octave;

        if(octave.equals("")) return;
        dotCount = Math.abs(Integer.parseInt(octave) - 4);
    }

    public String getOctave() {
        return octave;
    }
}