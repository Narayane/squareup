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

package com.sebastien.balard.android.squareup.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.BuildConfig;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 07/03/2016.
 */
public class SQAboutActivity extends SQActivity {

    @BindView(R.id.sq_activity_about_textview_version)
    TextView mVersionTextView;

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQAboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sq_activity_about);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mVersionTextView.setText(getString(R.string.sq_app_version_label, BuildConfig.VERSION_NAME, BuildConfig
                .VERSION_CODE));
    }
}
