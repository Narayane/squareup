package com.sebastien.balard.android.squareup.misc;

import android.util.Log;

import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;

/**
 * Created by sbalard on 27/07/2015.
 */
public class SQLog {

    public static enum SQLogLevel {

        // default = info
        NONE(0, "none"), WTF(1, "wtf"), ERROR(2, "error"), WARN(3, "warn"), INFO(4, "info"), DEFAULT(4, "default"),
        DEBUG(5, "debug"), VERBOSE(6, "verbose"), ALL(7, "all");

        public static SQLogLevel fromName(String pName) {
            SQLogLevel result = null;
            for (SQLogLevel vLogLevel : values()) {
                if (vLogLevel.mLevel.equals(pName.toLowerCase())) {
                    result = vLogLevel;
                    break;
                }
            }
            return result;
        }

        public final int mValue;

        public final String mLevel;

        SQLogLevel(int pValue, String pLevel) {
            mValue = pValue;
            mLevel = pLevel;
        }
    }

    private static final String TAG = "### Square up - ";

    private static int mValue;

    public static void d(String pMessage) {
        if (mValue >= SQLogLevel.DEBUG.mValue) {
            Log.d(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void d(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.DEBUG.mValue) {
            Log.d(TAG + getCallerName(), pMessage, tr);
        }
    }

    public static void e(String pMessage) {
        if (mValue >= SQLogLevel.ERROR.mValue) {
            Log.e(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void e(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.ERROR.mValue) {
            Log.e(TAG + getCallerName(), pMessage, tr);
        }
    }

    /*public static void e(String pMessage, RetrofitError pError) {
        if (mValue >= SQLogLevel.ERROR.mValue) {
            if (pError.getCause() != null) {
                Log.e(TAG + getCallerName(), pMessage + ": " + pError.getKind() + ", " + pError.getCause().getClass()
                        .getName() + ", " + pError.getMessage());
            } else {
                Log.e(TAG + getCallerName(), pMessage + ": " + pError.getKind() + ", " + pError.getMessage());
            }
        }
    }*/

    public static void i(String pMessage) {
        if (mValue >= SQLogLevel.INFO.mValue) {
            Log.i(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void i(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.INFO.mValue) {
            Log.i(TAG + getCallerName(), pMessage, tr);
        }
    }

    public static void initWithLevel(String pLevel) {
        if (pLevel != null) {
            SQLog.v("log level: " + pLevel);
            SQLogLevel vLogLevel = SQLogLevel.fromName(pLevel);
            if (vLogLevel != null) {
                SQLog.mValue = vLogLevel.mValue;
            } else {
                SQLog.mValue = SQLogLevel.DEFAULT.mValue;
            }
        } else {
            SQLog.mValue = SQLogLevel.DEFAULT.mValue;
        }
    }

    public static void v(String pMessage) {
        if (mValue >= SQLogLevel.VERBOSE.mValue) {
            Log.v(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void v(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.VERBOSE.mValue) {
            Log.v(TAG + getCallerName(), pMessage, tr);
        }
    }

    public static void w(String pMessage) {
        if (mValue >= SQLogLevel.WARN.mValue) {
            Log.w(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void w(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.WARN.mValue) {
            Log.w(TAG + getCallerName(), pMessage, tr);
        }
    }

    public static void w(Throwable tr) {
        if (mValue >= SQLogLevel.WARN.mValue) {
            Log.w(TAG + getCallerName(), tr);
        }
    }

    public static void wtf(String pMessage) {
        if (mValue >= SQLogLevel.WTF.mValue) {
            Log.wtf(TAG + getCallerName(), pMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(pMessage);
        }
    }

    public static void wtf(String pMessage, Throwable tr) {
        if (mValue >= SQLogLevel.WTF.mValue) {
            Log.wtf(TAG + getCallerName(), pMessage, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (mValue >= SQLogLevel.WTF.mValue) {
            Log.wtf(TAG + getCallerName(), tr);
        }
    }

    private static String getCallerName() {
        return new Throwable().getStackTrace()[2].getClassName();
    }
}
