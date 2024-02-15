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

    private final String url = "https://apiportalv01.azurewebsites.net/api/Get_Schedulesv1?DeviceConnectionID=WIP04";
    private final String token = "1phdCM9b-r7mDtm4VyKXOhgf53A_2gMPTGM9DllION7IOl4Zlf-zICNfq_hMBB3XAKJhSlZjlTk0ZS_ldVAa2IAMCnb-FNTdCihDV_AtnlGYhT2O2qmZEfsF-pZnTSOCUpBmXoVqrT_mEXDzBOtAUjrcWNWYO9s77LHC3In-V9-IElcYqCMsQ_8oBzZetJl7dlJvsUC3O6m3hYLA5ay3VfdFvTc8kc_7Y_v8bo5oQ3yvoEnonkiXotIXaRMxE3fnPt5Os_rv8HqslmMvOjFJn1m8fZJEWB7QD4VgCj4Guk0uOlYMP8SypU2vru6tyxPza1dY6Urm65T0d7VtXkORWgGexny5sT2OtB4t1ZttIXDLozmKTBQFAMshazs6YlCJ66lI-iiDqbGFVdEKf3IVTMv4GgSEvfAetmoN2irS0OANNfqyc6I4aNjyDIgdjUjy04VfylzPe1PxbuMN4bBmrDlHjw61ksJvyaZ3zKwRrsCNd4nFoS9ayNIy0ltSSTyC";
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
                        setAlarms(scheduleList);
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

    private void setAlarms(List<ScheduleModel> scheduleList) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (ScheduleModel scheduleModel : scheduleList) {
            long triggerTimeMillis = calculateTriggerTime(scheduleModel);
            // If the trigger time is in the past, it has been adjusted to the next appropriate time by calculateTriggerTime method.
            long finishDateMillis = calculateFinishTimeMillis(scheduleModel); // Implement this to parse finish date
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);

            if (triggerTimeMillis < finishDateMillis) {
                alarmIntent.putExtra("scheduleName", scheduleModel.getScheduleName());
                // Additional info to handle in AlarmReceiver
                alarmIntent.putExtra("scheduleId", scheduleModel.getScheduleId());
                alarmIntent.putExtra("repeatType", (int) scheduleModel.getRepeatType() - 1);
                alarmIntent.putExtra("finishDateMillis", finishDateMillis); // Pass finish date for handling in receiver

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, scheduleModel.getScheduleId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                }

                Log.d(TAG, "Alarm (re)scheduled for: " + scheduleModel.getScheduleName() + " at " + new Date(triggerTimeMillis).toString());
                // Note: For repeating alarms, AlarmReceiver is responsible for calculating and setting the next occurrence.
            } else {
                PendingIntent cancelIntent = PendingIntent.getBroadcast(this, scheduleModel.getScheduleId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(cancelIntent);
                Log.d(TAG, "Cancelled alarm for schedule " + scheduleModel.getScheduleName() + " as its finish date has passed.");
            }
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
                Log.e(TAG, "calculateTriggerTime: Repeat ID:" + (schedule.getRepeatType() - 1));
                // Adjust the trigger time based on the repeat type
                switch (Math.toIntExact(schedule.getRepeatType() - 1)) {
                    case 0: // One-time
                        if (nextTriggerTime.getTimeInMillis() <= currentTimeMillis) {
                            // This means the one-time event is in the past and should not be scheduled.
                            return 0;
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
