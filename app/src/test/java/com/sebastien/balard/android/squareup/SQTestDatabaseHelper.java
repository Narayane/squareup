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

import android.content.Context;

import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQTestDatabaseHelper extends SQDatabaseHelper {

    public SQTestDatabaseHelper(Context pContext) {
        super(pContext, null, null, 1, R.raw.sq_config_ormlite);
    }
}
