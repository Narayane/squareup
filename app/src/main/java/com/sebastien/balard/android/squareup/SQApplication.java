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

package com.sebastien.balard.android.squareup;

import android.app.Application;
import android.content.Context;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;
import com.sebastien.balard.android.squareup.ui.services.SQDataAsyncUpdateIntentService;

import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Sébastien BALARD on 25/12/2015.
 */
public class SQApplication extends Application {

    private static SQApplication mInstance;

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLog.v("onCreate");
        mInstance = this;

        SQLog.initWithLevel(BuildConfig.LOG_LEVEL);
        SQFabricUtils.SQCrashlyticsUtils.init(this);

        try {
            SQCurrency vBase = SQDatabaseHelper.getInstance(this).getCurrencyDao().getBase();
            if (vBase == null) {
                Currency vLocaleCurrency = Currency.getInstance(Locale.getDefault());
                SQLog.d("android locale currency: " + vLocaleCurrency.getCurrencyCode());
                SQDatabaseHelper.getInstance(this).getCurrencyDao().createBaseFromCode(vLocaleCurrency.getCurrencyCode());
            } else {
                SQLog.d("base currency is already set: " + vBase.getCode());
            }
        } catch (SQLException pException) {
            SQLog.e("fail to check base currency", pException);
        }

        // TODO: add delta, check it
        SQDataAsyncUpdateIntentService.startActionUpdateConversionBases(this);
    }
}
