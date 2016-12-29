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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
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
import com.sebastien.balard.android.squareup.misc.utils.SQFabricUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQPermissionsUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUserPreferencesUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.SQDrawerActivity;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQEventsListAdapter;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQHomeActivity extends SQDrawerActivity {

    @BindView(R.id.sq_activity_home_nestedscrollview_empty)
    protected NestedScrollView mEmptyView;
    @BindView(R.id.sq_activity_home_recyclerview)
    protected RecyclerView mRecyclerView;

    private ActionMode mActionMode;
    private SQEventsListAdapter mAdapter;
    private List<SQEvent> mListEvents;
    private Long mEventInsertedPosition;
    private Long mEventUpdatedPosition;
    private Long mEventIdToEdit;
    private String mEventName;

    //region static methods
    public static final Intent getIntent(Context pContext) {
        return new Intent(pContext, SQHomeActivity.class)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP)*/;
    }

    public static final Intent getIntentForNewEvent(Long pId, String pName) {
        return new Intent().putExtra(SQConstants.EXTRA_EVENT_ID, pId).putExtra(SQConstants.EXTRA_EVENT_NAME, pName);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLog.v("onCreate");
        setContentView(R.layout.sq_activity_home);
        ButterKnife.bind(this);

        initToolbar();
        initDrawer();
        initNavigationView();
        initRecyclerView();
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
                    mEventInsertedPosition = pData.getExtras().getLong(SQConstants.EXTRA_EVENT_ID);
                    mEventName = pData.getExtras().getString(SQConstants.EXTRA_EVENT_NAME);
                }
                break;
            case SQConstants.NOTIFICATION_REQUEST_EDIT_EVENT:
                if (pResultCode == RESULT_OK) {
                    mEventUpdatedPosition = pData.getExtras().getLong(SQConstants.EXTRA_EVENT_ID);
                    mEventName = pData.getExtras().getString(SQConstants.EXTRA_EVENT_NAME);
                }
                break;
            case SQConstants.NOTIFICATION_REQUEST_LOGIN_TO_CREATE_EVENT:
                if (pResultCode == RESULT_OK) {
                    //SQUserPreferencesUtils.setUserProfile(SQFirebaseUtils.getFirebaseUser());
                    refreshNavigationView();
                    startActivityForResult(SQEditEventActivity.getIntent(this), SQConstants.NOTIFICATION_REQUEST_CREATE_EVENT);
                }
                break;
            default:
                super.onActivityResult(pRequestCode, pResultCode, pData);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int pRequestCode, @NonNull String[] pPermissions, @NonNull int[]
            pGrantResults) {
        super.onRequestPermissionsResult(pRequestCode, pPermissions, pGrantResults);
        switch (pRequestCode) {
            case SQConstants.NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS:
                if (pGrantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(SQEditEventActivity.getIntentToEdit(this, mEventIdToEdit),
                            SQConstants.NOTIFICATION_REQUEST_EDIT_EVENT);
                } else {
                    SQDialogUtils.createSnackBarWithAction(this, mToolbar, Snackbar.LENGTH_LONG, R
                            .color.sq_color_warning, R.color.sq_color_white, R.string
                            .sq_message_warning_open_event_with_contacts_without_contacts_permissions, R.string
                            .sq_actions_allow, R.color.sq_color_white, pView -> {
                        startActivityForResult(SQPermissionsUtils.getIntentForApplicationSettings(this),
                                SQConstants.NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS);
                    }).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");
        mNavigationView.setCheckedItem(R.id.sq_menu_drawer_item_event);
        mToolbar.setTitle(getString(R.string.sq_commons_my_events));
        checkCurrenciesRates();

        refreshLayout();
    }
    //endregion

    //region ui events
    @OnClick(R.id.sq_activity_home_fab)
    protected void onNewEventButtonClicked(View pView) {
        SQLog.i("click on button: new event");
        if (SQUserPreferencesUtils.isUserConnected()) {
            startActivityForResult(SQEditEventActivity.getIntent(this), SQConstants.NOTIFICATION_REQUEST_CREATE_EVENT);
        } else {
            startActivityForResult(SQLoginActivity.getIntent(this), SQConstants.NOTIFICATION_REQUEST_LOGIN_TO_CREATE_EVENT);
        }
    }
    //endregion

    //region private methods
    public void deleteEvent(Long pEventId) {
        try {
            int vPosition = -1;
            for (int vIndex = 0; vIndex < mListEvents.size(); vIndex++) {
                if (mListEvents.get(vIndex).getId().equals(pEventId)) {
                    vPosition = vIndex;
                    break;
                }
            }
            SQEvent pEvent = SQDatabaseHelper.getInstance(this).getEventDao().queryForId(pEventId);
            SQDatabaseHelper.getInstance(this).getEventDao().delete(pEvent);
            mListEvents.remove(vPosition);
            mAdapter.notifyItemRemoved(vPosition);
            mAdapter.notifyItemRangeChanged(vPosition, mAdapter.getItemCount());
        } catch (SQLException pException) {
            SQLog.e("fail to delete event", pException);
            SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_delete_event),
                    Snackbar.LENGTH_LONG);
        }
    }

    public void updateEventToPosition(Long pEventId, String pEventName) {
        int vPosition = -1;
        for (int vIndex = 0; vIndex < mListEvents.size(); vIndex++) {
            if (mListEvents.get(vIndex).getId().equals(pEventId)) {
                vPosition = vIndex;
                break;
            }
        }
        mAdapter.notifyItemChanged(vPosition);
        mAdapter.notifyItemRangeChanged(vPosition, mAdapter.getItemCount());
        /*SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string.sq_message_success_update_event,
                pEventName), Snackbar.LENGTH_LONG).show();*/
    }

    public void insertEventAtPosition(Long pEventId, String pEventName) {
        int vPosition = -1;
        for (int vIndex = 0; vIndex < mListEvents.size(); vIndex++) {
            if (mListEvents.get(vIndex).getId().equals(pEventId)) {
                vPosition = vIndex;
                break;
            }
        }
        mAdapter.notifyItemInserted(vPosition);
        mAdapter.notifyItemRangeChanged(vPosition, mAdapter.getItemCount());
        /*SQDialogUtils.createSnackBarSuccess(mToolbar, getString(R.string.sq_message_success_create_event,
                pEventName), Snackbar.LENGTH_LONG).show();*/
    }

    /*private void performSelection(int pPosition) {
        //FIXME: ripple but not selection
        mAdapter.toggleSelection(pPosition);
        if (mAdapter.getSelectedItemsCount() == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(getResources().getQuantityString(R.plurals.sq_cab_events_selected, mAdapter
                    .getSelectedItemsCount(), mAdapter.getSelectedItemsCount()));
        }
    }*/

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
        new Thread(() -> {
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
        }).start();
    }

    private void refreshLayout() {
        mListEvents.clear();
        mListEvents.addAll(SQDatabaseHelper.getInstance(this).getEventDao().getAll());
        if (mEventInsertedPosition == null && mEventUpdatedPosition == null) {
            mAdapter.notifyDataSetChanged();
        } else if (mEventInsertedPosition != null) {
            insertEventAtPosition(mEventInsertedPosition, mEventName);
            mEventInsertedPosition = null;
            mEventName = null;
        } else {
            updateEventToPosition(mEventUpdatedPosition, mEventName);
            mEventUpdatedPosition = null;
            mEventName = null;
        }
        if (mListEvents.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        mListEvents = new ArrayList<>();
        mAdapter = new SQEventsListAdapter(mListEvents);
        mAdapter.setOnEventActionListener(new SQEventsListAdapter.OnEventActionListener() {
            @Override
            public SQActivity getActivity() {
                return SQHomeActivity.this;
            }

            @Override
            public void onOpen(Long pEventId) {
                SQLog.v("onOpen");
                startActivity(SQEventActivity.getIntentToOpen(SQHomeActivity.this, pEventId));
            }

            @Override
            public void onEdit(Long pEventId) {
                SQLog.v("onEdit");
                if (!SQPermissionsUtils.hasPermission(SQHomeActivity.this, Manifest.permission.READ_CONTACTS)) {
                    mEventIdToEdit = pEventId;
                    SQPermissionsUtils.requestPermission(SQHomeActivity.this, Manifest.permission.READ_CONTACTS, SQConstants
                            .NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS);
                } else {
                    startActivityForResult(SQEditEventActivity.getIntentToEdit(SQHomeActivity.this, pEventId),
                            SQConstants.NOTIFICATION_REQUEST_EDIT_EVENT);

                }
            }

            @Override
            public void onDuplicate(Long pEventId) {
                SQLog.v("onDuplicate");
                startActivityForResult(SQEditEventActivity.getIntentToDuplicate(SQHomeActivity.this, pEventId), SQConstants
                        .NOTIFICATION_REQUEST_CREATE_EVENT);
            }

            @Override
            public void onShare(Long pEventId) {
                SQLog.v("onShare");

            }

            @Override
            public void onDelete(Long pEventId) {
                SQLog.v("onDelete");
                SQDialogUtils.showDialogYesNo(SQHomeActivity.this, R.string.sq_dialog_title_warning, R
                                .string.sq_dialog_message_delete_event, android.R.string.ok, android.R.string.cancel,
                        (pDialogInterface, pWhich) -> {
                            deleteEvent(pEventId);
                            SQFabricUtils.AnswersUtils.logDeleteEvent();
                            pDialogInterface.dismiss();
                        }, (pDialogInterface, pWhich) -> pDialogInterface.dismiss());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder pViewHolder) {
                return true;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        /*mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(this, mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {
            @Override
            public void onClick(View pView, int pPosition) {
                SQLog.v("onClick");
                //if (mActionMode != null) {
                //    performSelection(pPosition);
                //}
                startActivity(SQEventActivity.getIntentToOpen(SQHomeActivity.this, mListEvents.get(pPosition).getId()));
            }

            @Override
            public void onLongClick(View pView, int pPosition) {
                SQLog.v("onLongClick");
                //if (mActionMode == null) {
                //    mActionMode = startSupportActionMode(mActionModeCallback);
                //}
                //performSelection(pPosition);
            }

            @Override
            public boolean isEnabled(int pPosition) {
                return true;
            }
        }));*/
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
