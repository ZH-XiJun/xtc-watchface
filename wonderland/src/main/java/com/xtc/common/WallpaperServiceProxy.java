package com.xtc.common;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.Log;

/* loaded from: classes3.dex */
public class WallpaperServiceProxy extends WallpaperService {
    public WallpaperServiceProxy(Context context, String path) {
        attachBaseContext(context);
        Log.d("WallpaperServiceProxy", "zjj, WallpaperServiceProxy2");
    }

    public WallpaperServiceProxy(Context context) {
        attachBaseContext(context);
        Log.d("WallpaperServiceProxy", "zjj, WallpaperServiceProxy");
    }

    @Override // android.service.wallpaper.WallpaperService
    public WallpaperService.Engine onCreateEngine() {
        return null;
    }

    public class ActiveEngine extends WallpaperService.Engine {
        public ActiveEngine() {
            super();
            //super(WallpaperServiceProxy.this);
        }
    }
}