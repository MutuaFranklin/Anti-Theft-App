package com.track.function;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.track.receiver.BackgroundReceiver;

public class AlarmFunction {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Context nContext;

    public AlarmFunction(Context context) {
        nContext = context;
    }

    public void startRepeatingAlarm() {
        alarmManager = (AlarmManager) nContext.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(nContext, BackgroundReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(nContext, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000 * 6, pendingIntent);
    }

    public void stopRepeatingAlarm() {
        alarmManager = (AlarmManager) nContext.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(nContext, BackgroundReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(nContext, 0, alarmIntent, 0);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
