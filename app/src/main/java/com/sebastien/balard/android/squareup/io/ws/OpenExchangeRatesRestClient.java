/*
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
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

package com.sebastien.balard.android.squareup.io.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sebastien.balard.android.squareup.io.converters.SQMapConverter;
import com.sebastien.balard.android.squareup.io.converters.SQTimestampConverter;
import com.sebastien.balard.android.squareup.io.ws.openexchangerates.PublicResources;

import org.joda.time.DateTime;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Sebastien BALARD on 08/03/2016.
 */
public class OpenExchangeRatesRestClient {

    private static PublicResources mPublicApiService;

    private static OkHttpClient mOkHttpClient;

    private static Gson mGson;

    protected static PublicResources getPublicApiService() {

        if (mPublicApiService == null) {
            initGson();
            initOkHttpClient();
            initRestClient();
        }
        return mPublicApiService;
    }

    private static void initRestClient() {
        mPublicApiService = new Retrofit.Builder().baseUrl("https://openexchangerates.org/api/").client
                (mOkHttpClient).addConverterFactory(GsonConverterFactory.create(mGson)).addCallAdapterFactory
                (RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())).build().create(PublicResources.class);
    }

    private static void initOkHttpClient() {
        HttpLoggingInterceptor vInterceptor = new HttpLoggingInterceptor();
        vInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient().newBuilder().addInterceptor(vInterceptor).build();
    }

    private static void initGson() {
        mGson = new GsonBuilder().serializeNulls().registerTypeAdapter(DateTime.class, new SQTimestampConverter())
                .registerTypeAdapter(new TypeToken<Map<String, Float>>() {
        }.getType(), new SQMapConverter()).create();
    }
}
