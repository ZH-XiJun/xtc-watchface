package com.xtc.dial.konata;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.format.DateFormat;
import android.util.Log;
import com.xtc.dial.konata.Constant;
import java.util.Calendar;

/* loaded from: classes2.dex */
public class WallpaperRender {
    public static final String TAG = "WallpaperRender";
    private int batteryLevel;
    private Paint bitmapPaint;
    private Context context;
    private Paint handPaint;
    private int hourOne;
    private int hourTen;
    private int minuteOne;
    private int minuteTen;
    private Resources resources;
    private int animationStep = -1;
    private BitmapManagerInstance bitmapManagerInstance = new BitmapManagerInstance();
    private Calendar calendar = Calendar.getInstance();
    LinearOutSlowInInterpolator linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();

    public WallpaperRender(Resources resources, Context context) {
        this.resources = resources;
        this.context = context;
        initPaint();
    }

    private void initPaint() {
        this.handPaint = new Paint(1);
        this.handPaint.setAntiAlias(true);
        this.handPaint.setFilterBitmap(true);
        this.bitmapPaint = new Paint(1);
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setAlpha(255);
    }

    public int getAnimationStep() {
        return this.animationStep;
    }

    public void setAnimationStep(int animationStep) {
        this.animationStep = animationStep;
    }

    public void onDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        this.calendar = Calendar.getInstance();
        drawbg(canvas);
        drawWeek(canvas);
        drawTime(canvas);
        drawPower(canvas);
        drawMonth(canvas);
        drawDate(canvas);
    }

    private void drawDate(Canvas canvas) {
        int day = this.calendar.get(5);
        int currentDayTen = day / 10;
        int currentDayUnit = day % 10;
        float tenAnimationLeft = 120 - ((1.0f - getAnimationPercentDelay100()) * 60.0f);
        float oneAnimationLeft = 143 - ((1.0f - getAnimationPercentDelay100()) * 60.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercentDelay100() * 255.0f));
        Bitmap dateTenBitmap = GetBitmapUtil.getDateNumBitmap(this.resources, currentDayTen, Constant.Component.COMPONENT_DATE_TEN, this.bitmapManagerInstance);
        Bitmap dateOneBitmap = GetBitmapUtil.getDateNumBitmap(this.resources, currentDayUnit, Constant.Component.COMPONENT_DATE_ONE, this.bitmapManagerInstance);
        drawBitmap(canvas, dateTenBitmap, (int) tenAnimationLeft, 316, this.bitmapPaint);
        drawBitmap(canvas, dateOneBitmap, (int) oneAnimationLeft, 316, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private void drawMonth(Canvas canvas) {
        int month = this.calendar.get(2) + 1;
        Bitmap bitmap = GetBitmapUtil.getMonthBitmap(this.resources, month, Constant.Component.COMPONENT_MONTH, this.bitmapManagerInstance);
        float animationLeft = 18 - ((1.0f - getAnimationPercentDelay100()) * 60.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercentDelay100() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 316, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawbg(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getAnimationBitmap(this.resources, this.animationStep, this.bitmapManagerInstance);
        drawBitmap(canvas, bitmap, 0, 0);
    }

    private void drawWeek(Canvas canvas) {
        int currentWeek = this.calendar.get(7);
        Bitmap bitmap = GetBitmapUtil.getWeekBitmap(this.resources, currentWeek, this.bitmapManagerInstance);
        float animationLeft = 18 - ((1.0f - getAnimationPercentDelay200()) * 60.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercentDelay200() * 255.0f));
        Log.i(TAG, "drawWeek: alpha:" + this.bitmapPaint.getAlpha());
        drawBitmap(canvas, bitmap, (int) animationLeft, 268, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawTime(Canvas canvas) {
        int hour;
        int minute = this.calendar.get(12);
        if (DateFormat.is24HourFormat(this.context)) {
            hour = this.calendar.get(11);
        } else {
            hour = this.calendar.get(10);
            if (hour == 0) {
                hour = 12;
            }
        }
        this.minuteTen = minute / 10;
        this.minuteOne = minute % 10;
        this.hourTen = hour / 10;
        this.hourOne = hour % 10;
        drawTimeHourTen(canvas);
        drawTimeHourOne(canvas);
        drawTimeMinuteTen(canvas);
        drawTimeMinuteOne(canvas);
    }

    private void drawTimeHourTen(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getTimeNumId(this.resources, this.hourTen, Constant.Component.COMPONENT_TIME_HOUR_TEN, this.bitmapManagerInstance);
        float animationLeft = 136 + ((1.0f - getAnimationPercent()) * 90.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercent() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 30, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private float getAnimationPercent() {
        int i = this.animationStep;
        if (i == -1 || i >= 10) {
            return 1.0f;
        }
        return this.linearOutSlowInInterpolator.getInterpolation((i * 1.0f) / 10.0f);
    }

    private float getAnimationPercentDelay100() {
        int i = this.animationStep;
        if (i == -1 || i >= 12) {
            return 1.0f;
        }
        if (i > 2) {
            return this.linearOutSlowInInterpolator.getInterpolation(((i - 2) * 1.0f) / 10.0f);
        }
        return 0.0f;
    }

    private float getAnimationPercentDelay200() {
        int i = this.animationStep;
        if (i == -1 || i >= 15) {
            return 1.0f;
        }
        if (i > 5) {
            return this.linearOutSlowInInterpolator.getInterpolation(((i - 5) * 1.0f) / 10.0f);
        }
        return 0.0f;
    }

    private void drawTimeHourOne(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getTimeNumId(this.resources, this.hourOne, Constant.Component.COMPONENT_TIME_HOUR_ONE, this.bitmapManagerInstance);
        float animationLeft = 226 + ((1.0f - getAnimationPercent()) * 90.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercent() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 30, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawTimeMinuteTen(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getTimeNumId(this.resources, this.minuteTen, Constant.Component.COMPONENT_TIME_MINUTE_TEN, this.bitmapManagerInstance);
        float animationLeft = 136 + ((1.0f - getAnimationPercent()) * 90.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercent() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 182, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawTimeMinuteOne(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getTimeNumId(this.resources, this.minuteOne, Constant.Component.COMPONENT_TIME_MINUTE_ONE, this.bitmapManagerInstance);
        float animationLeft = 226 + ((1.0f - getAnimationPercent()) * 90.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercent() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 182, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawPower(Canvas canvas) {
        Bitmap bitmap = GetBitmapUtil.getPowerBitmap(this.resources, this.batteryLevel, Constant.Component.COMPONENT_POWER, this.bitmapManagerInstance);
        float animationLeft = 186 - ((1.0f - getAnimationPercent()) * 60.0f);
        this.bitmapPaint.setAlpha((int) (getAnimationPercent() * 255.0f));
        drawBitmap(canvas, bitmap, (int) animationLeft, 331, this.bitmapPaint);
        this.bitmapPaint.setAlpha(255);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, int left, int top) {
        if (canvas == null || bitmap == null || bitmap.isRecycled()) {
            return;
        }
        canvas.drawBitmap(bitmap, left, top, (Paint) null);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, int left, int top, Paint paint) {
        if (canvas == null || bitmap == null || bitmap.isRecycled()) {
            return;
        }
        canvas.drawBitmap(bitmap, left, top, paint);
    }
}