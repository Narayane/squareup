package com.sebastien.balard.android.squareup;

import android.app.Application;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;

import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Sébastien BALARD on 25/12/2015.
 */
public class SQApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SQLog.initWithLevel(BuildConfig.LOG_LEVEL);
        SQFabricUtils.SQCrashlyticsUtils.init(this);

        try {
            SQCurrency vBase = SQDatabaseHelper.getInstance(this).getCurrencyDao().getBase();
            if (vBase == null) {
                Currency vLocaleCurrency = Currency.getInstance(Locale.getDefault());
                SQLog.d("android locale currency: " + vLocaleCurrency.getCurrencyCode());
                SQDatabaseHelper.getInstance(this).getCurrencyDao().createBaseFromCode(vLocaleCurrency.getCurrencyCode());
            } else {
                SQLog.d("base currency is already set");
            }
        } catch (SQLException pException) {
            SQLog.e("fail to check base currency", pException);
        }
    }
}
