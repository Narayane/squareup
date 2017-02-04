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
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastien BALARD on 30/03/2016.
 */
public class SQEventDaoImpl extends BaseDaoImpl<SQEvent, Long> {

    public SQEventDaoImpl(ConnectionSource pConnectionSource, DatabaseTableConfig<SQEvent> pTableConfig)
            throws SQLException {
        super(pConnectionSource, pTableConfig);
    }

    public List<SQEvent> getAll() {
        QueryBuilder<SQEvent, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.orderBy(SQConstants.TABLE_EVENT_COLUMN_START_DATE, false);

        List<SQEvent> vList = new ArrayList<>();
        try {
            vList = vQueryBuilder.query();
            SQLog.d("events count: " + vList.size());
        } catch (SQLException pException) {
            SQLog.e("fail to get events");
        }
        return vList;
    }

    public SQCurrency getCurrencyForNewDeal(Long pEventId) throws SQLException {
        QueryBuilder<SQEvent, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_EVENT_COLUMN_ID, pEventId);
        return vQueryBuilder.queryForFirst().getCurrency();
    }

    public SQCurrency getParticipants(Long pEventId) throws SQLException {
        QueryBuilder<SQEvent, Long> vQueryBuilder = queryBuilder();
        vQueryBuilder.where().eq(SQConstants.TABLE_EVENT_COLUMN_ID, pEventId);
        return vQueryBuilder.queryForFirst().getCurrency();
    }
}
