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
import com.sebastien.balard.android.squareup.data.daos.SQDealDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.value;

/**
 * Created by Sebastien BALARD on 22/01/2017.
 */

@DatabaseTable(tableName = SQConstants.TABLE_DEAL_NAME, daoClass = SQDealDaoImpl.class)
public class SQDeal {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_DEAL_COLUMN_ID, canBeNull = false)
    protected Long mId;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_CATEGORY, canBeNull = false)
    protected String mCategory;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_TAG, canBeNull = false)
    protected String mTag;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_DATE, dataType = DataType.DATE_TIME, canBeNull = false)
    protected DateTime mDate;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_VALUE, canBeNull = false)
    protected Float mValue;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_CURRENCY_RATE, canBeNull = false)
    protected Float mRate;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_CONVERSION_BASE_CODE, canBeNull = false)
    protected String mConversionBaseCode;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_LATITUDE)
    protected Double mLatitude;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_LONGITUDE)
    protected Double mLongitude;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_FK_CURRENCY_ID, canBeNull = false, foreign = true,
            foreignAutoRefresh = true)
    protected SQCurrency mCurrency;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_FK_OWNER_ID, canBeNull = false, foreign = true,
            foreignAutoRefresh = true)
    protected SQPerson mOwner;
    @DatabaseField(columnName = SQConstants.TABLE_DEAL_COLUMN_FK_EVENT_ID, canBeNull = false)
    protected Long mEventId;
    @ForeignCollectionField(eager = true, foreignFieldName = "mDeal")
    protected ForeignCollection<SQDebt> mDebts;

    protected SQDeal() {
        mDate = DateTime.now();
    }

    public SQDeal(String pTag) {
        this();
        mTag = pTag;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQDeal vOther = (SQDeal) pObject;

        return new EqualsBuilder().append(mTag, vOther.mTag).append(mDate, vOther.mDate).append(mValue, vOther
                .mValue).append(mRate, vOther.mRate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mTag).append(mDate).append(mValue).append(mRate).toHashCode();
    }

    @Override
    public String toString() {
        return "Deal [" + mId + ", " + mCategory + ", " + mTag + ", " + SQFormatUtils.formatDateTime(mDate) + ", " +
                mOwner + ", " + value + " " + mCurrency + " (" + SQFormatUtils.formatRate(mRate) + ", " +
                mConversionBaseCode + "), (" + mLatitude + ", " + mLongitude + "), eventId: " + mEventId +
                "]";
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

    public void setEventId(Long pEventId) {
        mEventId = pEventId;
    }

    public Long getId() {
        return mId;
    }

    public SQPerson getOwner() {
        return mOwner;
    }

    public void setOwner(SQPerson pOwner) {
        mOwner = pOwner;
    }

    public Float getRate() {
        return mRate;
    }

    public void setRate(Float pRate) {
        mRate = pRate;
    }

    public String getConversionBaseCode() {
        return mConversionBaseCode;
    }

    public void setConversionBaseCode(String pConversionBaseCode) {
        mConversionBaseCode = pConversionBaseCode;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String pTag) {
        mTag = pTag;
    }

    public Float getValue() {
        return mValue;
    }

    public void setValue(Float pValue) {
        mValue = pValue;
    }

    public List<SQDebt> getDebts() {
        if (mDebts != null) {
            List<SQDebt> vList = new ArrayList<>(mDebts);
            Collections.sort(vList, (pFirst, pSecond) -> pFirst.getRecipient().getName().compareTo(pSecond
                    .getRecipient().getName()));
            return vList;
        } else {
            return new ArrayList<>();
        }
    }
}
