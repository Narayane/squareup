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

package com.sebastien.balard.android.squareup.misc.utils;

import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by sbalard on 04/03/2016.
 */
public class SQCurrencyUtils {

    static Map<String, Currency> CURRENCIES;

    static Currency LOCALE_CURRENCY;

    static SQCurrency BASE_CURRENCY;

    static SQConversionBase DEFAULT_CONVERSION_BASE;

    public static Currency getLocaleCurrency() {
        if (LOCALE_CURRENCY == null) {
            LOCALE_CURRENCY = Currency.getInstance(Locale.getDefault());
        }
        return LOCALE_CURRENCY;
    }

    public static SQCurrency getBaseCurrency() throws SQLException {
        if (BASE_CURRENCY == null) {
            loadBaseCurrency();
        }
        return BASE_CURRENCY;
    }

    public static SQConversionBase getDefaultConversionBase() throws SQLException {
        if (DEFAULT_CONVERSION_BASE == null) {
            loadConversionBase();
        }
        return DEFAULT_CONVERSION_BASE;
    }

    public static List<Currency> getAllCurrencies() {
        if (CURRENCIES == null) {
            loadCurrencies();
        }
        List<Currency> vAllCurrencies = new ArrayList<Currency>(CURRENCIES.values());
        Collections.sort(vAllCurrencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency pFirst, Currency pSecond) {
                return pFirst.getDisplayName(Locale.getDefault()).compareTo(pSecond.getDisplayName(Locale.getDefault
                        ()));

            }
        });
        return vAllCurrencies;
    }

    public static void refreshConversionBase() {
        SQLog.v("refresh conversion base");
        DEFAULT_CONVERSION_BASE = null;
    }

    public static Currency getCurrencyByCode(String pCode) {
        if (CURRENCIES == null) {
            loadCurrencies();
        }
        return CURRENCIES.get(pCode);
    }

    private static void loadBaseCurrency() throws SQLException {
        BASE_CURRENCY = SQDatabaseHelper.getInstance(SQApplication.getContext()).getCurrencyDao().getBase();
    }

    private static void loadConversionBase() throws SQLException {
        DEFAULT_CONVERSION_BASE = SQDatabaseHelper.getInstance(SQApplication.getContext()).getConversionBaseDao()
                .findByCode(getBaseCurrency().getCode());
    }

    private static void loadCurrencies() {
        Set<Currency> vSet = Currency.getAvailableCurrencies();
        SQLog.d("android currencies count: " + vSet.size());
        CURRENCIES = new HashMap<String, Currency>();
        List<Currency> vAllCurrencies = new ArrayList<Currency>();
        vAllCurrencies.addAll(vSet);
        for (Currency vCurrency : vAllCurrencies) {
            CURRENCIES.put(vCurrency.getCurrencyCode(), vCurrency);
        }
    }
}
