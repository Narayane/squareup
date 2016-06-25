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

package com.sebastien.balard.android.squareup.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.daos.SQConversionBaseDaoImpl;
import com.sebastien.balard.android.squareup.data.daos.SQCurrencyDaoImpl;
import com.sebastien.balard.android.squareup.data.daos.SQEventDaoImpl;
import com.sebastien.balard.android.squareup.data.daos.SQPersonDaoImpl;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "square_up.db";
    public static final int DATABASE_VERSION = 2;

    private static SQDatabaseHelper mInstance;

    private SQCurrencyDaoImpl mCurrencyDao;
    private SQConversionBaseDaoImpl mConversionBaseDao;
    private SQEventDaoImpl mEventDao;
    private SQPersonDaoImpl mPersonDao;

    public static synchronized SQDatabaseHelper getInstance(Context pContext) {
        if (mInstance == null) {
            mInstance = OpenHelperManager.getHelper(pContext.getApplicationContext(), SQDatabaseHelper.class);
        }
        return mInstance;
    }

    public static synchronized void release() {
        if (mInstance != null) {
            OpenHelperManager.releaseHelper();
            mInstance = null;
        }
    }

    protected SQDatabaseHelper(Context pContext, String pDatabaseName, SQLiteDatabase.CursorFactory pCursorFactory, int
            pDatabaseVersion, int pConfigFileId) {
        super(pContext, pDatabaseName, pCursorFactory, pDatabaseVersion, pConfigFileId);
    }

    public SQDatabaseHelper(Context pContext) {
        super(pContext, DATABASE_NAME, null, DATABASE_VERSION, R.raw.sq_config_ormlite);
    }

    @Override
    public void onCreate(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource) {
        SQLog.v("onCreate");
        try {
            TableUtils.createTable(pConnectionSource, SQCurrency.class);
            TableUtils.createTable(pConnectionSource, SQConversionBase.class);
            TableUtils.createTable(pConnectionSource, SQEvent.class);
            TableUtils.createTable(pConnectionSource, SQPerson.class);
        } catch (SQLException pException) {
            SQLog.e("fail to create database", pException);
            throw new RuntimeException(pException);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource, int pOldVersion, int
            pNewVersion) {
        SQLog.v("onUpgrade");
        try {
            if (pOldVersion < 2) {
                String vRequestUpdateTableCurrency = "";
                getCurrencyDao().executeRaw(vRequestUpdateTableCurrency);
                /*getCurrencyDao().executeRaw("ALTER TABLE `sq_currency` ADD COLUMN `event_type` " + "INTEGER NOT " +
                        "NULL DEFAULT 0;");*/
                //TODO: put start date in end date when end date is null
            }
        } catch (SQLException pException) {
            SQLog.e("database upgrade problem (" + pOldVersion + " => " + pNewVersion, pException);
            throw new RuntimeException(pException);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase pSQLiteDatabase) {
        super.onOpen(pSQLiteDatabase);
        SQLog.v("onOpen");
        if (!pSQLiteDatabase.isReadOnly()){
            pSQLiteDatabase.setForeignKeyConstraintsEnabled(true);
        }
    }

    public SQPersonDaoImpl getPersonDao() {
        if (mPersonDao == null) {
            try {
                mPersonDao = getDao(SQPerson.class);
            } catch (SQLException pException) {
                SQLog.e("fail to get person dao");
                throw new RuntimeException(pException);
            }
        }
        return mPersonDao;
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
                SQLog.e("fail to get conversion base dao");
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
    }
}
