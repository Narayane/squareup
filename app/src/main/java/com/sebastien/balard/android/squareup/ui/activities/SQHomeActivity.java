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
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQHomeActivity extends SQActivity {

    @Bind(R.id.sq_activity_home_layout_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.sq_activity_home_navigationview)
    NavigationView mNavigationView;
    @Bind(R.id.sq_widget_app_bar_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.sq_activity_home_recyclerview)
    RecyclerView mRecyclerView;

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQHomeActivity.class)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP)*/;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLog.v("onCreate");
        setContentView(R.layout.sq_activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle vDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .sq_actions_open_drawer, R.string.sq_actions_close_drawer);
        mDrawerLayout.addDrawerListener(vDrawerToggle);
        vDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem pMenuItem) {
                switch (pMenuItem.getItemId()) {
                    case R.id.sq_menu_drawer_item_event:
                        SQLog.i("click on drawer menu item: events list");
                        break;
                    case R.id.sq_menu_drawer_item_currency:
                        SQLog.i("click on drawer menu item: currencies list");
                        startActivity(SQCurrenciesListActivity.getIntent(SQHomeActivity.this));
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mNavigationView.setCheckedItem(R.id.sq_menu_drawer_item_event);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_default, pMenu);
        return true;
    }

    @Override
    public void onBackPressed() {
        SQLog.v("onBackPressed");
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.sq_activity_home_fab)
    protected void onNewEventButtonClick(View pView) {
        SQLog.i("click on button: new event");
        SQDialogUtils.createSnackBarWarning(pView, "Not yet implemented", Snackbar.LENGTH_LONG).show();
    }
}
