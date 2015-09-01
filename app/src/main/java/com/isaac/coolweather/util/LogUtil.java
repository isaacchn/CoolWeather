package com.isaac.coolweather.util;

import android.util.Log;

/**
 * Created by IsaacCn on 2015/8/30.
 */
public class LogUtil {
    private final static int LEVEL_VERBOSE = 1;
    private final static int LEVEL_DEBUG = 2;
    private final static int LEVEL_INFO = 3;
    private final static int LEVEL_WARN = 4;
    private final static int LEVEL_ERROR = 5;
    private final static int NOTHING = 6;
    private final static int LEVEL = LEVEL_DEBUG;

    public static void v(String tag,String msg){
        if(LEVEL<=LEVEL_VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if(LEVEL<=LEVEL_DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(LEVEL<=LEVEL_INFO){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if(LEVEL<=LEVEL_WARN){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(LEVEL<=LEVEL_ERROR){
            Log.e(tag,msg);
        }
    }
}
