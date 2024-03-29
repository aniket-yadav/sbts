package com.app.sbts.classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class SingletonClass {

    private static SingletonClass mInstance ;
    private RequestQueue requestQueue;
    private static Context mContext;

    private SingletonClass(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized SingletonClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);

    }

}