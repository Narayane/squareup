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

package com.sebastien.balard.android.squareup.io.dto.openexchangerates;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created by Sebastien BALARD on 08/03/2016.
 */
public class OERLatestDto {

    @SerializedName("timestamp")
    DateTime mDate;
    @SerializedName("base")
    String mBase;
    @SerializedName("rates")
    Map<String, Float> mRates;

    protected DateTime getDate() {
        return mDate;
    }

    public String getBase() {
        return mBase;
    }

    public Map<String, Float> getRates() {
        return mRates;
    }
}
