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

package com.sebastien.balard.android.squareup.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.SQSettingsFragment;

import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 14/03/2016.
 */
public class SQSettingsActivity extends SQActivity {

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQSettingsActivity.class);
    }

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        SQLog.v("onCreate");
        setContentView(R.layout.sq_activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.activity_settings_content, new SQSettingsFragment(),
                SQSettingsFragment.TAG).commit();

    }
}
