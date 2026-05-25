package com.xtc.wonderland;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import com.xtc.common.ContactsResource;
import com.xtc.common.DialUtil;
import com.xtc.common.StatusHelper;
import com.xtc.common.TimeChangeAlarm;
import com.xtc.common.WallpaperServiceProxy;
import com.xtc.common.WatchDataBase;
import com.xtc.common.WeakHandler;
import com.xtc.common.callback.IDialCallback;
import com.xtc.system.motion.MotionClient;
import com.xtc.system.motion.StepUpdateListener;

/* loaded from: classes2.dex */
public class XTCBaseWallpaperService2 extends WallpaperServiceProxy {
    private static final String ACTION_POWER_KEY = "com.xtc.i3launcher.module.powerkey.event.broadcast";
    private static final int DEFAULT_TARGET = 8000;
    private static final String KEY_TARGET = "target";
    private static final int MESSAGE_SHOW_DATA = 3055;
    public static final String TAG = "XTCBaseWallpaperService2";
    protected static boolean isFirstBoot = true;
    private BroadcastReceiver broadcastReceiver;
    private WeakHandler handler;
    private HandlerThread handlerThread;
    protected boolean isClass;
    protected boolean isHighTemperature;
    protected boolean isPowerSave;

    public XTCBaseWallpaperService2(Context context, String path) {
        super(context, path);
    }

    public XTCBaseWallpaperService2(Context context) {
        super(context);
        Log.d("XTCBaseWallpaperService2", "zjj, XTCBaseWallpaperService");
    }

    @Override // com.xtc.common.WallpaperServiceProxy, android.service.wallpaper.WallpaperService
    public WallpaperServiceProxy.ActiveEngine onCreateEngine() {
        return null;
    }

    public class BaseEngine extends WallpaperServiceProxy.ActiveEngine implements IDialCallback, Handler.Callback {
        private StatusHelper.BatteryChangedCallback batteryChangedCallback;
        protected boolean isNeedBattery;
        protected boolean isNeedStep;
        protected boolean isNeedWeather;
        private boolean isVisible;
        private StepUpdateListener mStepUpdateListener;
        private MotionClient motionClient;
        private boolean screenOnShowWallpaperFlag;
        private TimeChangeAlarm.ITimeChangeCallback timeChangeCallback;
        private boolean unlockFlag;

        public BaseEngine() {
            super();
            this.isVisible = true;
            this.timeChangeCallback = new TimeChangeAlarm.ITimeChangeCallback() { // from class: com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine.1
                @Override // com.xtc.common.TimeChangeAlarm.ITimeChangeCallback
                public void onTimeChange() {
                    Log.d("XTCBaseWallpaperService2", "onTimeChange");
                    BaseEngine.this.updateTime();
                }
            };
            this.batteryChangedCallback = new StatusHelper.BatteryChangedCallback() { // from class: com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine.2
                @Override // com.xtc.common.StatusHelper.BatteryChangedCallback
                public void onBatteryChanged(int batteryLevel, boolean isCharging) {
                    Log.i("XTCBaseWallpaperService2", "onBatteryChanged = " + batteryLevel);
                    BaseEngine.this.updateBattery(batteryLevel);
                    BaseEngine.this.updateBatteryAndCharging(batteryLevel, isCharging);
                }
            };
            this.mStepUpdateListener = new StepUpdateListener() { // from class: com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine.3
                @Override // com.xtc.system.motion.StepUpdateListener
                public void onUpdate() {
                    if (BaseEngine.this.motionClient != null) {
                        int step = BaseEngine.this.motionClient.getTodayStep();
                        int targetStep = BaseEngine.this.motionClient.getTargetStep();
                        BaseEngine.this.updateStep(step, targetStep);
                    }
                }
            };
            this.screenOnShowWallpaperFlag = false;
            this.unlockFlag = false;
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            XTCBaseWallpaperService2.this.handlerThread = new HandlerThread("XTCBaseWallpaperService2");
            XTCBaseWallpaperService2.this.handlerThread.start();
            XTCBaseWallpaperService2 xTCBaseWallpaperService2 = XTCBaseWallpaperService2.this;
            xTCBaseWallpaperService2.handler = new WeakHandler(xTCBaseWallpaperService2.handlerThread.getLooper(), this);
            initData();
        }

        private void initData() {
            this.isNeedStep = false;
            this.isNeedBattery = false;
            this.isNeedWeather = false;
            XTCBaseWallpaperService2 xTCBaseWallpaperService2 = XTCBaseWallpaperService2.this;
            xTCBaseWallpaperService2.isClass = WatchDataBase.getWatchIsClassMode(xTCBaseWallpaperService2.getApplicationContext());
            XTCBaseWallpaperService2 xTCBaseWallpaperService22 = XTCBaseWallpaperService2.this;
            xTCBaseWallpaperService22.isHighTemperature = WatchDataBase.getWatchIsHighTemperature(xTCBaseWallpaperService22.getApplicationContext());
            XTCBaseWallpaperService2 xTCBaseWallpaperService23 = XTCBaseWallpaperService2.this;
            xTCBaseWallpaperService23.isPowerSave = WatchDataBase.getWatchIsPowerSave(xTCBaseWallpaperService23.getApplicationContext());
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initEvent();
        }

