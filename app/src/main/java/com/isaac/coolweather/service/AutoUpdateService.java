package com.isaac.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.isaac.coolweather.receiver.AlarmReceiver;
import com.isaac.coolweather.util.LogUtil;

import java.util.Date;

/**
 * Created by IsaacCn on 2015/9/25.
 */
public class AutoUpdateService extends Service {
    private LocalBroadcastManager localBroadcastManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("AutoUpdateService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("AutoUpdateService", "onStartCommand at " + new Date().toString());
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int minutes = intent.getIntExtra("AUTO_UPDATE_INTERVAL", 2);
        LogUtil.d("AutoUpdateService", "自动更新每 " + minutes + " 分钟.");
        final long INTERVAL = 60000 * minutes;
        long triggerAtTime = SystemClock.elapsedRealtime() + INTERVAL;
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        //LogUtil.d("AutoUpdateService",Boolean.toString(intent.getBooleanExtra("calledByAlarmFlag",false)));
        if (intent.getBooleanExtra("calledByAlarmFlag", false)) {
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent sendIntent = new Intent("com.isaac.coolweather.UPDATE_UI_BROADCAST");
            localBroadcastManager.sendBroadcast(sendIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d("AutoUpdateService", "onDestroy");
    }
}
