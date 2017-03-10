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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sebastien.balard.android.squareup.data.daos.SQDebtDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Sebastien BALARD on 04/02/2017.
 */

@DatabaseTable(tableName = SQConstants.TABLE_DEBT_NAME, daoClass = SQDebtDaoImpl.class)
public class SQDebt {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_DEBT_COLUMN_ID, canBeNull = false)
    protected Long mId;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_IS_ACTIVE, canBeNull = false)
    protected Boolean mIsActive;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_VALUE, canBeNull = false)
    protected Float mValue;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_CURRENCY_RATE, canBeNull = false)
    protected Float mRate;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_FK_RECIPIENT_ID, canBeNull = false, foreign = true,
            foreignAutoRefresh = true)
    protected SQPerson mRecipient;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_FK_CURRENCY_ID, canBeNull = false, foreign = true,
            foreignAutoRefresh = true)
    protected SQCurrency mCurrency;
    @DatabaseField(columnName = SQConstants.TABLE_DEBT_COLUMN_FK_DEAL_ID, canBeNull = false, foreign = true,
            foreignAutoRefresh = true, columnDefinition = "integer references " + SQConstants.TABLE_DEAL_NAME + "(" +
            SQConstants.TABLE_DEAL_COLUMN_ID + ") on delete cascade")
    protected SQDeal mDeal;

    protected SQDebt() {
        mValue = 0f;
        mRate = 1f;
    }

    public SQDebt(SQPerson pRecipient) {
        this();
        mIsActive = true;
        mRecipient = pRecipient;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQDebt vOther = (SQDebt) pObject;

        return new EqualsBuilder().append(mIsActive, vOther.mIsActive).append(mRecipient, vOther.mRecipient).append(mValue,
                vOther.mValue).append(mRate, vOther.mRate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mIsActive).append(mRecipient).append(mValue).append(mRate)
                .toHashCode();
    }

    public Boolean isActive() {
        return mIsActive;
    }

    public SQCurrency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(SQCurrency pCurrency) {
        mCurrency = pCurrency;
    }

    public SQDeal getDeal() {
        return mDeal;
    }

    public Long getId() {
        return mId;
    }

    public Float getRate() {
        return mRate;
    }

    public SQPerson getRecipient() {
        return mRecipient;
    }

    public Float getValue() {
        return mValue;
    }

    public void setValue(Float pValue) {
        mValue = pValue;
    }

    public void setDeal(SQDeal pDeal) {
        mDeal = pDeal;
    }

    public void setActive(Boolean pActive) {
        mIsActive = pActive;
    }
}
