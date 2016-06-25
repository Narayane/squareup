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

package com.sebastien.balard.android.squareup;

import com.sebastien.balard.android.squareup.data.daos.SQConversionBaseDaoImplTest;
import com.sebastien.balard.android.squareup.data.daos.SQCurrencyDaoImplTest;
import com.sebastien.balard.android.squareup.data.daos.SQEventDaoImplTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Sebastien BALARD on 24/06/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SQCurrencyDaoImplTest.class, SQConversionBaseDaoImplTest.class, SQEventDaoImplTest.class})
public class UnitTestsSuite {
}
