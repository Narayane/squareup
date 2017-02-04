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

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQDeal;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchContactFragment;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchCurrencyFragment;
import com.sebastien.balard.android.squareup.ui.widgets.SQValidationCallback;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQDealParticipantsListAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQPersonSpinnerAdapter;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Sebastien BALARD on 11/01/2017.
 */

public class SQEditDealActivity extends SQActivity implements SQSearchCurrencyFragment.OnCurrencySelectionListener {

    @BindView(R.id.sq_activity_edit_deal_view_focus)
    protected View mViewFocus;
    @BindView(R.id.sq_activity_edit_deal_edittext_tag)
    @NotEmpty(messageId = R.string.sq_validation_required_field)
    protected TextInputEditText mEditTextTag;
    @BindView(R.id.sq_activity_edit_deal_textview_date)
    protected AppCompatTextView mTextViewDate;
    @BindView(R.id.sq_activity_edit_deal_edittext_value)
    protected AppCompatEditText mEditTextValue;
    @BindView(R.id.sq_activity_edit_deal_edittext_currency)
    protected AppCompatEditText mEditTextCurrency;
    @BindView(R.id.sq_activity_edit_deal_spinner_owner)
    protected AppCompatSpinner mSpinnerOwner;
    @BindView(R.id.sq_activity_edit_deal_recyclerview_participants)
    protected RecyclerView mRecyclerView;

    private SQDealParticipantsListAdapter mAdapterParticipants;
    private List<SQPerson> mListParticipants;

    private SQDeal mDeal;
    /*private boolean mIsModeEdit;
    private boolean mIsModeDuplicate;
    private String mSearchPattern;*/

