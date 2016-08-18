/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.sebastien.balard.android.squareup;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Sébastien BALARD on 25/12/2015.
 */
public class SQApplication extends MultiDexApplication {

    private static SQApplication mInstance;

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
		SQLog.initWith(SQLog.SQLogLevel.fromName(BuildConfig.LOG_LEVEL));
        mInstance = this;

        SQFabricUtils.CrashlyticsUtils.init(this);
        JodaTimeAndroid.init(this);

        checkBaseCurrency();
        checkDefaultConversionBase();
    }

    @Override
    public void onTrimMemory(int pLevel) {
        super.onTrimMemory(pLevel);
        if (pLevel == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            SQDatabaseHelper.release();
        }
    }

    private void checkDefaultConversionBase() {
        try {
            SQConversionBase vDefault = SQDatabaseHelper.getInstance(this).getConversionBaseDao().getDefault();
            if (vDefault == null) {
                setDefaultConversionBase();
            } else {
                SQLog.d("default conversion base is already set: " + vDefault.getCode());
            }
        } catch (SQLException pException) {
            SQLog.e("fail to check default conversion base", pException);
        }
    }

    private void setDefaultConversionBase() {
        Currency vLocaleCurrency = Currency.getInstance(Locale.getDefault());
        SQLog.d("android locale currency: " + vLocaleCurrency.getCurrencyCode());
        try {
            SQDatabaseHelper.getInstance(this).getConversionBaseDao().createDefaultWithCode(vLocaleCurrency
                    .getCurrencyCode());
        } catch (SQLException pException) {
            SQLog.e("fail to create default conversion base: " + vLocaleCurrency.getCurrencyCode());
        }
    }

    private void checkBaseCurrency() {
        try {
            SQCurrency vBase = SQDatabaseHelper.getInstance(this).getCurrencyDao().getBase();
            if (vBase == null) {
                setBaseCurrency();
            } else {
                SQLog.d("base currency is already set: " + vBase.getCode());
            }
        } catch (SQLException pException) {
            SQLog.e("fail to check base currency", pException);
        }
    }

    private void setBaseCurrency() {
        Currency vLocaleCurrency = Currency.getInstance(Locale.getDefault());
        SQLog.d("android locale currency: " + vLocaleCurrency.getCurrencyCode());
        try {
            SQDatabaseHelper.getInstance(this).getCurrencyDao().createBaseWithCode(vLocaleCurrency.getCurrencyCode());
        } catch (SQLException pException) {
            SQLog.e("fail to create base currency: " + vLocaleCurrency.getCurrencyCode());
        }
    }
}
