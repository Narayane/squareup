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

    private static SQLogLevel mLevel;

    public static void initWith(SQLogLevel pLevel) {
        mLevel = pLevel;
        v("set OLSLog level to: " + mLevel);
    }

    public static void v(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.VERBOSE.mValue) {
            Log.v(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void v(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.VERBOSE.mValue) {
            Log.v(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    public static void d(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.DEBUG.mValue) {
            Log.d(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void d(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.DEBUG.mValue) {
            Log.d(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    public static void i(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.INFO.mValue) {
            Log.i(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void i(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.INFO.mValue) {
            Log.i(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    public static void w(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.WARN.mValue) {
            Log.w(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void w(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.WARN.mValue) {
            Log.w(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    public static void e(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.ERROR.mValue) {
            Log.e(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void e(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.ERROR.mValue) {
            Log.e(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    public static void wtf(String pMessage) {
        if (mLevel.mValue >= SQLogLevel.WTF.mValue) {
            Log.wtf(TAG + getCallerName(), pMessage);
            SQFabricUtils.CrashlyticsUtils.logMessage(pMessage);
        }
    }

    public static void wtf(String pMessage, Throwable pThrowable) {
        if (mLevel.mValue >= SQLogLevel.WTF.mValue) {
            Log.wtf(TAG + getCallerName(), pMessage + ": " + pThrowable.getMessage(), pThrowable);
            SQFabricUtils.CrashlyticsUtils.catchException(pThrowable);
        }
    }

    private static String getCallerName() {
        StackTraceElement[] vElements = new Throwable().getStackTrace();
        return vElements[2].getClassName();
    }
}
