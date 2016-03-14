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

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.sebastien.balard.android.squareup.BuildConfig;
import com.sebastien.balard.android.squareup.SQTestApplication;
import com.sebastien.balard.android.squareup.SQTestDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = SQTestApplication.class)
public class SQConversionBaseDaoImplUnitTest {

    private SQTestDatabaseHelper mDatabaseHelperTest;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mDatabaseHelperTest = SQTestDatabaseHelper.getInstance(context);
    }

    @After
    public void tearDown() {
        SQTestDatabaseHelper.release();
    }

    @Test
    public void testCreateConversionBase() throws Exception {

        SQConversionBaseDaoImpl vConversionBaseDao = mDatabaseHelperTest.getConversionBaseDao();
        List<SQConversionBase> vList = vConversionBaseDao.queryForAll();
        assertThat(vList.size(), is(equalTo(0)));

        vConversionBaseDao.createDefaultWithCode("USD");

        SQConversionBase vUSDConversionBase = vConversionBaseDao.findByCode("USD");
        vUSDConversionBase.setLastUpdate(new DateTime());
        HashMap<String, Float> vRates = new HashMap<>();
        vRates.put("USD", 1.0f);
        vRates.put("EUR", 0.5f);
        vUSDConversionBase.setRates(vRates);
        Dao.CreateOrUpdateStatus vStatus = vConversionBaseDao.createOrUpdate(vUSDConversionBase);
        assertThat(vStatus.isCreated(), is(false));
        assertThat(vStatus.isUpdated(), is(true));

        vList = vConversionBaseDao.queryForAll();
        assertThat(vList.size(), is(equalTo(1)));
        assertThat(vList.get(0).getRates().size(), is(equalTo(2)));
    }

    @Test
    public void testUpdateCurrenciesRates() throws Exception {

        SQConversionBaseDaoImpl vConversionBaseDao = mDatabaseHelperTest.getConversionBaseDao();
        List<SQConversionBase> vList = vConversionBaseDao.queryForAll();
        assertThat(vList.size(), is(equalTo(0)));

        vConversionBaseDao.createDefaultWithCode("EUR");

        SQConversionBase vUSDConversionBase = new SQConversionBase("USD");
        vUSDConversionBase.setLastUpdate(new DateTime());
        HashMap<String, Float> vRates = new HashMap<>();
        vRates.put("USD", 1.0f);
        vRates.put("EUR", 0.5f);
        vUSDConversionBase.setRates(vRates);
        vConversionBaseDao.createOrUpdate(vUSDConversionBase);

        vConversionBaseDao.updateDefault();

        SQConversionBase vEURConversionBase = vConversionBaseDao.getDefault();
        assertThat(vEURConversionBase.getLastUpdate(), is(equalTo(vUSDConversionBase.getLastUpdate())));
        assertThat(vEURConversionBase.isDefault(), is(equalTo(true)));
        assertThat(vEURConversionBase.getRates().size(), is(equalTo(2)));
        assertThat(vEURConversionBase.getRates().get("EUR"), is(equalTo(1.0f)));
        assertThat(vEURConversionBase.getRates().get("USD"), is(equalTo(2.0f)));
    }
}