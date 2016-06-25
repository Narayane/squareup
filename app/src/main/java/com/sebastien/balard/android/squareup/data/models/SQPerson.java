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

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.daos.SQPersonDaoImpl;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.utils.SQContactUtils;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by Sebastien BALARD on 11/05/2016.
 */
@DatabaseTable(tableName = SQConstants.TABLE_PERSON_NAME, daoClass = SQPersonDaoImpl.class)
public class SQPerson implements Serializable, Comparable<SQPerson> {

    @DatabaseField(generatedId = true, columnName = SQConstants.TABLE_PERSON_COLUMN_ID, canBeNull = false)
    protected Long mId;
    @DatabaseField(columnName = SQConstants.TABLE_PERSON_COLUMN_NAME, canBeNull = false)
    protected String mName;
    @DatabaseField(columnName = SQConstants.TABLE_PERSON_COLUMN_EMAIL)
    protected String mEmail;
    @DatabaseField(columnName = SQConstants.TABLE_PERSON_COLUMN_WEIGHT)
    protected Integer mWeight;
    @DatabaseField(columnName = SQConstants.TABLE_PERSON_COLUMN_FK_CONTACT_ID)
    protected Long mContactId;
    @DatabaseField(columnName = SQConstants.TABLE_PERSON_COLUMN_FK_EVENT_ID, canBeNull = false, foreign = true,
            foreignAutoCreate = true, foreignAutoRefresh = true, columnDefinition = "integer references " +
            SQConstants.TABLE_EVENT_NAME + "(" + SQConstants.TABLE_EVENT_COLUMN_ID + ") on delete cascade")
    protected SQEvent mEvent;

    protected String mPhotoUriString;

    protected SQPerson() {
        mWeight = 1;
    }

    public SQPerson(Long pContactId) {
        this();
        mContactId = pContactId;
    }

    public SQPerson(String pName, String pEmail, Integer pWeight) {
        this();
        mName = pName;
        mEmail = pEmail;
        mWeight = pWeight;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) return true;

        if (pObject == null || getClass() != pObject.getClass()) return false;

        SQPerson vOther = (SQPerson) pObject;

        return new EqualsBuilder().append(mEmail, vOther.mEmail).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mEmail).toHashCode();
    }

    @Override
    public int compareTo(SQPerson pOther) {
        /*int order = 0;
        if (this.balance < person.balance) {
            order = -1;
        } else if (this.balance > person.balance) {
            order = 1;
        } else {
            order = mName.compareTo(pOther.mName);
        }
        return order;*/
        return new CompareToBuilder().append(this.mName, pOther.mName).toComparison();
    }

    public Long getContactId() {
        return mContactId;
    }

    public String getName() {
        if (mContactId != null && mName == null) {
            mName = SQContactUtils.getDisplayName(SQApplication.getContext(), mContactId);
        }
        return mName;
    }

    public Integer getWeight() {
        return mWeight;
    }

    public String getEmail() {
        if (mContactId != null && mEmail == null) {
            mEmail = SQContactUtils.getEmail(SQApplication.getContext(), mContactId);
        }
        return mEmail;
    }

    public String getPhotoUriString() {
        if (mContactId != null && mPhotoUriString == null) {
            Uri vUri = SQContactUtils.getPhotoUri(SQApplication.getContext(), mContactId);
            if (vUri != null) {
                mPhotoUriString = vUri.toString();
            }
        }
        return mPhotoUriString;
    }

    public Long getId() {
        return mId;
    }

    public void setEvent(SQEvent pEvent) {
        mEvent = pEvent;
    }
}
