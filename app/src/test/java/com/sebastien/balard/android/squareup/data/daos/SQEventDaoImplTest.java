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
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.data.models.SQPerson;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = SQTestApplication.class)
public class SQEventDaoImplTest {

    private static final String EVENT_NAME = "name";

    private Context mApplicationContext;
    private SQEventDaoImpl mEventDao;
    private SQPersonDaoImpl mPersonDao;
    private SQCurrencyDaoImpl mCurrencyDao;
    private SQEvent mEvent;

    @Before
    public void setUp() {
        mApplicationContext = RuntimeEnvironment.application.getApplicationContext();
        mEventDao = SQTestDatabaseHelper.getInstance(mApplicationContext).getEventDao();
        mPersonDao = SQTestDatabaseHelper.getInstance(mApplicationContext).getPersonDao();
        mCurrencyDao = SQTestDatabaseHelper.getInstance(mApplicationContext).getCurrencyDao();
        mEvent = new SQEvent(EVENT_NAME, new DateTime(), null, new SQCurrency("EUR"));
    }

    @After
    public void tearDown() {
        mEvent = null;
        SQTestDatabaseHelper.release();
    }

    @Test
    public void testCreateEvent() throws Exception {

        assertThat(mEventDao.getAll().size(), is(equalTo(0)));

        mEventDao.createOrUpdate(mEvent);

        assertThat(mEvent.getId(), notNullValue());
        assertThat(mEvent.getName(), is(equalTo(EVENT_NAME)));
        assertThat(mEvent.getStartDate(), notNullValue());
        assertThat(mEvent.getEndDate(), nullValue());
        assertThat(mEvent.getCurrency(), notNullValue());
        assertThat(mEvent.getParticipants(), notNullValue());
        assertThat(mEvent.getParticipants().size(), is(equalTo(0)));

        assertThat(mEventDao.getAll().size(), is(equalTo(1)));
        assertThat(mCurrencyDao.queryForAll().size(), is(equalTo(1)));
    }

    @Test
    public void testCreateEventWithOneParticipant() throws Exception {

        assertThat(mEventDao.getAll().size(), is(equalTo(0)));

        SQPerson vPerson = new SQPerson("name", "email", 1);
        vPerson.setEvent(mEvent);
        mPersonDao.createOrUpdate(vPerson);

        mEventDao.refresh(mEvent);

        assertThat(mEvent.getId(), notNullValue());
        assertThat(mEvent.getName(), is(equalTo(EVENT_NAME)));
        assertThat(mEvent.getStartDate(), notNullValue());
        assertThat(mEvent.getEndDate(), nullValue());
        assertThat(mEvent.getCurrency(), notNullValue());
        assertThat(mEvent.getParticipants(), notNullValue());
        assertThat(mEvent.getParticipants().size(), is(equalTo(1)));

        assertThat(mEventDao.getAll().size(), is(equalTo(1)));
        assertThat(mPersonDao.queryForAll().size(), is(equalTo(1)));
        assertThat(mCurrencyDao.queryForAll().size(), is(equalTo(1)));
    }

    @Test
    public void testCreateEventWithTwoParticipants() throws Exception {

        assertThat(mEventDao.getAll().size(), is(equalTo(0)));

        SQPerson vPerson1 = new SQPerson("name1", "email1", 1);
        vPerson1.setEvent(mEvent);
        mPersonDao.createOrUpdate(vPerson1);

        SQPerson vPerson2 = new SQPerson("name2", "email2", 1);
        vPerson2.setEvent(mEvent);
        mPersonDao.createOrUpdate(vPerson2);

        mEventDao.refresh(mEvent);

        assertThat(mEvent.getId(), notNullValue());
        assertThat(mEvent.getName(), is(equalTo(EVENT_NAME)));
        assertThat(mEvent.getStartDate(), notNullValue());
        assertThat(mEvent.getEndDate(), nullValue());
        assertThat(mEvent.getCurrency(), notNullValue());
        assertThat(mEvent.getParticipants(), notNullValue());
        assertThat(mEvent.getParticipants().size(), is(equalTo(2)));

        assertThat(mEventDao.getAll().size(), is(equalTo(1)));
        assertThat(mPersonDao.queryForAll().size(), is(equalTo(2)));
        assertThat(mCurrencyDao.queryForAll().size(), is(equalTo(1)));
    }

    @Test
    public void testDeleteEventWithTwoParticipants() throws Exception {

        assertThat(mEventDao.getAll().size(), is(equalTo(0)));

        List<SQPerson> vParticipants = new ArrayList<>();
        vParticipants.add(new SQPerson("name1", "email1", 1));
        vParticipants.add(new SQPerson("name2", "email2", 1));

        mPersonDao.createAll(vParticipants, mEvent);
        mEventDao.refresh(mEvent);
        mEventDao.delete(mEvent);

        assertThat(mEventDao.getAll().size(), is(equalTo(0)));
        assertThat(mPersonDao.queryForAll().size(), is(equalTo(0)));
        assertThat(mCurrencyDao.queryForAll().size(), is(equalTo(1)));
    }
}