/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.sebastien.balard.android.squareup.misc;

import android.util.Log;

import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;

import retrofit2.Response;

/**
 * Created by Sébastien BALARD on 27/07/2015.
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

    public static void e(Response pResponse) {
        if (mValue >= SQLogLevel.ERROR.mValue) {
            String vMessage = pResponse.code() + ": " + pResponse.message();
            Log.e(TAG + getCallerName(), vMessage);
            SQFabricUtils.SQCrashlyticsUtils.sendLog(vMessage);
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
