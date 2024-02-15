package com.globaldws.alarmscheduler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.globaldws.alarmscheduler.databinding.AlarmLayoutBinding;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private static final String TAG = "AlarmAdapter";
    private List<ScheduleModel> scheduleModelList;
    private Context context;

    public AlarmAdapter(List<ScheduleModel> scheduleModel, Context context) {
        this.scheduleModelList = scheduleModel;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AlarmLayoutBinding binding = AlarmLayoutBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleModel scheduleModel = scheduleModelList.get(position);
        holder.binding.name.setText(scheduleModel.getScheduleName() + " id: " + scheduleModel.getScheduleId());
        holder.binding.dateTime.setText(scheduleModel.getScheduleDate() + ":" + scheduleModel.getScheduleSatrtTime());

        if ((scheduleModel.getRepeatType() - 1) == 0) {
            holder.binding.repeatType.setText("None");
        } else if ((scheduleModel.getRepeatType() - 1) == 1) {
            holder.binding.repeatType.setText("Daily");
        } else if ((scheduleModel.getRepeatType() - 1) == 2) {
            holder.binding.repeatType.setText("Weekly");
        } else if ((scheduleModel.getRepeatType() - 1) == 3) {
            holder.binding.repeatType.setText("Monthly");
        }
    }

    @Override
    public int getItemCount() {
        return scheduleModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AlarmLayoutBinding binding;

        public ViewHolder(AlarmLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
