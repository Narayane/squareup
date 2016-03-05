package com.sebastien.balard.android.squareup.misc.utils;

import com.sebastien.balard.android.squareup.misc.SQLog;

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

    public static Currency getCurrencyByCode(String pCode) {
        if (CURRENCIES == null) {
            loadCurrencies();
        }
        return CURRENCIES.get(pCode);
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
