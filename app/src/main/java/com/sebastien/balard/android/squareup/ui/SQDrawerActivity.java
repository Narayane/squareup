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

package com.sebastien.balard.android.squareup.ui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFirebaseUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUserPreferencesUtils;
import com.sebastien.balard.android.squareup.ui.activities.SQCurrenciesListActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQHomeActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQLoginActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 02/10/2016.
 */

public class SQDrawerActivity extends SQActivity {

    @BindView(R.id.sq_activity_drawer_layout_drawer)
    protected DrawerLayout mDrawerLayout;
    @BindView(R.id.sq_activity_drawer_navigationview)
    protected NavigationView mNavigationView;

    protected AppCompatImageView mImageViewProfile;
    protected AppCompatTextView mTextViewDisplayName;
    protected AppCompatTextView mTextViewEmail;
    protected AppCompatButton mButtonConnect;
    protected AppCompatButton mButtonUpdateProfile;

    @Override
    protected void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        SQLog.v("onActivityResult");
        switch (pRequestCode) {
            case SQConstants.NOTIFICATION_REQUEST_LOGIN:
                if (pResultCode == RESULT_OK) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SQLog.v("onStart");
        SQFirebaseUtils.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");
        refreshNavigationView();
    }

    @Override
    public void onStop() {
        super.onStop();
        SQLog.v("onStop");
        SQFirebaseUtils.stop();
    }

    //region ui events
    @Override
    public void onBackPressed() {
        SQLog.v("onBackPressed");
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //endregion

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //region private methods
    protected void refreshNavigationView() {
        if (SQUserPreferencesUtils.isUserConnected()) {
            mImageViewProfile.setVisibility(View.VISIBLE);
            Picasso.with(this).load(SQUserPreferencesUtils.getUserPhotoUri()).placeholder(R.drawable.sq_ic_person_24dp)
                /*.transform(new SQRoundedTransformation(50, 1)).error(R.drawable.user_placeholder_error)*/.into
                    (mImageViewProfile);
            mTextViewDisplayName.setVisibility(View.VISIBLE);
            mTextViewDisplayName.setText(SQUserPreferencesUtils.getUserDisplayName());
            mTextViewEmail.setVisibility(View.VISIBLE);
            mTextViewEmail.setText(SQUserPreferencesUtils.getUserEmail());
            mButtonUpdateProfile.setVisibility(View.VISIBLE);
            mButtonConnect.setVisibility(View.GONE);
        } else {
            mImageViewProfile.setVisibility(View.GONE);
            mTextViewDisplayName.setVisibility(View.GONE);
            mTextViewEmail.setVisibility(View.GONE);
            mButtonUpdateProfile.setVisibility(View.GONE);
            mButtonConnect.setVisibility(View.VISIBLE);
        }
    }

    protected void initNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(pMenuItem -> {
            switch (pMenuItem.getItemId()) {
                case R.id.sq_menu_drawer_item_event:
                    SQLog.i("click on drawer menu item: events list");
                    startActivity(SQHomeActivity.getIntent(SQDrawerActivity.this));
                    break;
                case R.id.sq_menu_drawer_item_currency:
                    SQLog.i("click on drawer menu item: currencies list");
                    startActivity(SQCurrenciesListActivity.getIntent(SQDrawerActivity.this));
                    break;
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        mNavigationView.setCheckedItem(R.id.sq_menu_drawer_item_event);
        mImageViewProfile = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id
                .sq_widget_drawer_imageview_profile);
        mTextViewDisplayName = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id
                .sq_widget_drawer_textview_display_name);
        mTextViewEmail = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id
                .sq_widget_drawer_textview_email);
        mButtonConnect = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id
                .sq_widget_drawer_button_connect);
        mButtonConnect.setOnClickListener(pView -> {
            SQLog.i("click on button: connect");
            openLoginScreen();
        });
        mButtonUpdateProfile = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id
                .sq_widget_drawer_button_disconnect);
        mButtonUpdateProfile.setOnClickListener(pView -> {
            SQLog.i("click on button: update profile");
            openLoginScreen();
        });
    }

    protected void openLoginScreen() {
        startActivityForResult(SQLoginActivity.getIntent(this), SQConstants.NOTIFICATION_REQUEST_LOGIN);
    }

    protected void initDrawer() {
        ActionBarDrawerToggle vDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .sq_actions_open_drawer, R.string.sq_actions_close_drawer);
        mDrawerLayout.addDrawerListener(vDrawerToggle);
        vDrawerToggle.syncState();
    }
    //endregion
}
