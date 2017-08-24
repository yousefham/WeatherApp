package com.app.weatherapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Eyad on 7/27/2017.
 */

public class MyVollySingleton {
    private static RequestQueue requestQueue ;
    private static Context context;
    private static MyVollySingleton mInstance;
    private static ImageLoader loader;

    public MyVollySingleton(Context contexttt){
         context = contexttt;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null)
            return requestQueue=Volley.newRequestQueue(context.getApplicationContext());
        return  requestQueue;
    }

    public  static  synchronized MyVollySingleton getInstance(Context context){
        if (mInstance ==null)mInstance=new MyVollySingleton(context);
         return  mInstance;
     }

    public  static <T> void AddToRequestQue(Request<T> tRequest){
        requestQueue.add(tRequest);
    }

    public  static <T> void AddToRequestQue(Request<T> tRequest  , String tag){
        tRequest.setTag(tag);
        requestQueue.add(tRequest);
    }

    public static void cancelRequest(String tag){
        requestQueue.cancelAll(tag);
    }


}
