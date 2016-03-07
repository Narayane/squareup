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

package com.sebastien.balard.android.squareup.misc.utils;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sebastien.balard.android.squareup.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQFabricUtils {

    public static class SQCrashlyticsUtils {

        public static void init(Context pContext) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS) {
                Fabric.with(pContext, new Crashlytics());
            }
        }

        public static void sendLog(String pMessage) {
            if (BuildConfig.ENABLE_FABRIC_CRASHLYTICS && Fabric.isInitialized()) {
                Crashlytics.log(pMessage);
            }
        }
    }
}
