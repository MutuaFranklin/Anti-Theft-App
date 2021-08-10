package com.track.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.track.function.AlarmFunction;
import com.track.function.PreferenceFunction;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            //set alarm background service
            if (!TextUtils.isEmpty(new PreferenceFunction(context).getUserId())) {
                new AlarmFunction(context).startRepeatingAlarm();
            }
        }

    }

}