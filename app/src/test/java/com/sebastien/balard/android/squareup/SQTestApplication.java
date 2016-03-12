/*
 * Square up android app
 * Copyright (C) 2016 Sebastien BALARD
 *
 * This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
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

package com.sebastien.balard.android.squareup;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import com.sebastien.balard.android.squareup.misc.SQLog;

/**
 * Created by Sébastien BALARD on 25/12/2015.
 */
public class SQTestApplication extends Application {

    private static SQTestApplication mInstance;

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLog.v("onCreate");
        mInstance = this;

        SQLog.initWithLevel(BuildConfig.LOG_LEVEL);
    }

    @Override
    public void onTrimMemory(int pLevel) {
        super.onTrimMemory(pLevel);
        if (pLevel == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            SQTestDatabaseHelper.release();
        }
    }
}
