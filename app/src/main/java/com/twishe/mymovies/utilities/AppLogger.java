package com.twishe.mymovies.utilities;

import android.util.Log;

public class AppLogger {
    public static final String LOG_LEVEL_WARNING ="w";
    public static final String LOG_LEVEL_DEBUG ="d";
    public static final String LOG_LEVEL_INFO ="i";
    public static final String LOG_LEVEL_ERROR ="e";
    public static void printLog(String TAG,String message,String type){
        if(AppData.SHOW_LOGS) {

            if (type.equals(LOG_LEVEL_WARNING)) {
                Log.w(TAG, message);
            } else if (type.equals(LOG_LEVEL_DEBUG)) {
                Log.d(TAG, message);
            } else if (type.equals(LOG_LEVEL_INFO)) {
                Log.i(TAG, message);
            }
            if (type.equals(LOG_LEVEL_ERROR)) {
                Log.e(TAG, message);
            }


        }
    }
}
