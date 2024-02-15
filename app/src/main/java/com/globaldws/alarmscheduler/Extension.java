package com.globaldws.alarmscheduler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Toast;

public class Extension {

    private static ProgressDialog progressDialog;


    /**
     * Shows a toast message.
     *
     * @param context The context in which the toast should be shown.
     * @param msg     The message to be displayed in the toast.
     */
    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if an EditText field is empty and sets an error if it is.
     *
     * @param field The EditText field to be checked.
     * @return true if the field is empty or null, false otherwise.
     */
    public static boolean isFieldEmpty(EditText field) {
        if (field == null) {
            return true;
        }
        String text = field.getText().toString();
        if (text.trim().isEmpty()) {
            setError(field);  // Sets an error message on the EditText field
            return true;
        }
        return false;
    }

    /**
     * Sets an error message on an EditText field.
     *
     * @param field The EditText field on which to set the error message.
     */
    private static void setError(EditText field) {
        if (field != null) {
            field.setError("All Fields are required");
        }
    }

    /**
     * Retrieves trimmed text from an EditText field.
     *
     * @param editText The EditText field from which to get the text.
     * @return The trimmed text from the EditText field, or an empty string if the field is null.
     */
    public static String getTextField(EditText editText) {
        if (editText != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    /**
     * Starts an activity based on the provided class.
     *
     * @param context   The Context from which the activity is started. This is typically an Activity or Application context.
     *                  It is used to create the intent and start the activity.
     * @param className The class of the activity to be started. This is the target activity that the method will initiate.
     *                  It is a generic type parameter, allowing this method to be used with any activity class.
     * @param <T>       Generic type parameter that extends from Object, representing the activity class.
     */
    public static <T> void activityStart(Context context, Class<T> className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    /**
     * Starts an activity based on the provided class.
     *
     * @param context   The Context from which the activity is started. This is typically an Activity or Application context.
     *                  It is used to create the intent and start the activity.
     * @param className The class of the activity to be started. This is the target activity that the method will initiate.
     *                  It is a generic type parameter, allowing this method to be used with any activity class.
     * @param <T>       Generic type parameter that extends from Object, representing the activity class.
     */
    public static void startActivityAfterDelay(final Context context, final Class<?> newActivity, final Activity currentActivity, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityStart(context, newActivity);
            }
        }, delayMillis);
    }

    /**
     * Utility method for executing a background task using BackgroundTask class.
     *
     * @param listener    The listener containing the background task logic.
     * @param params      The parameters to be passed to the background task.
     * @param <TParams>   Type of parameters.
     * @param <TProgress> Type of progress.
     * @param <TResult>   Type of result.
     */

    public static <TParams, TProgress, TResult> void executeBackgroundTask(BackgroundTask.BackgroundTaskListener<TParams, TProgress, TResult> listener, TParams... params) {
        BackgroundTask<TParams, TProgress, TResult> backgroundTask = new BackgroundTask<>(listener);
        backgroundTask.execute(params);
    }


    /**
     * Executes the specified code block within a try-catch block, catching any exceptions
     * that might be thrown during its execution.
     *
     * @param runnable The code block to be executed.
     */
    public static void tryCatch(ExceptionRunnable runnable) {
        try {
            // Attempt to execute the provided code block
            runnable.run();
        } catch (Exception e) {
            // If any exception is caught during execution, print the stack trace
            e.printStackTrace();
            // Alternatively, you can log or handle the exception as needed in your specific application
        }
    }

    /**
     * Functional interface representing a code block that can be executed within the try-catch block.
     */
    public interface ExceptionRunnable {
        /**
         * Runs the code block.
         *
         * @throws Exception If an exception occurs during execution.
         */
        void run() throws Exception;
    }

    /**
     * Displays a progress dialog with the specified message.
     *
     * @param context The context in which the progress dialog will be displayed.
     * @param message The message to be displayed on the progress dialog.
     * @return The created ProgressDialog instance.
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * Dismisses the previously displayed progress dialog if it is currently showing.
     */
    public static void dismissProgressDialog() {
        // Check if progressDialog is not null and is currently showing
        if (progressDialog != null && progressDialog.isShowing()) {
            // Dismiss the progress dialog
            progressDialog.dismiss();
        }
    }

    public static void showToastOnMainThread(Context context, String message, int duration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), message, duration).show();
            }
        });
    }

    // Overloaded method with default duration
    public static void showToastOnMainThread(Context context, String message) {
        showToastOnMainThread(context, message, Toast.LENGTH_SHORT);
    }

    /*


ToastUtils.showToastOnMainThread(context, "Your message");



*/ 

}