        private void initEvent() {
            initReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(XTCBaseWallpaperService2.ACTION_POWER_KEY);
            intentFilter.addAction(ContactsResource.ACTION_TIME_MODE);
            intentFilter.addAction(ContactsResource.ACTION_UNLOCK);
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction(ContactsResource.ACTION_UPDATE_TARGET);
            XTCBaseWallpaperService2 xTCBaseWallpaperService2 = XTCBaseWallpaperService2.this;
            xTCBaseWallpaperService2.registerReceiver(xTCBaseWallpaperService2.broadcastReceiver, intentFilter);
            initListener();
        }

        protected void initListener() {
            TimeChangeAlarm.getInstance(XTCBaseWallpaperService2.this.getApplicationContext()).setTimeChangeCallback(this.timeChangeCallback);
            if (this.isNeedStep) {
                if (this.motionClient == null) {
                    this.motionClient = new MotionClient(XTCBaseWallpaperService2.this.getApplicationContext());
                }
                this.motionClient.registerUpdateListener(this.mStepUpdateListener);
            }
            if (this.isNeedBattery) {
                StatusHelper.getInstance(XTCBaseWallpaperService2.this.getApplicationContext()).setBatteryChangedCallback(this.batteryChangedCallback);
            }
        }

        private void resetListener() {
            MotionClient motionClient;
            if (this.timeChangeCallback != null) {
                TimeChangeAlarm.getInstance(XTCBaseWallpaperService2.this.getApplicationContext()).removeTimeChangeCallback(this.timeChangeCallback);
            }
            if (this.isNeedBattery) {
                Log.i("XTCBaseWallpaperService2", "remove battery callback success");
                StatusHelper.getInstance(XTCBaseWallpaperService2.this.getApplicationContext()).removeBatteryChangedCallback(this.batteryChangedCallback);
            }
            if (this.isNeedStep && this.motionClient != null) {
                Log.i("XTCBaseWallpaperService2", "remove step callback success");
                StepUpdateListener stepUpdateListener = this.mStepUpdateListener;
                if (stepUpdateListener != null && (motionClient = this.motionClient) != null) {
                    motionClient.unregisterUpdateListener(stepUpdateListener);
                }
            }
        }

        public void showWallpaper() {
        }

        public void offScreen() {
        }

        public void updateBattery(int batteryLevel) {
        }

        public void updateBatteryAndCharging(int batteryLevel, boolean isCharging) {
        }

        @Override // com.xtc.common.callback.IDialCallback
        public void updateStep(int step, int targetStep) {
        }

        @Override // com.xtc.common.callback.IDialCallback
        public void updateTime() {
        }

