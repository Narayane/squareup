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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sebastien BALARD on 30/03/2016.
 */
public class SQAsyncUpdateService extends IntentService {

    static final String TAG = SQAsyncUpdateService.class.getSimpleName();

    public SQAsyncUpdateService() {
        super(TAG);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLog.v("update currencies rates");
                WSFacade.getLatestRates(new Callback<SQConversionBase>() {
                    @Override
                    public void onResponse(Call<SQConversionBase> pCall, Response<SQConversionBase> pResponse) {
                        if (pResponse.isSuccess()) {
                            SQConversionBase vBaseUSD = pResponse.body();
                            updateCurrenciesRates(vBaseUSD);
                        } else {
                            SQLog.e(pResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<SQConversionBase> pCall, Throwable pException) {
                        SQLog.e("fail to get latest rates: " + pException.getMessage());
                    }
                });
            }
        }).start();
    }

    private void updateCurrenciesRates(SQConversionBase pUSDConversionBase) {
        SQLog.v("conversion base: " + pUSDConversionBase.getCode());
        SQLog.v("last update: " + SQFormatUtils.formatDateAndTime(pUSDConversionBase.getLastUpdate()));
        SQLog.v("rates count: " + pUSDConversionBase.getRates().size());
        try {
            SQDatabaseHelper.getInstance(this).getConversionBaseDao().createOrUpdate(pUSDConversionBase);
            SQDatabaseHelper.getInstance(this).getConversionBaseDao().updateDefault();
        } catch (SQLException pException) {
            SQLog.e("fail to update conversion base: " + pUSDConversionBase.getCode());
        }
    }
}