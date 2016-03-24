package in.oliveboard.devishankar.utils;

import android.util.Log;

import in.oliveboard.devishankar.BuildConfig;

/**
 * @author Devishankar
 */
public class Logger {
    public static int d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            if (msg != null) return Log.d(tag, msg);
            else return Log.d(tag, "null");
        else
            return 0;
    }

    public static int i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.i(tag, msg);
        else
            return 0;
    }

    public static int e(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.e(tag, msg);
        else
            return 0;
    }

    public static int w(String tag, String msg) {
        if (BuildConfig.DEBUG)
            return Log.w(tag, msg);
        else
            return 0;
    }

    public static void w(String logTag, String s, Exception e) {
        if (BuildConfig.DEBUG)
            Log.w(logTag, s, e);
    }

    public static void v(String tag, String s) {
        if (BuildConfig.DEBUG)
            Log.w(tag, s);

    }

    public static void e(String tag, String s, Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(tag, s, e);
    }

    public static void getStackTraceString(Throwable th) {
        if (BuildConfig.DEBUG)
            Log.getStackTraceString(th);
    }

    public static void json(String tag, String str) {
        if (BuildConfig.DEBUG)
            if (str.length() > 4000) {
                Logger.d(tag, str.substring(0, 4000));
                json(tag, str.substring(4000));
            } else
                Logger.d(tag, str);
    }

}
