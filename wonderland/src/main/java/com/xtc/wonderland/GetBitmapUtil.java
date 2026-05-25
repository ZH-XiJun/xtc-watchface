package com.xtc.wonderland;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.xtc.system.motion.core.MotionState;
import com.xtc.wonderland.Constant;

/* loaded from: classes2.dex */
public class GetBitmapUtil {
    public static Bitmap getWeekBitmap(Resources resources, int weekOfDay, BitmapManagerInstance bitmapManagerInstance) {
        int resId = R.drawable.ic_i25_pointer_b_week_sun;
        switch (weekOfDay) {
            case 1:
                resId = R.drawable.ic_i25_pointer_b_week_sun;
                break;
            case 2:
                resId = R.drawable.ic_i25_pointer_b_week_mon;
                break;
            case 3:
                resId = R.drawable.ic_i25_pointer_b_week_tue;
                break;
            case 4:
                resId = R.drawable.ic_i25_pointer_b_week_wed;
                break;
            case 5:
                resId = R.drawable.ic_i25_pointer_b_week_thu;
                break;
            case 6:
                resId = R.drawable.ic_i25_pointer_b_week_fri;
                break;
            case 7:
                resId = R.drawable.ic_i25_pointer_b_week_sat;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, Constant.Component.COMPONENT_WEEK, Integer.valueOf(resId));
    }

    public static Bitmap getAnimationBitmap(Resources resources, int step, BitmapManagerInstance bitmapManagerInstance) {
        int resId;
        if (step == -1) {
            return bitmapManagerInstance.getBitmapCache(resources, Constant.Component.COMPONENT_BG, Integer.valueOf(R.drawable.all_background));
        }
        switch (step) {
            case 0:
                resId = R.drawable.yhlm_0;
                break;
            case 1:
                resId = R.drawable.yhlm_1;
                break;
            case 2:
                resId = R.drawable.yhlm_2;
                break;
            case 3:
                resId = R.drawable.yhlm_3;
                break;
            case 4:
                resId = R.drawable.yhlm_4;
                break;
            case 5:
                resId = R.drawable.yhlm_5;
                break;
            case 6:
                resId = R.drawable.yhlm_6;
                break;
            case 7:
                resId = R.drawable.yhlm_7;
                break;
            case MotionState.SENSOR_STATE_8 /* 8 */:
                resId = R.drawable.yhlm_8;
                break;
            case MotionState.SENSOR_STATE_9 /* 9 */:
                resId = R.drawable.yhlm_9;
                break;
            case MotionState.SENSOR_STATE_10 /* 10 */:
                resId = R.drawable.yhlm_10;
                break;
            case MotionState.SENSOR_STATE_11 /* 11 */:
                resId = R.drawable.yhlm_11;
                break;
            case MotionState.SENSOR_STATE_12 /* 12 */:
                resId = R.drawable.yhlm_12;
                break;
            case MotionState.SENSOR_STATE_13 /* 13 */:
                resId = R.drawable.yhlm_13;
                break;
            case 14:
                resId = R.drawable.yhlm_14;
                break;
            case 15:
                resId = R.drawable.yhlm_15;
                break;
            case 16:
                resId = R.drawable.yhlm_16;
                break;
            case 17:
                resId = R.drawable.yhlm_17;
                break;
            case 18:
                resId = R.drawable.yhlm_18;
                break;
            case 19:
                resId = R.drawable.yhlm_19;
                break;
            case WallpaperServiceImpl.ANIMATION_COUNT /* 20 */:
                resId = R.drawable.yhlm_20;
                break;
            default:
                resId = R.drawable.all_background;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, Constant.Component.COMPONENT_BG, Integer.valueOf(resId));
    }

    public static Bitmap getTimeNumId(Resources resources, int num, String component, BitmapManagerInstance bitmapManagerInstance) {
        int resId;
        switch (num) {
            case 1:
                resId = R.drawable.ic_i25_pointer_b_time_1;
                break;
            case 2:
                resId = R.drawable.ic_i25_pointer_b_time_2;
                break;
            case 3:
                resId = R.drawable.ic_i25_pointer_b_time_3;
                break;
            case 4:
                resId = R.drawable.ic_i25_pointer_b_time_4;
                break;
            case 5:
                resId = R.drawable.ic_i25_pointer_b_time_5;
                break;
            case 6:
                resId = R.drawable.ic_i25_pointer_b_time_6;
                break;
            case 7:
                resId = R.drawable.ic_i25_pointer_b_time_7;
                break;
            case MotionState.SENSOR_STATE_8 /* 8 */:
                resId = R.drawable.ic_i25_pointer_b_time_8;
                break;
            case MotionState.SENSOR_STATE_9 /* 9 */:
                resId = R.drawable.ic_i25_pointer_b_time_9;
                break;
            default:
                resId = R.drawable.ic_i25_pointer_b_time_0;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, component, Integer.valueOf(resId));
    }

