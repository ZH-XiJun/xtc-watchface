package com.xtc.dial.konata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BitmapManagerInstance {
    public static final String TAG = "BitmapManagerInstance";

    private Map<String, BitmapComponent> bitmapComponentMap;

    public BitmapManagerInstance() {
        bitmapComponentMap = new HashMap<>();
    }

    private Bitmap getBitmapById(Resources resources, int resId) {
        try {
            return BitmapFactory.decodeResource(resources, resId);
        } catch (Exception e) {
            Log.i(TAG, "getBitmapById error");
            return null;
        }
    }

    public Bitmap getBitmapCache(Resources resources, String key, Integer resId) {
        BitmapComponent component = bitmapComponentMap.get(key);
        if (component == null) {
            Log.i(TAG, "component:" + key + "  bitmap item null");
            component = new BitmapComponent();
            Bitmap bitmap = getBitmapById(resources, resId.intValue());
            component.setBitmap(bitmap);
            component.setRes(resId);
            bitmapComponentMap.put(key, component);
            return bitmap;
        }

        Integer oldRes = component.getRes();
        Bitmap oldBitmap = component.getBitmap();

        if (oldRes != null && oldRes.equals(resId)) {
            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                return oldBitmap;
            }
            Log.i(TAG, "bitmap null");
            Bitmap bitmap = getBitmapById(resources, resId.intValue());
            component.setBitmap(bitmap);
            return bitmap;
        }

        Log.i(TAG, "bitmap change");
        Bitmap newBitmap = getBitmapById(resources, resId.intValue());
        component.setBitmap(newBitmap);
        component.setRes(resId);
        return newBitmap;
    }
}
