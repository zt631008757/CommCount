package com.android.baselibrary.tool;

import android.text.TextUtils;

/**
 * 通用日志打印类 （安全控制）
 * 只有A/B版打印   其他版本不打印
 */
public final class Log {
    private static final String TAG = "test";
    private static boolean logEnabled = true;

    public static void w(String msg) {
        if (logEnabled) {
            android.util.Log.w(TAG, getLocation() + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (logEnabled) {
            android.util.Log.w(tag, getLocation() + msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (logEnabled) {
            android.util.Log.w(tag, getLocation() + msg, e);
        }
    }

    public static void w() {
        if (logEnabled) {
            android.util.Log.w(TAG, getLocation());
        }
    }

    public static void d() {
        if (logEnabled) {
            android.util.Log.v(TAG, getLocation());
        }
    }

    public static void d(String msg) {
        if (logEnabled) {
            android.util.Log.d(TAG, getLocation() + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (logEnabled) {
            android.util.Log.d(tag, getLocation() + msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (logEnabled) {
            android.util.Log.d(tag, getLocation() + msg, e);
        }
    }

    public static void i(String msg) {
        if (logEnabled) {
            i(TAG, getLocation() + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (logEnabled) {
            if (msg.length() > 4000) {
                for (int i = 0; i < msg.length(); i += 4000) {
                    if (i + 4000 < msg.length())
                        android.util.Log.i(tag, getLocation() + msg.substring(i, i + 4000));
                    else
                        android.util.Log.i(tag, getLocation() + msg.substring(i, msg.length()));
                }
            } else {
                android.util.Log.i(tag, getLocation() + msg);
            }
        }
    }

    public static void e(String msg) {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation() + msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation() + msg, e);
        }
    }

    public static void e(String TAG, String msg) {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation() + msg);
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation() + msg, e);
        }
    }

    public static void e(Throwable e) {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation(), e);
        }
    }

    public static void e() {
        if (logEnabled) {
            android.util.Log.e(TAG, getLocation());
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (logEnabled) {
            android.util.Log.v(TAG, getLocation(), e);
        }
    }

    public static void v(String TAG, String msg) {
        if (logEnabled) {
            android.util.Log.v(TAG, getLocation());
        }
    }

    public static void v(String msg) {
        if (logEnabled) {
            android.util.Log.v(TAG, msg);
        }
    }

    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread()
                .getStackTrace();
        boolean found = false;

        for (StackTraceElement trace : traces) {
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":"
                                + trace.getMethodName() + ":"
                                + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }

        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

}
