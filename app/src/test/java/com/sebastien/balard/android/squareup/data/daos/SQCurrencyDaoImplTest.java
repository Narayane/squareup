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

import com.sebastien.balard.android.squareup.BuildConfig;
import com.sebastien.balard.android.squareup.SQTestApplication;
import com.sebastien.balard.android.squareup.SQTestDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = SQTestApplication.class)
public class SQCurrencyDaoImplTest {

    SQCurrencyDaoImpl mCurrencyDao;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mCurrencyDao = SQTestDatabaseHelper.getInstance(context).getCurrencyDao();
    }

    @After
    public void tearDown() {
        SQTestDatabaseHelper.release();
    }

    @Test
    public void testCreateCurrency() throws Exception {

        assertThat(mCurrencyDao.getActivatedCurrencies().size(), is(equalTo(0)));

        SQCurrency vCurrency = new SQCurrency("EUR");
        mCurrencyDao.create(vCurrency);

        assertThat(vCurrency.getId(), notNullValue());
        assertThat(vCurrency.getCode(), is(equalTo("EUR")));
        assertThat(vCurrency.isBase(), is(false));

        assertThat(mCurrencyDao.getActivatedCurrencies().size(), is(equalTo(1)));
    }

    @Test
    public void testCreateBaseCurrency() throws Exception {

        assertThat(mCurrencyDao.getActivatedCurrencies().size(), is(equalTo(0)));

        mCurrencyDao.createBaseWithCode("EUR");

        SQCurrency vBase = mCurrencyDao.getBase();
        assertThat(vBase.getId(), notNullValue());
        assertThat(vBase.getCode(), is(equalTo("EUR")));
        assertThat(vBase.isBase(), is(true));

        assertThat(mCurrencyDao.getActivatedCurrencies().size(), is(equalTo(1)));
    }
}