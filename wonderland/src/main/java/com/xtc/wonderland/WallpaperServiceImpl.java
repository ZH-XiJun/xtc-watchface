package com.xtc.wonderland;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.xtc.common.WallpaperServiceProxy.ActiveEngine;
import com.xtc.common.WeakHandler;
import com.xtc.common.WatchDataBase;

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
        this.resources = initResources(context);
    }

    static Resources access$000(WallpaperServiceImpl instance) {
        return instance.resources;
    }

    public Resources initResources(Context context) {
        try {
            AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
            assetManager.getClass()
                    .getMethod("addAssetPath", String.class)
                    .invoke(assetManager, path);
            Resources res = context.getResources();
            return new Resources(assetManager,
                    res.getDisplayMetrics(),
                    res.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
            return getResources();
        }
    }

    @Override
    public ActiveEngine onCreateEngine() {
        return new WonderlandEngine();
    }

    public class WonderlandEngine extends XTCBaseWallpaperService2.BaseEngine {
        private WeakHandler animationHandler;
        private WeakHandler refreshHandler;
        private WallpaperRender staticWallPaperRender;
        private boolean visible;

        public WonderlandEngine() {
            animationHandler = new WeakHandler();
            refreshHandler = new WeakHandler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Log.i(TAG, "onCreate:");
            staticWallPaperRender = new WallpaperRender(
                    WallpaperServiceImpl.this.resources,
                    WallpaperServiceImpl.this.getApplicationContext());
            staticWallPaperRender.setBatteryLevel(
                    WatchDataBase.getWatchBatteryLevel(
                            WallpaperServiceImpl.this.getApplicationContext()));
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            isNeedBattery = true;
            super.onSurfaceCreated(holder);
            Log.i(TAG, "onSurfaceCreated:");
            drawFrame();
            playAnimation();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "onSurfaceChanged:");
            drawFrame();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.i(TAG, "onSurfaceDestroyed:" + visible);
            visible = false;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            Log.i(TAG, "onVisibilityChanged:" + this.visible);
            if (this.visible) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawFrame();
                    }
                }, 200);
                startRefreshDial();
            } else {
                stopRefresh();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "onDestroy:" + visible);
            visible = false;
        }

        @Override
        public void showWallpaper() {
            super.showWallpaper();
            Log.i(TAG, "showWallpaper");
            playAnimation();
        }

        @Override
        public void offScreen() {
            super.offScreen();
            clearScreen();
        }

        @Override
        public void updateBattery(int level) {
            super.updateBattery(level);
            if (staticWallPaperRender != null) {
                staticWallPaperRender.setBatteryLevel(level);
            }
        }

        private void drawFrame() {
            drawFrame(false);
        }

        private void drawFrame(boolean clean) {
            Log.i(TAG, "drawFrame clean" + clean);
            if (!visible && !clean) {
                return;
            }
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                if (clean) {
                    canvas.drawColor(0xFF000000);
                } else if (staticWallPaperRender != null) {
                    staticWallPaperRender.onDraw(canvas);
                }
                if (canvas != null && surfaceHolder.getSurface().isValid()) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } catch (Exception e) {
                if (canvas != null && surfaceHolder.getSurface().isValid()) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                throw e;
            }
        }

        private void clearScreen() {
            drawFrame(true);
        }

        private void playAnimation() {
            Log.i(TAG, "start play animation");
            staticWallPaperRender.setAnimationStep(0);
            drawFrame();
            animationHandler.removeCallbacksAndMessages(null);
            animationHandler.post(createAnimationTask());
        }

        private Runnable createAnimationTask() {
            return new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run");
                    if (animationHandler == null) {
                        Log.i(TAG, "null");
                        return;
                    }
                    animationHandler.removeCallbacksAndMessages(null);
                    if (staticWallPaperRender.getAnimationStep() >= ANIMATION_COUNT) {
                        staticWallPaperRender.setAnimationStep(-1);
                        drawFrame();
                        Log.i(TAG, ">");
                        return;
                    }
                    Log.i(TAG, "run step:" + staticWallPaperRender.getAnimationStep());
                    animationHandler.postDelayed(createAnimationTask(), ANIMATION_INTERVAL);
                    drawFrame();
                    int step = staticWallPaperRender.getAnimationStep();
                    staticWallPaperRender.setAnimationStep(step + 1);
                }
            };
        }

        private Runnable createRefreshTask() {
            return new Runnable() {
                @Override
                public void run() {
                    if (refreshHandler == null) {
                        return;
                    }
                    refreshHandler.removeCallbacksAndMessages(null);
                    drawFrame();
                    refreshHandler.postDelayed(createRefreshTask(), REFRESH_INTERVAL);
                }
            };
        }

        private void startRefreshDial() {
            refreshHandler.removeCallbacksAndMessages(null);
            refreshHandler.postDelayed(createRefreshTask(), REFRESH_INTERVAL);
        }

        private void stopRefresh() {
            if (refreshHandler != null) {
                refreshHandler.removeCallbacksAndMessages(null);
            }
        }
    }
}
