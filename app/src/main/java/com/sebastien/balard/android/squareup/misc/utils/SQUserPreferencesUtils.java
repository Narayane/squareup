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

package com.sebastien.balard.android.squareup.misc.utils;

import android.preference.PreferenceManager;

import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.misc.SQConstants;

/**
 * Created by Sebastien BALARD on 14/03/2016.
 */
public class SQUserPreferencesUtils {

    public static int getRatesUpdateFrequency() {
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.USER_PREFERENCE_CURRENCIES_UPDATE_FREQUENCY, "7"));
    }
}
