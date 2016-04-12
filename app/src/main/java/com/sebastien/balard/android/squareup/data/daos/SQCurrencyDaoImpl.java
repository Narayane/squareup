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

    public SQCurrency findByCode(String pCode) throws SQLException {

        QueryBuilder<SQCurrency, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_CURRENCY_COLUMN_NAME_CODE, pCode);
        SQCurrency vCurrency = vQueryBuilder.queryForFirst();

        if (vCurrency == null) {
            vCurrency = new SQCurrency(pCode);
            create(vCurrency);
            SQLog.i("create currency: " + pCode);
        }
        return vCurrency;
    }

    public List<SQCurrency> getActivatedCurrencies() {
        QueryBuilder<SQCurrency, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.orderBy(SQConstants.TABLE_CURRENCY_COLUMN_NAME_IS_BASE, false);

        List<SQCurrency> vList = new ArrayList<>();
        try {
            vList = vQueryBuilder.query();
            SQLog.d("activated currencies count: " + vList.size());
        } catch (SQLException pException) {
            SQLog.e("fail to get activated currencies");
        }
        return vList;
    }

    public SQCurrency getBase() throws SQLException {
        QueryBuilder<SQCurrency, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_CURRENCY_COLUMN_NAME_IS_BASE, true);
        return vQueryBuilder.queryForFirst();
    }

    public void createBaseWithCode(String pCode) throws SQLException {
        SQCurrency vNewBase = new SQCurrency(pCode);
        vNewBase.setIsBase(true);
        create(vNewBase);
        SQLog.i("create base currency: " + pCode);
    }
}
