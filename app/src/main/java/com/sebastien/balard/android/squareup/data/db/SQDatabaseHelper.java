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
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "square_up.db";
    public static final int DATABASE_VERSION = 1;

    private static SQDatabaseHelper mInstance;

    private SQCurrencyDaoImpl mCurrencyDao;
    private SQConversionBaseDaoImpl mConversionBaseDao;

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

    public SQDatabaseHelper(Context pContext) {
        super(pContext, DATABASE_NAME, null, DATABASE_VERSION, R.raw.sq_config_ormlite);
    }

    @Override
    public void onCreate(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource) {
        SQLog.i("create database");
        try {
            TableUtils.createTable(pConnectionSource, SQCurrency.class);
            TableUtils.createTable(pConnectionSource, SQConversionBase.class);
        } catch (SQLException pException) {
            SQLog.e("fail to create database", pException);
            throw new RuntimeException(pException);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

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
}
