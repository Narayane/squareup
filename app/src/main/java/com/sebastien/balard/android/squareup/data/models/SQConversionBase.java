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

package com.sebastien.balard.android.squareup.data.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sebastien.balard.android.squareup.data.daos.SQConversionBaseDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.util.HashMap;

/**
 * Created by Sebastien BALARD on 10/03/2016.
 */
@DatabaseTable(tableName = SQConstants.TABLE_CONVERSION_BASE_NAME, daoClass = SQConversionBaseDaoImpl.class)
public class SQConversionBase {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_CONVERSION_BASE_COLUMN_NAME_ID, canBeNull = false)
    protected Long mId;
    @SerializedName("timestamp")
    @DatabaseField(columnName = SQConstants.TABLE_CONVERSION_BASE_COLUMN_NAME_LAST_UPDATE, dataType = DataType.DATE_TIME)
    protected DateTime mLastUpdate;
    @SerializedName("base")
    @DatabaseField(columnName = SQConstants.TABLE_CONVERSION_BASE_COLUMN_NAME_CODE, width = 3, canBeNull = false,
            unique = true)
    protected String mCode;
    @SerializedName("rates")
    @DatabaseField(columnName = SQConstants.TABLE_CONVERSION_BASE_COLUMN_NAME_RATES, dataType = DataType.SERIALIZABLE)
    protected HashMap<String, Float> mRates;
    @DatabaseField(columnName = SQConstants.TABLE_CONVERSION_BASE_COLUMN_NAME_IS_DEFAULT, canBeNull = false)
    protected Boolean mIsDefault;

    public SQConversionBase() {
        mRates = new HashMap<>();
        mIsDefault = false;
    }

    public SQConversionBase(String pCode) {
        this();
        mCode = pCode;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQConversionBase vOther = (SQConversionBase) pObject;

        return new EqualsBuilder().append(mCode, vOther.mCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mCode).toHashCode();
    }

    public String getCode() {
        return mCode;
    }

    public Long getId() {
        return mId;
    }

    public DateTime getLastUpdate() {
        return mLastUpdate;
    }

    public HashMap<String, Float> getRates() {
        return mRates;
    }

    public Boolean isDefault() {
        return mIsDefault;
    }

    public void setLastUpdate(DateTime pLastUpdate) {
        mLastUpdate = pLastUpdate;
    }

    public void setRates(HashMap<String, Float> pRates) {
        mRates = pRates;
    }

    public void setIsDefault(Boolean pIsDefault) {
        mIsDefault = pIsDefault;
    }
}
