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

package com.sebastien.balard.android.squareup;

import android.content.Context;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQTestDatabaseHelper extends SQDatabaseHelper {

    /*private static SQTestDatabaseHelper mInstance;

    private SQCurrencyDaoImpl mCurrencyDao;
    private SQConversionBaseDaoImpl mConversionBaseDao;
    private SQEventDaoImpl mEventDao;

    public static synchronized SQTestDatabaseHelper getInstance(Context pContext) {
        if (mInstance == null) {
            mInstance = OpenHelperManager.getHelper(pContext.getApplicationContext(), SQTestDatabaseHelper.class);
        }
        return mInstance;
    }

    public static synchronized void release() {
        if (mInstance != null) {
            OpenHelperManager.releaseHelper();
            mInstance = null;
        }
    }*/

    public SQTestDatabaseHelper(Context pContext) {
        super(pContext, null, null, 1, R.raw.sq_config_ormlite);
    }

    /*@Override
    public void onCreate(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource) {
        SQLog.v("onCreate");
        try {
            TableUtils.createTable(pConnectionSource, SQCurrency.class);
            TableUtils.createTable(pConnectionSource, SQConversionBase.class);
            TableUtils.createTable(pConnectionSource, SQEvent.class);
        } catch (SQLException pException) {
            SQLog.e("fail to create test database", pException);
            throw new RuntimeException(pException);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource, int pOldVersion, int pNewVersion) {
        SQLog.v("onUpgrade");
    }

    @Override
    public void onOpen(SQLiteDatabase pSQLiteDatabase) {
        super.onOpen(pSQLiteDatabase);
        SQLog.v("onOpen");
        if (!pSQLiteDatabase.isReadOnly()){
            pSQLiteDatabase.setForeignKeyConstraintsEnabled(true);
        }
    }

    public SQEventDaoImpl getEventDao() {
        if (mEventDao == null) {
            try {
                mEventDao = getDao(SQEvent.class);
            } catch (SQLException pException) {
                SQLog.e("fail to get event dao");
                throw new RuntimeException(pException);
            }
        }
        return mEventDao;
    }

    public SQConversionBaseDaoImpl getConversionBaseDao() {
        if (mConversionBaseDao == null) {
            try {
                mConversionBaseDao = getDao(SQConversionBase.class);
            } catch (SQLException pException) {
                SQLog.e("fail to get currency base dao");
                throw new RuntimeException(pException);
            }
        }
        return mConversionBaseDao;
    }

    public SQCurrencyDaoImpl getCurrencyDao() {
        if (mCurrencyDao == null) {
            try {
                mCurrencyDao = getDao(SQCurrency.class);
            } catch (SQLException pException) {
                SQLog.e("fail to get currency dao");
                throw new RuntimeException(pException);
            }
        }
        return mCurrencyDao;
    }*/
}