        @Override // com.xtc.common.callback.IDialCallback
        public void onScreen() {
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            super.onDestroy();
            if (XTCBaseWallpaperService2.this.broadcastReceiver != null) {
                XTCBaseWallpaperService2 xTCBaseWallpaperService2 = XTCBaseWallpaperService2.this;
                xTCBaseWallpaperService2.unregisterReceiver(xTCBaseWallpaperService2.broadcastReceiver);
                XTCBaseWallpaperService2.this.broadcastReceiver = null;
            }
            if (XTCBaseWallpaperService2.this.handler != null) {
                XTCBaseWallpaperService2.this.handler.removeCallbacksAndMessages(null);
            }
            if (XTCBaseWallpaperService2.this.handlerThread != null) {
                XTCBaseWallpaperService2.this.handlerThread.quit();
                XTCBaseWallpaperService2.this.handlerThread = null;
            }
            resetListener();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onVisibilityChanged(boolean visible) {
            Log.d("XTCBaseWallpaperService2", "onVisibilityChanged = " + visible);
            if (visible) {
                Log.i("XTCBaseWallpaperService2", "add callback");
                initListener();
            } else {
                Log.i("XTCBaseWallpaperService2", "remove callback");
                resetListener();
            }
        }

        private void initReceiver() {
            if (XTCBaseWallpaperService2.this.broadcastReceiver != null) {
                return;
            }
            XTCBaseWallpaperService2.this.broadcastReceiver = new BroadcastReceiver() { // from class: com.xtc.wonderland.XTCBaseWallpaperService2.BaseEngine.4
                /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.i("XTCBaseWallpaperService2", "action =" + action);
                    if (action != null) {
                        char c = 65535;
                        switch (action.hashCode()) {
                            case -2128145023:
                                if (action.equals("android.intent.action.SCREEN_OFF")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case -1784188287:
                                if (action.equals(ContactsResource.ACTION_TIME_MODE)) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -1454123155:
                                if (action.equals("android.intent.action.SCREEN_ON")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case -60342873:
                                if (action.equals(ContactsResource.ACTION_UPDATE_TARGET)) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 316798476:
                                if (action.equals(ContactsResource.ACTION_UNLOCK)) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        if (c == 0) {
                            boolean is24Hour = intent.getBooleanExtra("is24Hour", true);
                            Log.i("XTCBaseWallpaperService2", "onReceive =" + is24Hour);
                            BaseEngine.this.updateTime();
                            return;
                        }
                        if (c == 1) {
                            String reason = intent.getStringExtra("reason");
                            Log.i("XTCBaseWallpaperService2", "reason:" + reason);
                            BaseEngine.this.onScreen();
                            BaseEngine.this.isVisible = true;
                            boolean hasShow = intent.getBooleanExtra(ContactsResource.EXTRA_UNLOCK_SHOW, false);
                            Log.i("XTCBaseWallpaperService2", "lock hasShow = " + hasShow);
                            if (!hasShow && BaseEngine.this.isVisible) {
                                BaseEngine.this.showData();
                                if (!"android.policy:POWER".equalsIgnoreCase(reason)) {
                                    BaseEngine.this.screenOnShowWallpaperFlag = true;
                                    BaseEngine.this.showWallpaper();
                                    return;
                                }
                                return;
                            }
                            if (!"android.policy:POWER".equalsIgnoreCase(reason)) {
                                BaseEngine.this.screenOnShowWallpaperFlag = true;
                                return;
                            }
                            return;
                        }
                        if (c != 2) {
                            if (c == 3) {
                                BaseEngine.this.isVisible = false;
                                BaseEngine.this.screenOnShowWallpaperFlag = false;
                                BaseEngine.this.unlockFlag = false;
                                BaseEngine.this.offScreen();
                                return;
                            }
                            if (c == 4) {
                                if (BaseEngine.this.motionClient != null) {
                                    int step = BaseEngine.this.motionClient.getTodayStep();
                                    int targetStep = intent.getIntExtra(XTCBaseWallpaperService2.KEY_TARGET, XTCBaseWallpaperService2.DEFAULT_TARGET);
                                    Log.d("XTCBaseWallpaperService2", "step = " + step + ", targetStep = " + targetStep);
                                    BaseEngine.this.updateStep(step, targetStep);
                                    return;
                                }
                                Log.d("XTCBaseWallpaperService2", "motionClient == null");
                                return;
                            }
                            Log.d("XTCBaseWallpaperService2", "action is not register");
                            return;
                        }
                        boolean hasUnlock = WatchDataBase.getWatchIsScreenLock(XTCBaseWallpaperService2.this.getApplicationContext());
                        Log.i("XTCBaseWallpaperService2", "lock hasUnlock = " + hasUnlock + ", isVisible:" + BaseEngine.this.isVisible);
                        if (hasUnlock || !BaseEngine.this.isVisible) {
                            if (BaseEngine.this.isVisible && WatchDataBase.getWatchIsCharging(XTCBaseWallpaperService2.this.getApplicationContext()) && 2 != DialUtil.simpleGetInt(XTCBaseWallpaperService2.this.getApplicationContext(), ContactsResource.KeyguardViewState.URI)) {
                                Log.i("XTCBaseWallpaperService2", "isCharging but not charge show");
                                BaseEngine.this.unlockFlag = true;
                                BaseEngine.this.showData();
                                BaseEngine.this.tryShowWallpaper();
                                return;
                            }
                            return;
                        }
                        BaseEngine.this.unlockFlag = true;
                        BaseEngine.this.showData();
                        BaseEngine.this.tryShowWallpaper();
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void tryShowWallpaper() {
            Log.i("XTCBaseWallpaperService2", "screenOnShowWallpaperFlag:" + this.screenOnShowWallpaperFlag + " unlockFlag:" + this.unlockFlag);
            if (!this.screenOnShowWallpaperFlag || !this.unlockFlag) {
                return;
            }
            showWallpaper();
            this.screenOnShowWallpaperFlag = false;
            this.unlockFlag = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showData() {
            if (XTCBaseWallpaperService2.this.handler == null) {
                Log.d("XTCBaseWallpaperService2", "handler == null");
                XTCBaseWallpaperService2 xTCBaseWallpaperService2 = XTCBaseWallpaperService2.this;
                xTCBaseWallpaperService2.handler = new WeakHandler(xTCBaseWallpaperService2.handlerThread.getLooper(), this);
            }
            XTCBaseWallpaperService2.this.handler.removeCallbacksAndMessages(null);
            Message message = Message.obtain();
            message.what = XTCBaseWallpaperService2.MESSAGE_SHOW_DATA;
            XTCBaseWallpaperService2.this.handler.sendMessage(message);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            if (msg.what == XTCBaseWallpaperService2.MESSAGE_SHOW_DATA) {
                if (this.isNeedBattery) {
                    updateBattery(WatchDataBase.getWatchBatteryLevel(XTCBaseWallpaperService2.this.getApplicationContext()));
                }
                if (this.isNeedStep) {
                    if (this.motionClient == null) {
                        this.motionClient = new MotionClient(XTCBaseWallpaperService2.this.getApplicationContext());
                    }
                    int step = this.motionClient.getTodayStep();
                    int targetStep = this.motionClient.getTargetStep();
                    updateStep(step, targetStep);
                    return true;
                }
                return true;
            }
            return true;
        }
    }
}