package com.example.auditor.score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Wan Lin on 15/8/4.
 * Draw the number note
 */
public class NumberNoteView extends View {
    private String note;
    private Paint mPaint;

    public NumberNoteView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // env init
        mPaint.setTextSize(getMeasuredHeight());
        mPaint.setTextAlign(Paint.Align.CENTER);
        int x = canvas.getWidth() / 2;
        int y = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

        switch(note) {
            case "C":
                canvas.drawText("1", x, y, mPaint);
                break;
            case "D":
                canvas.drawText("2", x, y, mPaint);
                break;
            case "E":
                canvas.drawText("3", x, y, mPaint);
                break;
            case "F":
                canvas.drawText("4", x, y, mPaint);
                break;
            case "G":
                canvas.drawText("5", x, y, mPaint);
                break;
            case "A":
                canvas.drawText("6", x, y, mPaint);
                break;
            case "B":
                canvas.drawText("7", x, y, mPaint);
                break;
            case "R":
                canvas.drawText("0", x, y, mPaint);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
    }

    public void setNote(String note) {
        this.note = note;
    }
}