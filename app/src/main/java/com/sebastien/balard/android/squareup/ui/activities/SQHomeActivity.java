package com.sebastien.balard.android.squareup.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQHomeActivity extends SQActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQHomeActivity.class)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP)*/;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sq_activity_home);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle vDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .sq_navigation_drawer_open, R.string.sq_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(vDrawerToggle);
        vDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem pMenuItem) {
                switch (pMenuItem.getItemId()) {
                    case R.id.sq_drawer_menu_item_event:
                        SQLog.i("click on drawer menu item: event");
                        break;
                    case R.id.sq_drawer_menu_item_currency:
                        SQLog.i("click on drawer menu item: currency");
                        break;
                    case R.id.sq_drawer_menu_item_about:
                        SQLog.i("click on drawer menu item: about");
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.fab)
    protected void onNewEventButtonClick(View pView) {
        SQLog.i("click on button: new event");
        Snackbar.make(pView, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
