package com.globaldws.alarmscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private static PowerManager.WakeLock wakeLock = null;
    private MediaPlayer mediaPlayer = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver:WakeLock");
        wakeLock.acquire(10 * 60 * 1000L);

        // Extract details from intent
        int scheduleId = intent.getIntExtra("scheduleId", -1);
        int repeatType = intent.getIntExtra("repeatType", 0); // 0: No Repeat, 1: Daily, 2: Weekly, 3: Monthly
        long finishDateMillis = intent.getLongExtra("finishDateMillis", 0);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        }
        mediaPlayer.start();

        if (repeatType != 0 && System.currentTimeMillis() < finishDateMillis) {
            // Calculate the next trigger time based on the repeat type
            long nextTriggerTimeMillis = calculateNextTriggerTime(repeatType);

            // Reschedule the next alarm if it is before the finish date
            if (nextTriggerTimeMillis < finishDateMillis) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                intent.putExtra("triggerTimeMillis", nextTriggerTimeMillis); // Update intent with the new trigger time
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerTimeMillis, pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTriggerTimeMillis, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, nextTriggerTimeMillis, pendingIntent);
                }
            }
        }

        new Handler().postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }, 5000);
    }

    private long calculateNextTriggerTime(int repeatType) {
        Calendar nextTriggerTime = Calendar.getInstance();

        switch (repeatType) {
            case 0: // No Repeat
                // Return the current time to effectively disable the repeat
                return System.currentTimeMillis();
            case 1: // Daily
                nextTriggerTime.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case 2: // Weekly
                nextTriggerTime.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case 3: // Monthly
                nextTriggerTime.add(Calendar.MONTH, 1);
                break;
            default:
                Log.e("AlarmReceiver", "Unknown repeat type: " + repeatType);
                // For simplicity, we'll just return the current time which effectively disables the repeat
                return System.currentTimeMillis();
        }

        // Return the next trigger time in milliseconds
        return nextTriggerTime.getTimeInMillis();
    }
}
