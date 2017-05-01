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

package com.sebastien.balard.android.squareup.data.models;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.spinners.SQIconSpinnerAdapter;

/**
 * Created by Sebastien BALARD on 01/05/2017.
 */

public enum SQDealCategory implements SQIconSpinnerAdapter.SQIconSpinnerAdapterItem {
    // uncategorized
    Uncategorized(0, R.string.sq_commons_uncategorized, R.drawable.sq_ic_uncategorized_24dp),
    // food
    Food(1, R.string.sq_commons_food, R.drawable.sq_ic_food_24dp),
    // accommodation
    Accommodation(2, R.string.sq_commons_accommodation, R.drawable.sq_ic_accommodation_24dp),
    // restaurant
    Restaurant(3, R.string.sq_commons_restaurant, R.drawable.sq_ic_restaurant_24dp),
    // parking
    Parking(4, R.string.sq_commons_parking, R.drawable.sq_ic_parking_24dp),
    // taxi
    Taxi(5, R.string.sq_commons_taxi, R.drawable.sq_ic_taxi_24dp),
    // bar
    Party(6, R.string.sq_commons_party, R.drawable.sq_ic_party_24dp),
    // leisure
    Leisure(7, R.string.sq_commons_leisure, R.drawable.sq_ic_leisure_24dp),
    // transport
    Transport(8, R.string.sq_commons_transport, R.drawable.sq_ic_transport_24dp),
    // gas
    Gas(9, R.string.sq_commons_gas, R.drawable.sq_ic_gas_24dp),
    // toll
    Toll(10, R.string.sq_commons_toll, R.drawable.sq_ic_toll_24dp);

    private int mValue;
    private final int mLabelResId;
    private final int mIconResId;

    SQDealCategory(int pValue, int pLabelResId, int pIconResId) {
        mValue = pValue;
        mLabelResId = pLabelResId;
        mIconResId = pIconResId;
    }

    public int getLabelResourceId() {
        return mLabelResId;
    }

    public int getIconResourceId() {
        return mIconResId;
    }

    public static SQDealCategory fromValue(int pValue) {
        switch (pValue) {
            case 1:
                return Food;
            case 2:
                return Accommodation;
            case 3:
                return Restaurant;
            case 4:
                return Parking;
            case 5:
                return Taxi;
            case 6:
                return Party;
            case 7:
                return Leisure;
            case 8:
                return Transport;
            case 9:
                return Gas;
            case 10:
                return Toll;
            default:
                return Uncategorized;
        }
    }

    @Override
    public Long getItemId() {
        return (long) mValue;
    }

    @Override
    public String getItemLabel() {
        return SQApplication.getContext().getString(getLabelResourceId());
    }

    @Override
    public int getItemIconDrawable() {
        return getIconResourceId();
    }
}
