package com.sebastien.balard.android.squareup.data.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQCurrencyDaoImpl extends BaseDaoImpl<SQCurrency, Long> {

    public SQCurrencyDaoImpl(ConnectionSource pConnectionSource, DatabaseTableConfig<SQCurrency> pTableConfig)
            throws SQLException {
        super(pConnectionSource, pTableConfig);
    }

    public List<SQCurrency> getActivatedCurrenciesList() {
        QueryBuilder<SQCurrency, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.orderBy(SQConstants.TABLE_CURRENCY_COLUMN_NAME_BASE, false).orderBy(SQConstants
                .TABLE_CURRENCY_COLUMN_NAME_RATE, true);

        List<SQCurrency> vList = null;
        try {
            vList = vQueryBuilder.query();
        } catch (SQLException pException) {
            SQLog.e("fail to get activated currencies list");
            vList = new ArrayList<SQCurrency>();
        } finally {
            SQLog.d("activated currencies count: " + vList.size());
            return vList;
        }
    }

    public SQCurrency getBase() throws SQLException {
        QueryBuilder<SQCurrency, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_CURRENCY_COLUMN_NAME_BASE, true);
        return vQueryBuilder.queryForFirst();
    }

    public void createBaseFromCode(String pCode) {
        try {
            SQCurrency vNewBase = new SQCurrency(pCode, 1.0f);
            vNewBase.setBase(true);
            create(vNewBase);
            SQLog.i("create base currency: " + pCode);
        } catch (SQLException pException) {
            SQLog.e("fail to create base currency: " + pCode);
        }
    }
}
