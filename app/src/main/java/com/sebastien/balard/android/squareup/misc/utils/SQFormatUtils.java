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

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Sebastien BALARD on 10/03/2016.
 */
public class SQFormatUtils {

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

    public static String formatDateAndTime(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            vLabel = formatDate(pDateTime) + " " + formatTime(pDateTime);
        }
        return vLabel;
    }

    public static String formatMediumDateAndTime(DateTime pDateTime) {
        String vLabel = "-";
        if (pDateTime != null) {
            vLabel = formatMediumDate(pDateTime) + " " + formatTime(pDateTime);
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
