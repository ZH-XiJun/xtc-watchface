package com.xtc.wonderland;

import android.graphics.Bitmap;

public class BitmapComponent {
    private Bitmap bitmap;
    private Integer res;

    public BitmapComponent() {}

    public Bitmap getBitmap() { return bitmap; }
    public Integer getRes() { return res; }
    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }
    public void setRes(Integer res) { this.res = res; }
}
