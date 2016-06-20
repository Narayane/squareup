/*
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
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

package com.sebastien.balard.android.squareup.data.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Sebastien BALARD on 10/03/2016.
 */
public class SQConversionBaseDaoImpl extends BaseDaoImpl<SQConversionBase, Long> {

    public SQConversionBaseDaoImpl(ConnectionSource pConnectionSource, DatabaseTableConfig<SQConversionBase>
            pTableConfig) throws SQLException {
        super(pConnectionSource, pTableConfig);
    }

    public void createDefaultWithCode(String pCode) throws SQLException {
        SQConversionBase vConversionBase = new SQConversionBase(pCode);
        vConversionBase.setIsDefault(true);
        create(vConversionBase);
        SQLog.i("create default conversion base: " + pCode);
    }

    public void updateDefault() throws SQLException {
        SQConversionBase vDefaultConversionBase = getDefault();
        if (!vDefaultConversionBase.getCode().equals("USD")) {
            SQConversionBase vUSDConversionBase = findByCode("USD");
            vDefaultConversionBase.getRates().clear();
            Float vConversionRate = vUSDConversionBase.getRates().get(vDefaultConversionBase.getCode());
            SQLog.d(vDefaultConversionBase.getCode()  + " / USD: " + SQFormatUtils.formatRate(vConversionRate));
            for (Map.Entry<String, Float> vEntry : vUSDConversionBase.getRates().entrySet()) {
                vDefaultConversionBase.getRates().put(vEntry.getKey(), vEntry.getValue() / vConversionRate);
            }
            vDefaultConversionBase.setLastUpdate(vUSDConversionBase.getLastUpdate());
            super.update(vDefaultConversionBase);
            SQLog.i("update currencies rates of default conversion base: " + vDefaultConversionBase.getCode());
        }
        SQCurrencyUtils.refreshConversionBase();
    }

    public SQConversionBase findByCode(String pCode) throws SQLException {
        QueryBuilder<SQConversionBase, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_CONVERSION_BASE_COLUMN_CODE, pCode);
        return vQueryBuilder.queryForFirst();
    }

    public SQConversionBase getDefault() throws SQLException {
        QueryBuilder<SQConversionBase, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_CONVERSION_BASE_COLUMN_IS_DEFAULT, true);
        return vQueryBuilder.queryForFirst();
    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(SQConversionBase pConversionBase) throws SQLException {
        SQConversionBase vExisting = findByCode(pConversionBase.getCode());
        if (vExisting != null) {
            vExisting.setLastUpdate(pConversionBase.getLastUpdate());
            vExisting.setRates(pConversionBase.getRates());
            return super.createOrUpdate(vExisting);
        } else {
            return super.createOrUpdate(pConversionBase);
        }
    }
}
