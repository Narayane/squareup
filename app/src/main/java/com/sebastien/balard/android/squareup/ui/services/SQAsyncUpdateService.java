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

package com.sebastien.balard.android.squareup.ui.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.io.ws.WSFacade;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;

import java.sql.SQLException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Sebastien BALARD on 30/03/2016.
 */
public class SQAsyncUpdateService extends IntentService {

    static final String TAG = SQAsyncUpdateService.class.getSimpleName();

    protected CompositeSubscription mSubscriptions;

    public SQAsyncUpdateService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
        super.onDestroy();
    }

    public static void startActionUpdateCurrenciesRates(Context pContext) {
        Intent vIntent = new Intent(pContext, SQAsyncUpdateService.class);
        vIntent.setAction(SQConstants.ACTION_UPDATE_CURRENCIES_RATES);
        pContext.startService(vIntent);
    }

    @Override
    protected void onHandleIntent(Intent pIntent) {
        if (pIntent != null) {
            switch (pIntent.getAction()) {
                case SQConstants.ACTION_UPDATE_CURRENCIES_RATES:
                    handleActionUpdateCurrenciesRates();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleActionUpdateCurrenciesRates() {
        SQLog.v("update currencies rates");
        mSubscriptions.add(WSFacade.getLatestRates().observeOn(Schedulers.computation()).subscribe(mLoginCallObserver));
    }

    private Observer mLoginCallObserver = new Observer<SQConversionBase>() {
        @Override
        public void onCompleted() {
            SQLog.v("onCompleted");
            try {
                SQDatabaseHelper.getInstance(SQAsyncUpdateService.this).getConversionBaseDao().updateDefault();
            } catch (SQLException pException) {
                SQLog.e("fail to update default conversion base");
            }
        }

        @Override
        public void onError(Throwable pThrowable) {
            SQLog.v("onError");
            if (pThrowable instanceof HttpException) {
                SQLog.e("fail to get latest rates: " + pThrowable.getMessage());
            } else {
                SQLog.e("fail to get latest rates", pThrowable);
            }
        }

        @Override
        public void onNext(SQConversionBase pConversionBase) {
            SQLog.v("onNext");
            SQLog.d("conversion base: " + pConversionBase.getCode());
            SQLog.d("last update: " + SQFormatUtils.formatDateAndTime(pConversionBase.getLastUpdate()));
            SQLog.d("rates count: " + pConversionBase.getRates().size());
            try {
                SQDatabaseHelper.getInstance(SQAsyncUpdateService.this).getConversionBaseDao().createOrUpdate
                        (pConversionBase);
            } catch (SQLException pException) {
                SQLog.e("fail to update conversion base: " + pConversionBase.getCode());
            }
        }
    };
}