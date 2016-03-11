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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created by Sebastien BALARD on 10/03/2016.
 */
@DatabaseTable(tableName = "sq_conversion_base", daoClass = SQConversionBaseDaoImpl.class)
public class SQConversionBase {

    @DatabaseField(generatedId = true, columnName = "conversion_base_id", canBeNull = false)
    Long mId;
    @SerializedName("timestamp")
    @DatabaseField(columnName = "conversion_base_last_update", dataType = DataType.DATE_TIME)
    DateTime mLastUpdate;
    @SerializedName("base")
    @DatabaseField(columnName = "conversion_base_code", width = 3, canBeNull = false, unique = true)
    String mCode;
    @SerializedName("rates")
    @DatabaseField(columnName = "conversion_base_rates", dataType = DataType.SERIALIZABLE)
    Map<String, Float> mRates;

    public SQConversionBase() {
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQConversionBase vConversionBase = (SQConversionBase) pObject;

        return new EqualsBuilder().append(mCode, vConversionBase.mCode).isEquals();
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

    public Map<String, Float> getRates() {
        return mRates;
    }
}
