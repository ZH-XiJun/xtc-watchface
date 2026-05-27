package com.xtc.dial.konata;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.xtc.common.BaseProviderImpl;

public class ProviderImpl extends BaseProviderImpl {

    @Override
    public WallpaperService provideProxy(Context context, String path) {
        return new WallpaperServiceImpl(context, path);
    }
}
