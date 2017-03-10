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

package com.sebastien.balard.android.squareup.data.daos;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sebastien.balard.android.squareup.data.models.SQDeal;
import com.sebastien.balard.android.squareup.data.models.SQDebt;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Sebastien BALARD on 20/06/2016.
 */
public class SQDebtDaoImpl extends BaseDaoImpl<SQDebt, Long> {

    public SQDebtDaoImpl(ConnectionSource pConnectionSource, DatabaseTableConfig<SQDebt> pTableConfig) throws
            SQLException {
        super(pConnectionSource, pTableConfig);
    }

    public void createAll(List<SQDebt> pListDebts, SQDeal pDeal) throws SQLException {
        for (SQDebt vDebt : pListDebts) {
            try {
                vDebt.setDeal(pDeal);
                vDebt.setCurrency(pDeal.getCurrency());
                createOrUpdate(vDebt);
                //SQLog.v("create deal: " + vDebt.get);
            } catch (SQLException pException) {
                SQLog.e("fail to create deal", pException);
            }
        }
    }
}
