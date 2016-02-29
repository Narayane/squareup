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
