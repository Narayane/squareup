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
