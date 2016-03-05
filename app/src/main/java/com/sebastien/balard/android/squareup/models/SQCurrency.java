package com.sebastien.balard.android.squareup.models;

import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by sbalard on 04/03/2016.
 */
public class SQCurrency {

    Long mId;
    String mCode;
    Float mRate;
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

    public String getCurrencyCode() {
        if (mCurrency == null) {
            mCurrency = SQCurrencyUtils.getCurrencyByCode(mCode);
        }
        return mCurrency.getCurrencyCode();
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
}