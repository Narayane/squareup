/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sebastien.balard.android.squareup.data.daos.SQCurrencyDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by sbalard on 04/03/2016.
 */
@DatabaseTable(tableName = SQConstants.TABLE_CURRENCY_NAME, daoClass = SQCurrencyDaoImpl.class)
public class SQCurrency implements Comparable<SQCurrency> {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_CURRENCY_COLUMN_NAME_ID, canBeNull = false)
    Long mId;
    @DatabaseField(columnName = SQConstants.TABLE_CURRENCY_COLUMN_NAME_CODE, width = 3, canBeNull = false, unique = true)
    String mCode;
    @DatabaseField(columnName = SQConstants.TABLE_CURRENCY_COLUMN_NAME_IS_BASE, canBeNull = false)
    Boolean mIsBase;

    private Currency mCurrency;

    public SQCurrency() {
        mIsBase = false;
    }

    public SQCurrency(String pCode) {
        this();
        mCode = pCode;
    }

    @Override
    public int compareTo(SQCurrency pCurrency) {
        if (isBase() == true && pCurrency.isBase() == false) {
            return -1;
        } else if (isBase() == false && pCurrency.isBase() == true) {
            return 1;
        } else {
            return  getRate().compareTo(pCurrency.getRate());
        }
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQCurrency vCurrency = (SQCurrency) pObject;

        return new EqualsBuilder().append(mCode, vCurrency.mCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mCode).toHashCode();
    }

    public Boolean isBase() {
        return mIsBase;
    }

    public String getCode() {
        return mCode;
    }

    public Long getId() {
        return mId;
    }

    public Float getRate() {
        try {
            return SQCurrencyUtils.getConversionBase().getRates().get(mCode);
        } catch (SQLException pException) {
            SQLog.e("fail to get " + mCode + " currency rate");
            return null;
        }
    }

    public String getName() {
        if (mCurrency == null) {
            mCurrency = SQCurrencyUtils.getCurrencyByCode(mCode);
        }
        return mCurrency.getDisplayName(Locale.getDefault());
    }

    public String getSymbol() {
        if (mCurrency == null) {
            mCurrency = SQCurrencyUtils.getCurrencyByCode(mCode);
        }
        return mCurrency.getSymbol(Locale.getDefault());
    }

    public void setIsBase(Boolean pIsBase) {
        mIsBase = pIsBase;
    }
}