    public static Bitmap getMonthBitmap(Resources resources, int month, String component, BitmapManagerInstance bitmapManagerInstance) {
        int resId;
        switch (month) {
            case 1:
                resId = R.drawable.ic_i25_pointer_b_month_1;
                break;
            case 2:
                resId = R.drawable.ic_i25_pointer_b_month_2;
                break;
            case 3:
                resId = R.drawable.ic_i25_pointer_b_month_3;
                break;
            case 4:
                resId = R.drawable.ic_i25_pointer_b_month_4;
                break;
            case 5:
                resId = R.drawable.ic_i25_pointer_b_month_5;
                break;
            case 6:
                resId = R.drawable.ic_i25_pointer_b_month_6;
                break;
            case 7:
                resId = R.drawable.ic_i25_pointer_b_month_7;
                break;
            case MotionState.SENSOR_STATE_8 /* 8 */:
                resId = R.drawable.ic_i25_pointer_b_month_8;
                break;
            case MotionState.SENSOR_STATE_9 /* 9 */:
                resId = R.drawable.ic_i25_pointer_b_month_9;
                break;
            case MotionState.SENSOR_STATE_10 /* 10 */:
                resId = R.drawable.ic_i25_pointer_b_month_10;
                break;
            case MotionState.SENSOR_STATE_11 /* 11 */:
                resId = R.drawable.ic_i25_pointer_b_month_11;
                break;
            case MotionState.SENSOR_STATE_12 /* 12 */:
                resId = R.drawable.ic_i25_pointer_b_month_12;
                break;
            default:
                resId = R.drawable.ic_i25_pointer_b_month_1;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, Constant.Component.COMPONENT_MONTH, Integer.valueOf(resId));
    }

    public static Bitmap getDateNumBitmap(Resources resources, int num, String component, BitmapManagerInstance bitmapManagerInstance) {
        int resId;
        switch (num) {
            case 1:
                resId = R.drawable.ic_i25_pointer_b_date_1;
                break;
            case 2:
                resId = R.drawable.ic_i25_pointer_b_date_2;
                break;
            case 3:
                resId = R.drawable.ic_i25_pointer_b_date_3;
                break;
            case 4:
                resId = R.drawable.ic_i25_pointer_b_date_4;
                break;
            case 5:
                resId = R.drawable.ic_i25_pointer_b_date_5;
                break;
            case 6:
                resId = R.drawable.ic_i25_pointer_b_date_6;
                break;
            case 7:
                resId = R.drawable.ic_i25_pointer_b_date_7;
                break;
            case MotionState.SENSOR_STATE_8 /* 8 */:
                resId = R.drawable.ic_i25_pointer_b_date_8;
                break;
            case MotionState.SENSOR_STATE_9 /* 9 */:
                resId = R.drawable.ic_i25_pointer_b_date_9;
                break;
            default:
                resId = R.drawable.ic_i25_pointer_b_date_0;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, component, Integer.valueOf(resId));
    }

    public static Bitmap getPowerBitmap(Resources resources, int powerNum, String component, BitmapManagerInstance bitmapManagerInstance) {
        int resId = R.drawable.ic_i25_pointer_b_battery_01;
        switch (powerNum / 10) {
            case 0:
                resId = R.drawable.ic_i25_pointer_b_battery_01;
                break;
            case 1:
                resId = R.drawable.ic_i25_pointer_b_battery_02;
                break;
            case 2:
                resId = R.drawable.ic_i25_pointer_b_battery_03;
                break;
            case 3:
                resId = R.drawable.ic_i25_pointer_b_battery_04;
                break;
            case 4:
                resId = R.drawable.ic_i25_pointer_b_battery_05;
                break;
            case 5:
                resId = R.drawable.ic_i25_pointer_b_battery_06;
                break;
            case 6:
                resId = R.drawable.ic_i25_pointer_b_battery_07;
                break;
            case 7:
                resId = R.drawable.ic_i25_pointer_b_battery_08;
                break;
            case MotionState.SENSOR_STATE_8 /* 8 */:
                resId = R.drawable.ic_i25_pointer_b_battery_09;
                break;
            case MotionState.SENSOR_STATE_9 /* 9 */:
                resId = R.drawable.ic_i25_pointer_b_battery_10;
                break;
            case MotionState.SENSOR_STATE_10 /* 10 */:
                resId = R.drawable.ic_i25_pointer_b_battery_11;
                break;
        }
        return bitmapManagerInstance.getBitmapCache(resources, component, Integer.valueOf(resId));
    }
}