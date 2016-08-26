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

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sebastien.balard.android.squareup.data.daos.SQEventDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastien BALARD on 27/03/2016.
 */
@DatabaseTable(tableName = SQConstants.TABLE_EVENT_NAME, daoClass = SQEventDaoImpl.class)
public class SQEvent {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_EVENT_COLUMN_ID, canBeNull = false)
    protected Long mId;
    @DatabaseField(columnName = SQConstants.TABLE_EVENT_COLUMN_NAME, canBeNull = false)
    protected String mName;
    @DatabaseField(columnName = SQConstants.TABLE_EVENT_COLUMN_START_DATE, dataType = DataType.DATE_TIME,
            canBeNull = false)
    protected DateTime mStartDate;
    @DatabaseField(columnName = SQConstants.TABLE_EVENT_COLUMN_END_DATE, dataType = DataType.DATE_TIME)
    protected DateTime mEndDate;
    @DatabaseField(columnName = SQConstants.TABLE_EVENT_COLUMN_FK_CURRENCY_ID, canBeNull = false, foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    protected SQCurrency mCurrency;
    @ForeignCollectionField(eager = true, foreignFieldName = "mEvent")
    protected ForeignCollection<SQPerson> mParticipants;

    protected Float mAmount;

    protected SQEvent() {
        mStartDate = DateTime.now();
    }

    public SQEvent(String pName, DateTime pStartDate, DateTime pEndDate, SQCurrency pCurrency) {
        this();
        mName = pName;
        mStartDate = pStartDate;
        mEndDate = pEndDate;
        mCurrency = pCurrency;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQEvent vOther = (SQEvent) pObject;

        return new EqualsBuilder().append(mName, vOther.mName).append(mStartDate, vOther.mStartDate).append(mEndDate,
                vOther.mEndDate).append(mCurrency, vOther.mCurrency).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mName).append(mStartDate).append(mEndDate).append(mCurrency)
                .toHashCode();
    }

    public SQCurrency getCurrency() {
        return mCurrency;
    }

    public DateTime getEndDate() {
        return mEndDate;
    }

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public DateTime getStartDate() {
        return mStartDate;
    }

    public Float getAmount() {
        return mAmount;
    }

    public List<SQPerson> getParticipants() {
        if (mParticipants != null) {
            return new ArrayList<>(mParticipants);
        } else {
            return new ArrayList<>();
        }
    }

    public void setAmount(Float pAmount) {
        mAmount = pAmount;
    }

    public void setCurrency(SQCurrency pCurrency) {
        mCurrency = pCurrency;
    }

    public void setEndDate(DateTime pEndDate) {
        mEndDate = pEndDate;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public void setStartDate(DateTime pStartDate) {
        mStartDate = pStartDate;
    }
}
