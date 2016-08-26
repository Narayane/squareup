/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.sebastien.balard.android.squareup.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.io.ws.WSFacade;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUserPreferencesUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQEventsListAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.listeners.SQRecyclerViewItemTouchListener;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQHomeActivity extends SQActivity {

    @Bind(R.id.sq_activity_home_layout_drawer)
    protected DrawerLayout mDrawerLayout;
    @Bind(R.id.sq_activity_home_navigationview)
    protected NavigationView mNavigationView;
    @Bind(R.id.sq_activity_home_nestedscrollview_empty)
    protected NestedScrollView mEmptyView;
    @Bind(R.id.sq_activity_home_recyclerview)
    protected RecyclerView mRecyclerView;

    private ActionMode mActionMode;
    private SQEventsListAdapter mAdapter;
    private List<SQEvent> mEvents;

    //region static methods
    public static final Intent getIntent(Context pContext) {
        return new Intent(pContext, SQHomeActivity.class)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP)*/;
    }

    public static final Intent getIntentForNewEvent(String pName) {
        return new Intent().putExtra(SQConstants.EXTRA_EVENT_NAME, pName);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLog.v("onCreate");
        setContentView(R.layout.sq_activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle vDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .sq_actions_open_drawer, R.string.sq_actions_close_drawer);
        mDrawerLayout.addDrawerListener(vDrawerToggle);
        vDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem pMenuItem) {
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

        mEvents = new ArrayList<>();
        mAdapter = new SQEventsListAdapter(this, mEvents);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(this, mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {
            @Override
            public void onClick(View pView, int pPosition) {
                SQLog.v("onClick");
                if (mActionMode != null) {
                    performSelection(pPosition);
                }
            }

            @Override
            public void onLongClick(View pView, int pPosition) {
                SQLog.v("onLongClick");
                /*if (mActionMode == null) {
                    mActionMode = startSupportActionMode(mActionModeCallback);
                }
                performSelection(pPosition);*/
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_default, pMenu);
        return true;
    }

    @Override
    protected void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        SQLog.v("onActivityResult");
        switch (pRequestCode) {
            case SQConstants.NOTIFICATION_REQUEST_CREATE_EVENT:
                if (pResultCode == RESULT_OK) {
                    String vName = pData.getExtras().getString(SQConstants.EXTRA_EVENT_NAME);
                    SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string.sq_message_success_create_event,
                            vName), Snackbar.LENGTH_LONG).show();
                }
                break;
            case SQConstants.NOTIFICATION_REQUEST_EDIT_EVENT:
                if (pResultCode == RESULT_OK) {
                    String vName = pData.getExtras().getString(SQConstants.EXTRA_EVENT_NAME);
                    SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string.sq_message_success_update_event,
                            vName), Snackbar.LENGTH_LONG).show();
                }/* else {
                    String vName = pData.getExtras().getString(SQConstants.EXTRA_EVENT_NAME);
                    SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string.sq_message_success_create_event,
                            vName), Snackbar.LENGTH_LONG).show();
                }*/
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");
        mToolbar.setTitle(getString(R.string.sq_commons_my_events));
        checkCurrenciesRates();

        refreshLayout();
    }
    //endregion

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

    @OnClick(R.id.sq_activity_home_fab)
    protected void onNewEventButtonClick(View pView) {
        SQLog.i("click on button: new event");
        startActivityForResult(SQEditEventActivity.getIntent(this), SQConstants.NOTIFICATION_REQUEST_CREATE_EVENT);
    }
    //endregion

    //region private methods
    private void performSelection(int pPosition) {
        //FIXME: ripple but not selection
        mAdapter.toggleSelection(pPosition);
        if (mAdapter.getSelectedItemsCount() == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(getResources().getQuantityString(R.plurals.sq_cab_events_selected, mAdapter
                    .getSelectedItemsCount(), mAdapter.getSelectedItemsCount()));
        }
    }

    private void removeEventsSelection() {
        List<Integer> vPositions = mAdapter.getSelectedItemsPositions();
        boolean vSuccess = true;
        String vLabel = null;
        for (int vPosition : vPositions) {
            SQEvent vEvent = mAdapter.getItem(vPosition);
            try {
                SQDatabaseHelper.getInstance(SQHomeActivity.this).getEventDao().delete(vEvent);
                if (vLabel == null) {
                    vLabel = vEvent.getName();
                } else {
                    vLabel += ", " + vEvent.getName();
                }
            } catch (SQLException pException) {
                SQLog.e("fail to remove event:" + vEvent.getName());
                vSuccess &= false;
            }
        }
        refreshLayout();
        mActionMode.finish();
        if (vSuccess) {
            SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string
                    .sq_message_success_remove_events_selection, vLabel), Snackbar.LENGTH_LONG).show();
        } else {
            SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_remove_events_selection),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void checkCurrenciesRates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLog.d("check currencies rates last update");
                DateTime vNow = DateTime.now();
                int vFrequency = SQUserPreferencesUtils.getRatesUpdateFrequency();
                SQLog.v("update frequency: " + vFrequency + " day(s)");
                try {
                    DateTime vLastUpdate = SQCurrencyUtils.getDefaultConversionBase().getLastUpdate();
                    SQLog.v("last update: " + SQFormatUtils.formatDate(vLastUpdate));

                    boolean vTimeToCheck = true;
                    if (vLastUpdate != null) {
                        vTimeToCheck = Days.daysBetween(vLastUpdate, vNow).getDays() > vFrequency;
                    }
                    if (vTimeToCheck) {
                        mSubscriptions.add(subscribeToGetLatestRates());
                    } else {
                        SQLog.i("currencies rates are up-to-date");
                    }
                } catch (SQLException pException) {
                    SQLog.e("fail to get default conversion base");
                }
            }
        }).start();
    }

    private void refreshLayout() {
        mEvents.clear();
        mEvents.addAll(SQDatabaseHelper.getInstance(this).getEventDao().getAll());
        mAdapter.notifyDataSetChanged();
        if (mEvents.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    //endregion

    //region rx.Subscriptions
    private Subscription subscribeToGetLatestRates() {
        SQLog.v("update currencies rates");
        return WSFacade.getLatestRates().observeOn(Schedulers.computation()).subscribe(getObserverForGetLastestRates());
    }
    //endregion

    //region rx.Observers
    @NonNull
    private Observer<SQConversionBase> getObserverForGetLastestRates() {
        return new Observer<SQConversionBase>() {
            @Override
            public void onCompleted() {
                SQLog.v("onCompleted");
                try {
                    SQDatabaseHelper.getInstance(SQHomeActivity.this).getConversionBaseDao().updateDefault();
                } catch (SQLException pException) {
                    SQLog.e("fail to update default conversion base");
                }
            }

            @Override
            public void onError(Throwable pThrowable) {
                SQLog.v("onError");
                if (pThrowable instanceof HttpException) {
                    SQLog.e("fail to get latest rates: " + pThrowable.getMessage());
                } else {
                    SQLog.e("fail to get latest rates", pThrowable);
                }
            }

            @Override
            public void onNext(SQConversionBase pConversionBase) {
                SQLog.v("onNext");
                SQLog.d("conversion base: " + pConversionBase.getCode());
                SQLog.d("last update: " + SQFormatUtils.formatDateAndTime(pConversionBase.getLastUpdate()));
                SQLog.d("rates count: " + pConversionBase.getRates().size());
                try {
                    SQDatabaseHelper.getInstance(SQHomeActivity.this).getConversionBaseDao().createOrUpdate
                            (pConversionBase);
                } catch (SQLException pException) {
                    SQLog.e("fail to update conversion base: " + pConversionBase.getCode());
                }
            }
        };
    }
    //endregion

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode pActionMode, Menu pMenu) {
            getMenuInflater().inflate(R.menu.sq_menu_delete, pMenu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode pActionMode, Menu pMenu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode pActionMode, MenuItem pMenuItem) {
            removeEventsSelection();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode pActionMode) {
            mAdapter.clearSelection();
            mActionMode = null;
        }
    };
}
