package com.globaldws.alarmscheduler;

import android.os.AsyncTask;

public class BackgroundTask<TParams, TProgress, TResult> extends AsyncTask<TParams, TProgress, TResult> {

    private final BackgroundTaskListener<TParams, TProgress, TResult> listener;

    public BackgroundTask(BackgroundTaskListener<TParams, TProgress, TResult> listener) {
        this.listener = listener;
    }

    @Override
    protected TResult doInBackground(TParams... params) {
        if (listener != null) {
            return listener.doInBackground(params);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(TResult result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onPostExecute(result);
        }
    }

    @Override
    protected void onProgressUpdate(TProgress... values) {
        super.onProgressUpdate(values);
        if (listener != null) {
            listener.onProgressUpdate(values);
        }
    }

    public interface BackgroundTaskListener<TParams, TProgress, TResult> {
        TResult doInBackground(TParams... params);

        void onPreExecute();

        void onPostExecute(TResult result);

        void onProgressUpdate(TProgress... values);

    }
}