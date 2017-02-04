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

package com.sebastien.balard.android.squareup.misc.utils;

import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.misc.SQLog;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Sebastien BALARD on 10/03/2016.
 */
public class SQFormatUtils {

    public static Float parseFloatEntry(String pEntry) {
        Float vResult = null;
        NumberFormat vFormat = NumberFormat.getInstance(Locale.ENGLISH);
        vFormat.setParseIntegerOnly(false);
        String value;
        Number vNumber = 0;
        try {
            if (!pEntry.equals("")) {
                value = pEntry;
                vNumber = vFormat.parse(value).floatValue();
            }
            vResult = vNumber.floatValue();
        } catch (ParseException pException) {
            SQLog.e("Parsing float entry problem", pException);
        }
        return vResult;
    }

    public static String formatAmount(Float pValue) {
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        Float vFormatted = 0f;
        if (pValue != null) {
            vFormatted = pValue;
        }
        return df.format(vFormatted);
    }

    public static String formatAmount(Float pValue, String pCurrencySymbol) {

        DecimalFormat vDecimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols vDecimalFormatSymbols = vDecimalFormat.getDecimalFormatSymbols();
        vDecimalFormatSymbols.setCurrencySymbol(pCurrencySymbol);
        vDecimalFormat.setDecimalFormatSymbols(vDecimalFormatSymbols);

        String vLabel = "-";
        if (pValue != null) {
            vLabel = vDecimalFormat.format(pValue);
        }
        return vLabel;
    }

    public static String formatRate(Float pValue) {

        DecimalFormat vDecimalFormat = new DecimalFormat("###,###,###,##0.000000");

        String vLabel = "-";
        if (pValue != null) {
            vLabel = vDecimalFormat.format(pValue);
        }
        return vLabel;
    }

    public static String formatDateTime(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            vLabel = formatDate(pDateTime) + " " + formatTime(pDateTime);
        }
        return vLabel;
    }

    public static String formatMediumDateTime(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            vLabel = formatMediumDate(pDateTime) + " " + formatTime(pDateTime);
        }
        return vLabel;
    }

    public static String formatLongDateTime(DateTime pDateTime, String pDelimiter) {
        String vLabel = "-";
        if (pDateTime != null) {
            vLabel = formatLongDate(pDateTime) + " " + pDelimiter + " " + formatTime(pDateTime);
        }
        return vLabel;
    }

    public static String formatDate(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            DateFormat vDateFormat = android.text.format.DateFormat.getDateFormat(SQApplication.getContext());
            vLabel = vDateFormat.format(pDateTime.toDate());
        }
        return vLabel;
    }

    public static String formatTime(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            DateFormat vTimeFormat = android.text.format.DateFormat.getTimeFormat(SQApplication.getContext());
            vLabel = vTimeFormat.format(pDateTime.toDate());
        }
        return vLabel;
    }

    public static String formatMediumDate(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            DateFormat vDateFormat = android.text.format.DateFormat.getMediumDateFormat(SQApplication.getContext());
            vLabel = vDateFormat.format(pDateTime.toDate());
        }
        return vLabel;
    }

    public static String formatLongDate(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            DateFormat vDateFormat = android.text.format.DateFormat.getLongDateFormat(SQApplication.getContext());
            vLabel = vDateFormat.format(pDateTime.toDate());
        }
        return vLabel;
    }
}
