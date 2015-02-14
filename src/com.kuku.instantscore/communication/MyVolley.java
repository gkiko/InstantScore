package com.kuku.instantscore.communication;

import android.content.Context;

import com.kuku.instantscore.volley.Request;
import com.kuku.instantscore.volley.RequestQueue;
import com.kuku.instantscore.volley.toolbox.Volley;

/**
 * Created by gkiko on 8/1/14.
 */
public class MyVolley {
    private static MyVolley mVolley;
    private static Context mCtx;

    private RequestQueue mRequestQueue;

    public static MyVolley getInstance(Context context){
        if(mVolley == null){
            mVolley = new MyVolley(context);
        }
        return mVolley;
    }

    private MyVolley(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // Instantiate the cache
            com.kuku.instantscore.volley.Cache cache = new com.kuku.instantscore.volley.toolbox.DiskBasedCache(mCtx.getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            com.kuku.instantscore.volley.Network network = new com.kuku.instantscore.volley.toolbox.BasicNetwork(new com.kuku.instantscore.volley.toolbox.HurlStack());
//            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) { mRequestQueue.add(req);
    }
}
