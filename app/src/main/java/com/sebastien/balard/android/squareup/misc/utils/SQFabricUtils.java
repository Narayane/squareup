/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.sebastien.balard.android.squareup.misc.utils;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.sebastien.balard.android.squareup.BuildConfig;
import com.sebastien.balard.android.squareup.misc.SQLog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQFabricUtils {

    public static class AnswersUtils {

        public static void logCreateEvent(String pCurrencyCode, int pParticipantsCount) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Answers.getInstance().logCustom(new CustomEvent("createEvent").putCustomAttribute("currency",
                        pCurrencyCode).putCustomAttribute("participantsCount", pParticipantsCount));
            }
        }

        public static void logDuplicateEvent(String pCurrencyCode, int pParticipantsCount) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Answers.getInstance().logCustom(new CustomEvent("duplicateEvent").putCustomAttribute("currency",
                        pCurrencyCode).putCustomAttribute("participantsCount", pParticipantsCount));
            }
        }

        public static void logDeleteEvent() {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Answers.getInstance().logCustom(new CustomEvent("deleteEvent"));
            }
        }
    }

    public static class CrashlyticsUtils {

        public static void init(Context pContext) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                SQLog.i("start crashlytics");
                Fabric.with(pContext, new Crashlytics());
            }
        }

        public static void logMessage(String pMessage) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Crashlytics.log(pMessage);
            }
        }

        public static void catchException(Throwable pThrowable) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Crashlytics.logException(pThrowable);
            }
        }

        public static void sendCustomKey(String pKey, Integer pValue) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Crashlytics.setInt(pKey, pValue);
            }
        }
    }
}