    //region static methods
    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQEditDealActivity.class);
    }

    public final static Intent getIntentToCreate(Context pContext, Long pEventId, Long pOwnerId) {
        return new Intent(pContext, SQEditDealActivity.class).putExtra(SQConstants.EXTRA_EVENT_ID, pEventId)
                .putExtra(SQConstants.EXTRA_EVENT_OWNER_ID, pOwnerId);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.sq_activity_edit_deal);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        initToolbar();
        /*mIsModeEdit = getIntent().hasExtra("EXTRA_MODE_EDIT");
        mIsModeDuplicate = getIntent().hasExtra("EXTRA_MODE_DUPLICATE");
        if (mIsModeEdit) {
            editEvent();
        } else {
            duplicateEvent();
        }*/
        mDeal = new SQDeal("");
        try {
            mDeal.setCurrency(SQDatabaseHelper.getInstance(this).getEventDao().getCurrencyForNewDeal(getIntent()
                    .getExtras().getLong(SQConstants.EXTRA_EVENT_ID)));
        } catch (SQLException pException) {
            try {
                String vCode = SQCurrencyUtils.getLocaleCurrency().getCurrencyCode();
                SQCurrency vCurrency = SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(vCode);
                mDeal.setCurrency(vCurrency);
            } catch (SQLException pException2) {
                SQLog.e("gros pb");
            }
        }
        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLog.v("onStart");
        FormValidator.startLiveValidation(this, mLayoutAppBar, new SQValidationCallback(this, false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_create_event, pMenu);
        /*if (mIsModeEdit) {
            pMenu.getItem(0).setTitle(R.string.sq_actions_update);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            /*case R.id.sq_menu_create_event_item_create:
                if (FormValidator.validate(this, new SQValidationCallback(this, false))) {
                    if (mIsModeEdit) {
                        SQLog.v("click on menu item: update event");
                        updateEvent();
                    } else {
                        SQLog.v("click on menu item: create event");
                        createEvent();
                    }
                }
                return true;*/
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
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SQLog.v("onStop");
        FormValidator.stopLiveValidation(this);
    }
    //endregion

    //region ui events
    @OnClick(R.id.sq_activity_edit_deal_textview_date)
    protected void onDateTextViewClick(View pView) {
        SQLog.i("click on textview: deal date");
        new DatePickerDialog(this, (pDatePicker, pYear, pMonthOfYear, pDayOfMonth) -> {
            DateTime vDate = new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth);
            new TimePickerDialog(this, (pTimePicker, pHour, pMinute) -> {
                mDeal.setDate(new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth).withTime(pHour,
                        pMinute, 0, 0));
                mTextViewDate.setText(SQFormatUtils.formatLongDateTime(mDeal.getDate(), getString(R.string.sq_commons_at)));
            }, vDate.getHourOfDay(), vDate.getMinuteOfHour(), true).show();
        }, mDeal.getDate().getYear(), mDeal.getDate().getMonthOfYear() - 1, mDeal.getDate().getDayOfMonth()).show();
    }

    @OnTextChanged(value = R.id.sq_activity_edit_deal_edittext_currency, callback = OnTextChanged.Callback
            .TEXT_CHANGED)
    protected void onCurrencyTextChanged(CharSequence pCharSequence, int pStart, int pBefore, int pCount) {
        if (pStart > 2 && pCount > 0) {
            openSearchCurrencyFragment(pCharSequence.toString());
            mEditTextCurrency.getText().clear();
        }
    }
    //endregion

    public void openSearchCurrencyFragment(String pContent) {
        SQSearchCurrencyFragment vSearchCurrencyFragment = SQSearchCurrencyFragment.newInstance(pContent);
        getFragmentManager().beginTransaction().replace(R.id.sq_activity_edit_deal_layout_content,
                vSearchCurrencyFragment, SQSearchCurrencyFragment.TAG).addToBackStack(SQSearchCurrencyFragment.TAG)
                .commit();
    }

    @Override
    public void onCurrencySelected(Currency pCurrency) {
        SQLog.i("select currency: " + pCurrency.getCurrencyCode());
        String vCode = pCurrency.getCurrencyCode();
        try {
            mDeal.setCurrency(SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(vCode));
            mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mDeal.getCurrency().getName(),
                    mDeal.getCurrency().getCode()));
            mViewFocus.requestFocus();
            SQUIUtils.SoftInput.hide(SQEditDealActivity.this);
        } catch (SQLException pException) {
            SQLog.e("fail to load currency: " + vCode, pException);
            //setResult(Activity.RESULT_CANCELED, SQHomeActivity.getIntentForNewEvent(vName));
            finish();
        }
    }

    //region private methods
    private void initLayout() {
        if (mDeal.getTag() != null) {
            mEditTextTag.append(mDeal.getTag());
        }
        mTextViewDate.setText(SQFormatUtils.formatLongDateTime(mDeal.getDate(), getString(R.string.sq_commons_at)));
        mEditTextValue.setText(SQFormatUtils.formatAmount(mDeal.getValue()));
        mEditTextValue.setOnEditorActionListener((pView, pActionId, pKeyEvent) -> {
            if (pActionId == EditorInfo.IME_ACTION_DONE) {
                mViewFocus.requestFocus();
                SQUIUtils.SoftInput.hide(SQEditDealActivity.this);
            }
            return true;
        });
        mEditTextValue.setOnFocusChangeListener((pView, pHasFocus) -> {
            if (pHasFocus) {
                mEditTextValue.setText("");
            } else {
                Float vValue = SQFormatUtils.parseFloatEntry(mEditTextValue.getText().toString());
                mEditTextValue.setText(SQFormatUtils.formatAmount(vValue));
                //computeDebtValues(value);
            }
        });
        mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mDeal.getCurrency().getName(),
                mDeal.getCurrency().getCode()));

        try {

            List<SQPerson> vParticipants = SQDatabaseHelper.getInstance(this).getPersonDao().findParticipantsByEventId
                    (getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_ID));
            initSpinnerOwner(vParticipants);
            initRecyclerView(vParticipants);

        } catch (SQLException pException) {
            SQLog.e("fail to load deal participants");
        }
        mEditTextTag.requestFocus();
    }

    private void initSpinnerOwner(List<SQPerson> pParticipants) {
        mSpinnerOwner.setAdapter(new SQPersonSpinnerAdapter<>(this, pParticipants, new SQPersonSpinnerAdapter
                .SQSpinnerAdapterItemAdapter<SQPerson>() {

            @Override
            public Long getId(SQPerson pItem) {
                return pItem.getId();
            }

            @Override
            public String getStringUri(SQPerson pItem) {
                return pItem.getPhotoUriString();
            }

            @Override
            public String getLabel(SQPerson pItem) {
                return pItem.getName();
            }
        }));
        int vPosition = 0;
        for (SQPerson owner : pParticipants) {
            if (owner.getId().equals(getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_OWNER_ID))) {
                break;
            }
            vPosition++;
        }
        mSpinnerOwner.setSelection(vPosition);
    }

    private void initRecyclerView(List<SQPerson> pParticipants) {
        mListParticipants = new ArrayList<>();
        mListParticipants.addAll(pParticipants);
        mAdapterParticipants = new SQDealParticipantsListAdapter(mListParticipants);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterParticipants);
    }
    //endregion
}
