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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.event.SQEventSynthesisFragment;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQViewPagerAdapter;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 29/12/2016.
 */

public class SQEventActivity extends SQActivity {

    @BindView(R.id.sq_widget_appbar_tablayout)
    protected TabLayout mTabLayout;
    @BindView(R.id.sq_activity_event_viewpager)
    protected ViewPager mViewPager;

    private SQEvent mEvent;

    //region static methods
    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQEventActivity.class);
    }

    public final static Intent getIntentToOpen(Context pContext, Long pEventId) {
        return new Intent(pContext, SQEventActivity.class).putExtra(SQConstants.EXTRA_EVENT_ID, pEventId);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.sq_activity_event);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        initToolbar();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");
        Long vId = getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_ID);
        try {
            mEvent = SQDatabaseHelper.getInstance(this).getEventDao().queryForId(vId);
            mToolbar.setTitle(mEvent.getName());
        } catch (SQLException pException) {
            SQLog.e("fail to load event with id: " + vId, pException);
            finish();
        }
    }
    //endregion

    //region public methods
    public SQEvent getEvent() {
        return mEvent;
    }
    //endregion

    //region private methods
    private void initViewPager() {
        SQViewPagerAdapter vViewPagerAdapter = new SQViewPagerAdapter(getSupportFragmentManager());
        vViewPagerAdapter.addFragment(new SQEventSynthesisFragment(), "Synthèse");
        vViewPagerAdapter.addFragment(new Fragment(), "Opérations");
        vViewPagerAdapter.addFragment(new Fragment(), "Dettes");
        mViewPager.setAdapter(vViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    //endregion
}
