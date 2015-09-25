package com.isaac.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.isaac.coolweather.service.AutoUpdateService;
import com.isaac.coolweather.util.LogUtil;

import java.util.Date;

/**
 * Created by IsaacCn on 2015/9/25.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("AlarmReceiver", "onReceive at " + new Date().toString()+", "+context.toString());
        Intent i = new Intent(context, AutoUpdateService.class);
        i.putExtra("calledByAlarmFlag",true);
        context.startService(i);
    }
}
