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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

/**
 * Created by Sebastien BALARD on 22/01/2017.
 */

public class SQDeal {

    @DatabaseField(generatedId = true, columnName = "deal_id", canBeNull = false)
    protected Long mId;
    @DatabaseField(columnName = "deal_type", canBeNull = false)
    protected String type;
    @DatabaseField(columnName = "deal_tag", canBeNull = false)
    protected String mTag;
    @DatabaseField(columnName = "deal_date", dataType = DataType.DATE_TIME, canBeNull = false)
    protected DateTime mDate;
    @DatabaseField(columnName = "deal_value", canBeNull = false)
    protected Float mValue;
    @DatabaseField(columnName = "deal_currency_rate", canBeNull = false)
    protected Float mRate;
    @DatabaseField(columnName = "deal_latitude")
    protected Double mLatitude;
    @DatabaseField(columnName = "deal_longitude")
    protected Double mLongitude;
    @DatabaseField(columnName = "fk_currency_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    protected SQCurrency mCurrency;
    @DatabaseField(columnName = "fk_owner_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    protected SQPerson mOwner;
    @DatabaseField(columnName = "fk_event_id", canBeNull = false)
    protected Long mEventId;

    protected SQDeal() {
        mDate = DateTime.now();
    }

    public SQDeal(String pTag) {
        this();
        mTag = pTag;
    }

    public SQCurrency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(SQCurrency pCurrency) {
        mCurrency = pCurrency;
    }

    public DateTime getDate() {
        return mDate;
    }

    public void setDate(DateTime pDate) {
        mDate = pDate;
    }

    public Long getEventId() {
        return mEventId;
    }

    public Long getId() {
        return mId;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public SQPerson getOwner() {
        return mOwner;
    }

    public Float getRate() {
        return mRate;
    }

    public String getTag() {
        return mTag;
    }

    public Float getValue() {
        return mValue;
    }
}
