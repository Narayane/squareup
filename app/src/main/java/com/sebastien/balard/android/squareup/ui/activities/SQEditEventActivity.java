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

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQPermissionsUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchContactFragment;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchCurrencyFragment;
import com.sebastien.balard.android.squareup.ui.widgets.chips.SQChipsView;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Sebastien BALARD on 22/03/2016.
 */
public class SQEditEventActivity extends SQActivity implements SQSearchCurrencyFragment.OnCurrencySelectionListener,
        SQSearchContactFragment.OnContactsSelectionListener {

    @Bind(R.id.sq_activity_edit_event_edittext_name)
    protected EditText mEditTextName;
    @Bind(R.id.sq_activity_edit_event_textview_start_date)
    protected TextView mTextViewStartDate;
    @Bind(R.id.sq_activity_edit_event_textview_end_date)
    protected TextView mTextViewEndDate;
    @Bind(R.id.sq_activity_edit_event_edittext_currency)
    protected EditText mEditTextCurrency;
    @Bind(R.id.sq_activity_edit_event_chipsview_participants)
    protected SQChipsView mChipsViewParticipants;

    private SQEvent mEvent;
    private boolean mIsEditMode;
    private String mSearchPattern;
    private List<SQPerson> mListPeopleToCreate;
    private List<SQPerson> mListPeopleToDelete;

    //region static methods
    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQEditEventActivity.class);
    }

    public final static Intent getIntentToEdit(Context pContext, Long pEventId) {
        return new Intent(pContext, SQEditEventActivity.class).putExtra(SQConstants.EXTRA_EVENT_ID, pEventId);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.sq_activity_edit_event);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        initToolbar();
        mIsEditMode = getIntent().hasExtra(SQConstants.EXTRA_EVENT_ID);
        initEvent();
        initLayout();
        if (mIsEditMode) {
            mListPeopleToCreate = new ArrayList<>();
            mListPeopleToDelete = new ArrayList<>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_create_event, pMenu);
        if (mIsEditMode) {
            pMenu.getItem(0).setTitle(R.string.sq_actions_update);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.sq_menu_create_event_item_create:
                //TODO: validation
                String vName = mEditTextName.getText().toString();
                if (mIsEditMode) {
                    SQLog.v("click on menu item: update event");
                    try {
                        updateEvent(vName);
                        setResult(Activity.RESULT_OK, SQHomeActivity.getIntentForNewEvent(vName));
                        finish();
                    } catch (SQLException pException) {
                        SQLog.e("fail to update event", pException);
                        SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_update_event),
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    SQLog.v("click on menu item: create event");
                    try {
                        createEvent(vName);
                        setResult(Activity.RESULT_OK, SQHomeActivity.getIntentForNewEvent(vName));
                        finish();
                    } catch (SQLException pException) {
                        SQLog.e("fail to create event", pException);
                        SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_create_event),
                                Snackbar.LENGTH_LONG);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(pMenuItem);
        }
    }

    @Override
    public void onBackPressed() {
        SQLog.v("onBackPressed");

        Fragment vSearchCurrencyFragment = getFragmentManager().findFragmentByTag(SQSearchCurrencyFragment.TAG);
        Fragment vSearchContactFragment = getFragmentManager().findFragmentByTag(SQSearchContactFragment.TAG);
        if (vSearchCurrencyFragment != null && vSearchCurrencyFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else if (vSearchContactFragment != null && vSearchContactFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int pRequestCode, @NonNull String[] pPermissions, @NonNull int[]
            pGrantResults) {
        super.onRequestPermissionsResult(pRequestCode, pPermissions, pGrantResults);
        switch (pRequestCode) {
            case SQConstants.NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS:
                if (pGrantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSearchContactFragment(mSearchPattern);
                    mSearchPattern = null;
                } else {
                    SQDialogUtils.createSnackBarWithAction(this, mToolbar, Snackbar.LENGTH_LONG, R
                            .color.sq_color_warning, R.color.sq_color_white, R.string
                            .sq_message_warning_request_contacts_permission, R.string.sq_actions_allow, R.color
                            .sq_color_white, pView -> {
                        startActivityForResult(SQPermissionsUtils.getIntentForApplicationSettings(this),
                                SQConstants.NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS);
                    }).show();
                    openSearchContactFragmentWithoutContactsPermissions(mSearchPattern);
                    mSearchPattern = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SQLog.v("onPause");
        SQUIUtils.SoftInput.hide(this);
    }
    //endregion

    //region ui events
    @OnTextChanged(value = R.id.sq_activity_edit_event_edittext_currency, callback = OnTextChanged.Callback
            .TEXT_CHANGED)
    protected void onCurrencyTextChanged(CharSequence pCharSequence, int pStart, int pBefore, int pCount) {
        if (pBefore == 2 && pCount > 2) {
            openSearchCurrencyFragment(pCharSequence.toString());
            mEditTextCurrency.getText().clear();
        }
    }

    @OnClick(R.id.sq_activity_edit_event_textview_start_date)
    protected void onStartDateTextViewClick(View pView) {
        SQLog.i("click on textview: event start date");
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker pDatePicker, int pYear, int pMonthOfYear, int pDayOfMonth) {
                mEvent.setStartDate(new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth));
                mTextViewStartDate.setText(SQFormatUtils.formatLongDate(mEvent.getStartDate()));
                if (mEvent.getEndDate().isBefore(mEvent.getStartDate())) {
                    mEvent.setEndDate(mEvent.getStartDate());
                }
                mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mEvent.getEndDate()));
            }
        }, mEvent.getStartDate().getYear(), mEvent.getStartDate().getMonthOfYear() - 1, mEvent.getStartDate()
                .getDayOfMonth()).show();
    }

    @OnClick(R.id.sq_activity_edit_event_textview_end_date)
    protected void onEndDateTextViewClick(View pView) {
        SQLog.i("click on textview: event end date");
        DatePickerDialog vDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker pDatePicker, int pYear, int pMonthOfYear, int pDayOfMonth) {
                mEvent.setEndDate(new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth));
                mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mEvent.getEndDate()));
            }
        }, mEvent.getEndDate().getYear(), mEvent.getEndDate().getMonthOfYear() - 1, mEvent.getEndDate().getDayOfMonth());
        vDatePickerDialog.getDatePicker().setMinDate(mEvent.getStartDate().getMillis());
        vDatePickerDialog.show();
    }
    //endregion

    //region public methods
    public void openSearchContactFragmentWithoutContactsPermissions(String pContent) {
        SQSearchContactFragment vSearchContactFragment = SQSearchContactFragment.newInstanceWithoutContactsPermissions(pContent);
        getFragmentManager().beginTransaction().replace(R.id.sq_activity_edit_event_layout_content,
                vSearchContactFragment, SQSearchContactFragment.TAG).addToBackStack(SQSearchContactFragment.TAG)
                .commit();
    }

    public void openSearchContactFragment(String pContent) {
        if (!SQPermissionsUtils.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            mSearchPattern = pContent;
            SQPermissionsUtils.requestPermission(this, Manifest.permission.READ_CONTACTS, SQConstants
                    .NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS);
        } else {
            SQSearchContactFragment vSearchContactFragment = SQSearchContactFragment.newInstance(pContent);
            getFragmentManager().beginTransaction().replace(R.id.sq_activity_edit_event_layout_content,
                    vSearchContactFragment, SQSearchContactFragment.TAG).addToBackStack(SQSearchContactFragment.TAG)
                    .commit();
        }
    }

    public void openSearchCurrencyFragment(String pContent) {
        SQSearchCurrencyFragment vSearchCurrencyFragment = SQSearchCurrencyFragment.newInstance(pContent);
        getFragmentManager().beginTransaction().replace(R.id.sq_activity_edit_event_layout_content,
                vSearchCurrencyFragment, SQSearchCurrencyFragment.TAG).addToBackStack(SQSearchCurrencyFragment.TAG)
                .commit();
    }

    @Override
    public void onCurrencySelected(Currency pCurrency) {
        SQLog.i("select currency: " + pCurrency.getCurrencyCode());
        String vCode = pCurrency.getCurrencyCode();
        try {
            mEvent.setCurrency(SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(vCode));
            mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mEvent.getCurrency().getName(),
                    mEvent.getCurrency().getCode()));
            mChipsViewParticipants.getEditText().requestFocus();
        } catch (SQLException pException) {
            SQLog.e("fail to load currency: " + vCode, pException);
            //setResult(Activity.RESULT_CANCELED, SQHomeActivity.getIntentForNewEvent(vName));
            finish();
        }
    }

    @Override
    public void onContactsSelected(List<SQPerson> vContacts) {
        for (SQPerson vPerson : vContacts) {
            Uri vUri = null;
            if (vPerson.getPhotoUriString() != null) {
                vUri = Uri.parse(vPerson.getPhotoUriString()) ;
            }
            mChipsViewParticipants.addChip(vPerson.getName(), vUri, vPerson);
        }
    }
    //endregion

    //region private methods
    private void updateEvent(String pName) throws SQLException {
        mEvent.setName(pName);
        SQDatabaseHelper.getInstance(this).getEventDao().createOrUpdate(mEvent);
        if (mListPeopleToDelete.size() > 0) {
            SQDatabaseHelper.getInstance(SQEditEventActivity.this).getPersonDao().deleteAll(mListPeopleToDelete);
            mListPeopleToDelete.clear();
        }
        if (mListPeopleToCreate.size() > 0) {
            SQDatabaseHelper.getInstance(this).getPersonDao().createAll(mListPeopleToCreate, mEvent);
            mListPeopleToCreate.clear();
        }
        SQLog.v("event has been updated");
    }

    private void createEvent(String pName) throws SQLException {
        mEvent.setName(pName);
        List<SQPerson> vParticipants = mChipsViewParticipants.getContacts();
        if (vParticipants.size() > 0) {
            SQDatabaseHelper.getInstance(this).getPersonDao().createAll(vParticipants, mEvent);
            SQLog.d("new event has been created with " + vParticipants.size() + " participants");
        } else {
            SQDatabaseHelper.getInstance(this).getEventDao().createOrUpdate(mEvent);
            SQLog.d("new event has been created");
        }
    }

    private void initEvent() {
        DateTime vNow = DateTime.now();
        if (mIsEditMode) {
            Long vId = getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_ID);
            try {
                mEvent = SQDatabaseHelper.getInstance(this).getEventDao().queryForId(vId);
                for (SQPerson vPerson : mEvent.getParticipants()) {
                    Uri vUri = null;
                    if (vPerson.getPhotoUriString() != null) {
                        vUri = Uri.parse(vPerson.getPhotoUriString());
                    }
                    mChipsViewParticipants.addChip(vPerson.getName(), vUri, vPerson);
                }
            } catch (SQLException pException) {
                SQLog.e("fail to load event with id: " + vId, pException);
                //setResult(Activity.RESULT_CANCELED, SQHomeActivity.getIntentForNewEvent(vName));
                finish();
            }
        } else {
            String vCode = SQCurrencyUtils.getLocaleCurrency().getCurrencyCode();
            try {
                SQCurrency vCurrency = SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(vCode);
                mEvent = new SQEvent(null, vNow, vNow, vCurrency);
            } catch (SQLException pException) {
                SQLog.e("fail to load currency: " + vCode, pException);
                //setResult(Activity.RESULT_CANCELED, SQHomeActivity.getIntentForNewEvent(vName));
                finish();
            }
        }
    }

    private void initLayout() {
        if (mEvent.getName() != null) {
            mEditTextName.append(mEvent.getName());
        }
        mTextViewStartDate.setText(SQFormatUtils.formatLongDate(mEvent.getStartDate()));
        mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mEvent.getEndDate()));
        mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mEvent.getCurrency().getName(),
                mEvent.getCurrency().getCode()));
        mEditTextCurrency.setOnEditorActionListener((pTextView, pKeyCode, pKeyEvent) -> {
            if (pKeyCode == EditorInfo.IME_ACTION_NEXT) {
                mChipsViewParticipants.getEditText().requestFocus();
                return true;
            }
            return false;
        });
        mChipsViewParticipants.setChipsListener(new SQChipsView.ChipsListener() {
            @Override
            public void onChipAdded(SQChipsView.SQChip pChip) {
                if (mIsEditMode) {
                    mListPeopleToCreate.add(pChip.getContact());
                }
            }

            @Override
            public void onChipDeleted(SQChipsView.SQChip pChip) {
                if (mIsEditMode) {
                    mListPeopleToDelete.add(pChip.getContact());
                }
            }

            @Override
            public void onTextChanged(CharSequence pCharSequence) {
                if (pCharSequence.length() > 2) {
                    openSearchContactFragment(pCharSequence.toString());
                    mChipsViewParticipants.getEditText().getText().clear();
                }
            }

            @Override
            public void onContentValidated() {

            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //endregion
}
