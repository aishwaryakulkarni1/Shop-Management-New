package com.inevitablesol.www.shopmanagement.volley;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Pritam on 02-02-2018.
 */

public class AppController
{
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private Context context;


    public  AppController(Context context){

        mInstance = this;
       this.context = context;
        // Get the request queue
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppController getInstance(Context context) {
        if(mInstance == null){
            mInstance = new AppController(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
