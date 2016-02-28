package com.sebastien.balard.android.squareup;

import android.app.Application;

import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;

/**
 * Created by Sébastien BALARD on 25/12/2015.
 */
public class SQApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SQLog.initWithLevel(BuildConfig.LOG_LEVEL);
        SQFabricUtils.SQCrashlyticsUtils.init(this);
    }
}
