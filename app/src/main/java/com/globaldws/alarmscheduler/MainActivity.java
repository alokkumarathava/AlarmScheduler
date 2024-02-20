package com.globaldws.alarmscheduler;

import static com.globaldws.alarmscheduler.Extension.showToast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.globaldws.alarmscheduler.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://apiportalv01.azurewebsites.net/api/Get_Schedulesv1?DeviceConnectionID=WIP01";
    private final String token = "KH5ePSRIMnhxrJk9FLPYBvDRsCbNaoQZUjVxyWQPcW3gTn8zsrplRq-7vJs9HjGnkdHKu8k5RKa7lxWFd1T0Bl14a5o7Zzi3A5G9g51b_3TXh94D5xj0tXHKDCreytBfqNrC2S_rYJwSU8f2czPx0dRsbRy2XvSwg3DLUK6h3GLlxokt-7agttOUG5sjvBexz_nbM1GPDlUMz6e6oPcqw5xrzv8Zi3_M2YDUQY6YrZl3HbUVBo3R-9TX4LmFqROnN3YpEV4zp0zJGm2uFVrbqIxwYrISQBKvHnIwcIHwdJIzy_5y2KPT8pJ347ACAqzegRDfGxsy8L7unoFwKHDN7inPjldIRaKW31H_o-Db70_3v8e2h6iP11KKG3osM7QwDBmkaq1Qva7BmrB4PtNKQ47OQmyfvqgcgAs-QOG70g9lvQ8CR5y3d1v53VEQm_Re_oxkKMyjfgYbx_mipHJNjrO8fmlHXGDQ7jYP23vMz8ON6PpfHNPsacIxfy-hkX9k";
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private ArrayList<ScheduleModel> scheduleModels = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        alarmAdapter = new AlarmAdapter(scheduleModels, this);
        binding.recyclerView.setAdapter(alarmAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.e(TAG, "URL: " + url);
        Log.e(TAG, "Token: " + token);

        fetchAndSetAlarms();
    }

    private void fetchAndSetAlarms() {
        // Fetch schedule data
        VolleyRequestManager.getInstance(getApplicationContext()).makeGetRequest(url, getHeaders(), getContentType(), new VolleyRequestManager.VolleyResponseListener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Gson gson = new Gson();
                    Type scheduleListType = new TypeToken<List<ScheduleModel>>() {
                    }.getType();
                    List<ScheduleModel> scheduleList = gson.fromJson(response, scheduleListType);
                    if (scheduleList != null) {
                        setAlarms(getApplicationContext(), scheduleList);
                        scheduleModels.clear();
                        scheduleModels.addAll(scheduleList);
                        alarmAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "onResponse: No Schedule Found");
                        showToast(getApplicationContext(), "No Schedule Found");
                    }
                } else {
                    Log.e(TAG, "onResponse: Is Null");
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
            }
        });
    }

    public void setAlarms(Context context, List<ScheduleModel> scheduleList) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (ScheduleModel scheduleModel : scheduleList) {
            Log.e(TAG, "setAlarms: Schedule ID: " + scheduleModel.getScheduleId());

            long triggerTimeMillis = calculateTriggerTime(scheduleModel);
            // Validate trigger time
            if (triggerTimeMillis <= 0) {
                Log.d(TAG, "Invalid trigger time for Schedule ID: " + scheduleModel.getScheduleId());
                continue; // Skip this iteration
            }

            long finishDateMillis = calculateFinishTimeMillis(scheduleModel);
            // Validate finish date
            if (finishDateMillis == -1) {
                Log.e(TAG, "Invalid finish date for Schedule ID: " + scheduleModel.getScheduleId());
                continue; // Skip this iteration
            }

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra("scheduleName", scheduleModel.getScheduleName());
            alarmIntent.putExtra("scheduleId", scheduleModel.getScheduleId());
            alarmIntent.putExtra("repeatType", scheduleModel.getRepeatType() - 1);
            alarmIntent.putExtra("finishDateMillis", finishDateMillis);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleModel.getScheduleId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the alarm based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            }

            Log.d(TAG, "Alarm for Schedule ID: " + scheduleModel.getScheduleId() + " set for: " + new Date(triggerTimeMillis).toString());
        }
    }

    private long calculateFinishTimeMillis(ScheduleModel scheduleModel) {
        // Assuming ScheduleModel has a method getScheduleFinishDate() that returns a String
        String finishDateString = scheduleModel.getScheduleFinishDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date finishDate = dateFormat.parse(finishDateString);
            if (finishDate != null) {
                return finishDate.getTime();
            } else {
                Log.e(TAG, "Failed to parse finish date: " + finishDateString);
                return -1; // Indicate error
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing finish date: " + finishDateString, e);
            return -1; // Indicate error
        }
    }

    private long calculateTriggerTime(ScheduleModel schedule) {
        Log.d(TAG, "calculateTriggerTime: Schedule date: " + schedule.getScheduleDate());
        Log.d(TAG, "calculateTriggerTime: Schedule time: " + schedule.getScheduleSatrtTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        long currentTimeMillis = System.currentTimeMillis();
        Log.e(TAG, "calculateTriggerTime: " + new Date(currentTimeMillis).toString());
        try {
            Date scheduledDate = dateFormat.parse(schedule.getScheduleDate() + " " + schedule.getScheduleSatrtTime());
            if (scheduledDate != null) {
                Calendar nextTriggerTime = Calendar.getInstance();
                nextTriggerTime.setTime(scheduledDate);
                long scheduledTimeMillis = nextTriggerTime.getTimeInMillis();
                Log.e(TAG, "calculateTriggerTime: Repeat ID:" + (schedule.getRepeatType() - 1));
                // Adjust the trigger time based on the repeat type
                switch (Math.toIntExact(schedule.getRepeatType() - 1)) {
                    case 0: // One-time
                        if (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            // This means the one-time event is in the past and should not be scheduled.
                            return scheduledTimeMillis; // Not to schedule past alarms.
                        }
                        break;
                    case 1: // Daily
                        while (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            nextTriggerTime.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        break;
                    case 2: // Weekly
                        while (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            nextTriggerTime.add(Calendar.WEEK_OF_YEAR, 1);
                        }
                        break;
                    case 3: // Monthly
                        while (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            nextTriggerTime.add(Calendar.MONTH, 1);
                        }
                        break;
                    default:
                        Log.e(TAG, "calculateTriggerTime: Unknown repeat type for " + schedule.getScheduleName());
                        // Handle the case for no repeat or unknown repeat type
                        if (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            // This means the one-time event is in the past and should not be scheduled.
                            return 0;
                        }
                        break;
                }
                Log.d(TAG, "calculateTriggerTime: Adjusted trigger time to future: " + nextTriggerTime.getTime());
                return nextTriggerTime.getTimeInMillis();
            } else {
                Log.e(TAG, "calculateTriggerTime: Failed to parse date and time string.");
            }
        } catch (ParseException e) {
            Log.e(TAG, "calculateTriggerTime: Error parsing date and time.", e);
        }
        return 0;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    private String getContentType() {
        return "application/json";
    }

}
