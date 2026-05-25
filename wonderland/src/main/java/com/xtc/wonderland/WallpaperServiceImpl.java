package com.xtc.wonderland;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.xtc.common.WallpaperServiceProxy;
import com.xtc.common.WatchDataBase;
import com.xtc.common.WeakHandler;
import com.xtc.wonderland.XTCBaseWallpaperService2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
public class WallpaperServiceImpl extends XTCBaseWallpaperService2 {
    public static final int ANIMATION_COUNT = 20;
    public static final int ANIMATION_INTERVAL = 40;
    public static final int REFRESH_INTERVAL = 3000;
    public static final String TAG = "WallpaperServiceImpl_wonderland";
    private String path;
    private Resources resources;

    public WallpaperServiceImpl(Context context, String path) {
        super(context);
        this.path = path;
        try {
            this.resources = initResources(context);
        } catch (Exception ignore) {}
    }

    @Override // com.xtc.wonderland.XTCBaseWallpaperService2, com.xtc.common.WallpaperServiceProxy, android.service.wallpaper.WallpaperService
    public WallpaperServiceProxy.ActiveEngine onCreateEngine() {
        return new WonderlandEngine();
    }

    public class WonderlandEngine extends XTCBaseWallpaperService2.BaseEngine {
        private WeakHandler animationHandler;
        private WeakHandler refreshHandler;
        private WallpaperRender staticWallPaperRender;
        private boolean visible;

        public WonderlandEngine() {
            super();
            this.animationHandler = new WeakHandler();
            this.refreshHandler = new WeakHandler();
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            Log.i(WallpaperServiceImpl.TAG, "onCreate:");
            this.staticWallPaperRender = new WallpaperRender(WallpaperServiceImpl.this.resources, WallpaperServiceImpl.this.getApplicationContext());
            this.staticWallPaperRender.setBatteryLevel(WatchDataBase.getWatchBatteryLevel(WallpaperServiceImpl.this.getApplicationContext()));
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, com.xtc.common.callback.IDialCallback
        public void showWallpaper() {
            super.showWallpaper();
            Log.i(WallpaperServiceImpl.TAG, "showWallpaper");
            playAnimation();
        }

        private void playAnimation() {
            Log.i(WallpaperServiceImpl.TAG, "start play animation");
            this.staticWallPaperRender.setAnimationStep(0);
            drawFrame();
            this.animationHandler.removeCallbacksAndMessages(null);
            this.animationHandler.post(createAnimationTask());
        }

        private void startRefreshDial() {
            this.refreshHandler.removeCallbacksAndMessages(null);
            this.refreshHandler.postDelayed(createRefreshTask(), 3000L);
        }

        private void stopRefresh() {
            WeakHandler weakHandler = this.refreshHandler;
            if (weakHandler != null) {
                weakHandler.removeCallbacksAndMessages(null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Runnable createRefreshTask() {
            Runnable runnable = new Runnable() { // from class: com.xtc.wonderland.WallpaperServiceImpl.WonderlandEngine.1
                @Override // java.lang.Runnable
                public void run() {
                    if (WonderlandEngine.this.refreshHandler != null) {
                        WonderlandEngine.this.refreshHandler.removeCallbacksAndMessages(null);
                        WonderlandEngine.this.drawFrame();
                        WonderlandEngine.this.refreshHandler.postDelayed(WonderlandEngine.this.createRefreshTask(), 3000L);
                    }
                }
            };
            return runnable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Runnable createAnimationTask() {
            Runnable runnable = new Runnable() { // from class: com.xtc.wonderland.WallpaperServiceImpl.WonderlandEngine.2
                @Override // java.lang.Runnable
                public void run() {
                    Log.i(WallpaperServiceImpl.TAG, "run");
                    if (WonderlandEngine.this.animationHandler != null) {
                        WonderlandEngine.this.animationHandler.removeCallbacksAndMessages(null);
                        if (WonderlandEngine.this.staticWallPaperRender.getAnimationStep() >= 20) {
                            WonderlandEngine.this.staticWallPaperRender.setAnimationStep(-1);
                            WonderlandEngine.this.drawFrame();
                            Log.i(WallpaperServiceImpl.TAG, ">");
                            return;
                        }
                        Log.i(WallpaperServiceImpl.TAG, "run step:" + WonderlandEngine.this.staticWallPaperRender.getAnimationStep());
                        WonderlandEngine.this.animationHandler.postDelayed(WonderlandEngine.this.createAnimationTask(), 40L);
                        WonderlandEngine.this.drawFrame();
                        int step = WonderlandEngine.this.staticWallPaperRender.getAnimationStep() + 1;
                        WonderlandEngine.this.staticWallPaperRender.setAnimationStep(step);
                        return;
                    }
                    Log.i(WallpaperServiceImpl.TAG, "null");
                }
            };
            return runnable;
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, com.xtc.common.callback.IDialCallback
        public void updateBattery(int batteryLevel) {
            super.updateBattery(batteryLevel);
            this.staticWallPaperRender.setBatteryLevel(batteryLevel);
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(SurfaceHolder holder) {
            this.isNeedBattery = true;
            super.onSurfaceCreated(holder);
            Log.i(WallpaperServiceImpl.TAG, "onSurfaceCreated: ");
            drawFrame();
            playAnimation();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(WallpaperServiceImpl.TAG, "onSurfaceChanged: ");
            drawFrame();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.i(WallpaperServiceImpl.TAG, "onSurfaceDestroyed:" + this.visible);
            this.visible = false;
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            super.onDestroy();
            Log.i(WallpaperServiceImpl.TAG, "onDestroy:" + this.visible);
            this.visible = false;
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, android.service.wallpaper.WallpaperService.Engine
        public void onVisibilityChanged(boolean isVisible) {
            this.visible = isVisible;
            Log.i(WallpaperServiceImpl.TAG, "onVisibilityChanged:" + this.visible);
            if (this.visible) {
                new Handler().postDelayed(new Runnable() { // from class: com.xtc.wonderland.WallpaperServiceImpl.WonderlandEngine.3
                    @Override // java.lang.Runnable
                    public void run() {
                        WonderlandEngine.this.drawFrame();
                    }
                }, 200L);
                startRefreshDial();
            } else {
                stopRefresh();
            }
        }

        @Override // com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine, com.xtc.common.callback.IDialCallback
        public void offScreen() {
            super.offScreen();
            clearScreen();
        }

        private void clearScreen() {
            drawFrame(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drawFrame() {
            drawFrame(false);
        }

        private void drawFrame(boolean clean) {
            Log.i(WallpaperServiceImpl.TAG, "drawFrame clean" + clean);
            if (!this.visible && !clean) {
                return;
            }
            SurfaceHolder mHolder = getSurfaceHolder();
            Canvas canvas = null;
            Bitmap bitmap = null;
            try {
                canvas = mHolder.lockCanvas();
                if (clean) {
                    canvas.drawColor(-16777216);
                } else if (this.staticWallPaperRender != null) {
                    this.staticWallPaperRender.onDraw(canvas);
                }
            } finally {
                if (canvas != null && mHolder.getSurface().isValid()) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
                if (0 != 0) {
                    bitmap.recycle();
                }
            }
        }
    }

    public Resources initResources(Context context) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            AssetManager e = (AssetManager) AssetManager.class.newInstance();
            Method addAssetPath = e.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(e, this.path);
            Resources superRes = context.getResources();
            return new Resources(e, superRes.getDisplayMetrics(), superRes.getConfiguration());
        } catch (Exception var5) {
            var5.printStackTrace();
            return getResources();
        }
    }
}