package com.sebastien.balard.android.squareup.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.daos.SQCurrencyDaoImpl;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
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

    public SQDatabaseHelper(Context pContext) {
        super(pContext, DATABASE_NAME, null, DATABASE_VERSION, R.raw.sq_config_ormlite);
    }

    @Override
    public void onCreate(SQLiteDatabase pDatabase, ConnectionSource pConnectionSource) {
        SQLog.i("create database");
        try {
            TableUtils.createTable(pConnectionSource, SQCurrency.class);
        } catch (SQLException pException) {
            SQLog.e("fail to create database", pException);
            throw new RuntimeException(pException);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        super.close();
        mCurrencyDao = null;
        mInstance = null;
    }

    public static SQDatabaseHelper getInstance(Context pContext) {
        if (mInstance == null) {
            mInstance = OpenHelperManager.getHelper(pContext.getApplicationContext(), SQDatabaseHelper.class);
        }
        return mInstance;
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
