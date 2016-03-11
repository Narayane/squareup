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
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by sbalard on 04/03/2016.
 */
@DatabaseTable(tableName = "sq_currency", daoClass = SQCurrencyDaoImpl.class)
public class SQCurrency {

    @DatabaseField(generatedId = true, columnName = "currency_id", canBeNull = false)
    Long mId;
    @DatabaseField(columnName = "currency_code", width = 3, canBeNull = false, unique = true)
    String mCode;
    @DatabaseField(columnName = SQConstants.TABLE_CURRENCY_COLUMN_NAME_RATE)
    Float mRate;
    @DatabaseField(columnName = SQConstants.TABLE_CURRENCY_COLUMN_NAME_BASE, canBeNull = false)
    Boolean mBase;

    private Currency mCurrency;

    public SQCurrency() {
        mBase = false;
    }

    public SQCurrency(String pCode, Float pRate) {
        this();
        mCode = pCode;
        mRate = pRate;
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
        return mBase;
    }

    public String getCode() {
        return mCode;
    }

    public Long getId() {
        return mId;
    }

    public Float getRate() {
        return mRate;
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

    public void setBase(Boolean pIsBase) {
        mBase = pIsBase;
    }
}