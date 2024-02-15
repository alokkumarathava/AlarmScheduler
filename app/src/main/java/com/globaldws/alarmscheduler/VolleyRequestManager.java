package com.globaldws.alarmscheduler;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestManager {

    private RequestQueue requestQueue;
    private static VolleyRequestManager instance;
    private static Context appContext;
    private static final int Volley_Socket_Timeout = 0;

    private VolleyRequestManager(Context context) {
        // Initialize the request queue
        appContext = context.getApplicationContext();
        requestQueue = Volley.newRequestQueue(appContext);
    }

    public static synchronized VolleyRequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyRequestManager(context);
        }
        return instance;
    }

    public void makeGetRequest(String url, final Map<String, String> headers, final String contentType, final VolleyResponseListener<String> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> listener.onResponse(response),
                error -> listener.onError(error.toString())) {
            @Override
            public String getBodyContentType() {
                return contentType != null ? contentType : super.getBodyContentType();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(super.getHeaders());
                if (headers != null) {
                    params.putAll(headers);
                }
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Volley_Socket_Timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    public void makePostRequest(String url, final Map<String, String> headers, final String contentType, final VolleyResponseListener<String> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> listener.onResponse(response),
                error -> listener.onError(error.toString())) {
            @Override
            public String getBodyContentType() {
                return contentType != null ? contentType : super.getBodyContentType();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(super.getHeaders());
                if (headers != null) {
                    params.putAll(headers);
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Volley_Socket_Timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    public interface VolleyResponseListener<T> {
        void onResponse(T response);

        void onError(String error);
    }
}
