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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQDeal;
import com.sebastien.balard.android.squareup.data.models.SQDealCategory;
import com.sebastien.balard.android.squareup.data.models.SQDebt;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchContactFragment;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchCurrencyFragment;
import com.sebastien.balard.android.squareup.ui.widgets.SQValidationCallback;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQDealDebtsListAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.spinners.SQIconSpinnerAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.spinners.SQImageSpinnerAdapter;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    /*@BindView(R.id.sq_activity_edit_deal_layout_focus)
    protected LinearLayout mLayoutFocus;*/
    @BindView(R.id.sq_activity_edit_deal_edittext_tag)
    @NotEmpty(messageId = R.string.sq_validation_required_field)
    protected TextInputEditText mEditTextTag;
    @BindView(R.id.sq_activity_edit_deal_textview_date)
    protected AppCompatTextView mTextViewDate;
    @BindView(R.id.sq_activity_edit_deal_spinner_type)
    protected AppCompatSpinner mSpinnerType;
    @BindView(R.id.sq_activity_edit_deal_edittext_value)
    protected AppCompatEditText mEditTextValue;
    @BindView(R.id.sq_activity_edit_deal_edittext_currency)
    protected AppCompatEditText mEditTextCurrency;
    @BindView(R.id.sq_activity_edit_deal_spinner_owner)
    protected AppCompatSpinner mSpinnerOwner;
    @BindView(R.id.sq_activity_edit_deal_recyclerview_participants)
    protected RecyclerView mRecyclerView;

    private SQDealDebtsListAdapter mAdapterDebts;
    private List<SQDebt> mListDebts;
    private Handler mUIThread;

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
        mUIThread = new Handler(Looper.getMainLooper());
        /*mIsModeEdit = getIntent().hasExtra("EXTRA_MODE_EDIT");
        mIsModeDuplicate = getIntent().hasExtra("EXTRA_MODE_DUPLICATE");
        if (mIsModeEdit) {
            editEvent();
        } else {
            duplicateEvent();
        }*/
        mDeal = new SQDeal("");
        mDeal.setEventId(getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_ID));
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
        getMenuInflater().inflate(R.menu.sq_menu_create, pMenu);
        /*if (mIsModeEdit) {
            pMenu.getItem(0).setTitle(R.string.sq_actions_update);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.sq_menu_create_item_create:
                boolean vAtLeastOneActive = false;
                for (SQDebt vDebt : mListDebts) {
                    vAtLeastOneActive |= vDebt.isActive();
                }
                if (FormValidator.validate(this, new SQValidationCallback(this, false)) && vAtLeastOneActive) {
                    /*if (mIsModeEdit) {
                        SQLog.v("click on menu item: update event");
                        updateEvent();
                    } else {*/
                        SQLog.v("click on menu item: create deal");
                        createDeal();
                    //}
                } else {
                    SQDialogUtils.createSnackBarError(mToolbar, getString(R.string
                            .sq_message_error_at_least_one_active_participant), Snackbar.LENGTH_LONG).show();
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
        if (pStart > 1 && pCount > 0) {
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
            SQUIUtils.SoftInput.hide(SQEditDealActivity.this, mEditTextCurrency);
            //mLayoutFocus.requestFocus();
        } catch (SQLException pException) {
            SQLog.e("fail to load currency: " + vCode, pException);
            //setResult(Activity.RESULT_CANCELED, SQHomeActivity.getIntentForNewEvent(vName));
            finish();
        }
    }

    //region private methods
    private void createDeal() {
        try {
            mDeal.setTag(mEditTextTag.getText().toString());
            Float value;
            if (mEditTextValue.hasFocus()) {
                // value is not formatted
                value = SQFormatUtils.parseFloatEntry(mEditTextValue.getText().toString(), false);
            } else {
                // value is formatted
                value = SQFormatUtils.parseFloatEntry(mEditTextValue.getText().toString(), true);
            }
            mDeal.setRate(mDeal.getCurrency().getRate());
            mDeal.setConversionBaseCode(SQCurrencyUtils.getDefaultConversionBase().getCode());
            mDeal.setValue(value);
            mDeal.setOwner((SQPerson) mSpinnerOwner.getSelectedItem());
            /*if (this.latitude != null && this.longitude != null) {
                deal.setLatitude(this.latitude);
                deal.setLongitude(this.longitude);
            }*/
            if (mListDebts.size() > 0) {
                SQDatabaseHelper.getInstance(this).getDebtDao().createAll(mListDebts, mDeal);
                //SQLog.d("new event has been created with " + vParticipants.size() + " participants");
            }
            SQDatabaseHelper.getInstance(this).getDealDao().refresh(mDeal);
            /*if (mIsModeDuplicate) {
                SQFabricUtils.AnswersUtils.logDuplicateEvent(mEvent.getCurrency().getCode(), mEvent
                        .getParticipants().size());
            } else {
                SQFabricUtils.AnswersUtils.logCreateEvent(mEvent.getCurrency().getCode(), mEvent.getParticipants().size());
            }*/
            SQLog.d("create " + mDeal);
            SQUIUtils.SoftInput.hide(this);
            setResult(Activity.RESULT_OK);
            finish();
        } catch (SQLException pException) {
            SQLog.e("fail to create deal", pException);
            SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_create_deal),
                    Snackbar.LENGTH_LONG);
        }
    }

    private void refreshDebts() {
        int vUnitsCount = 0;
        for (SQDebt vDebt : mListDebts) {
            if (vDebt.isActive()) {
                vUnitsCount += vDebt.getRecipient().getWeight();
            }
        }
        SQLog.d("units count: " + vUnitsCount);
        Float vValuePerUnit = mDeal.getValue() / vUnitsCount;
        SQLog.d("debt value per unit: " + vValuePerUnit);
        for (SQDebt vDebt : mListDebts) {
            if (vDebt.isActive()) {
                vDebt.setValue(vValuePerUnit * vDebt.getRecipient().getWeight());
            } else {
                vDebt.setValue(0f);
            }
        }
        mAdapterDebts.notifyDataSetChanged();
    }

    private void initLayout() {

        if (mDeal.getTag() != null) {
            mEditTextTag.append(mDeal.getTag());
        }
        mTextViewDate.setText(SQFormatUtils.formatLongDateTime(mDeal.getDate(), getString(R.string.sq_commons_at)));

        initSpinnerType();
        initEditTextValue();
        initEditTextCurrency();

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

    private void initEditTextCurrency() {
        try {
            mDeal.setCurrency(SQDatabaseHelper.getInstance(this).getEventDao().getCurrencyForNewDeal(getIntent()
                    .getExtras().getLong(SQConstants.EXTRA_EVENT_ID)));
        } catch (SQLException pException) {
            try {
                String vCode = SQCurrencyUtils.getLocaleCurrency().getCurrencyCode();
                SQCurrency vCurrency = SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(vCode);
                mDeal.setCurrency(vCurrency);
            } catch (SQLException pException2) {
                SQLog.e("fail to init deal currency");
            }
        }
        mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mDeal.getCurrency().getName(),
                mDeal.getCurrency().getCode()));
        /*mEditTextCurrency.setOnEditorActionListener((pView, pActionId, pKeyEvent) -> {
            if (pActionId == EditorInfo.IME_ACTION_DONE) {
                mEditTextCurrency.clearFocus();
            }
            return true;
        });
        mEditTextCurrency.setOnFocusChangeListener((pView, pHasFocus) -> {
            if (pHasFocus) {
                mEditTextCurrency.setText("");
            } else {
                mEditTextCurrency.append(getString(R.string.sq_format_currency_label, mDeal.getCurrency().getName(),
                        mDeal.getCurrency().getCode()));
            }
        });*/
    }

    private void initEditTextValue() {
        mEditTextValue.setText(SQFormatUtils.formatAmount(mDeal.getValue()));
        /*mEditTextValue.setOnEditorActionListener((pView, pActionId, pKeyEvent) -> {
            if (pActionId == EditorInfo.IME_ACTION_DONE) {
                mEditTextValue.clearFocus();
            }
            return true;
        });*/
        mEditTextValue.setOnFocusChangeListener((pView, pHasFocus) -> {
            if (pHasFocus) {
                mEditTextValue.setText("");
            } else {
                Float vValue = SQFormatUtils.parseFloatEntry(mEditTextValue.getText().toString(), false);
                mDeal.setValue(vValue);
                mEditTextValue.setText(SQFormatUtils.formatAmount(mDeal.getValue()));
                refreshDebts();
            }
        });
    }

    private void initSpinnerOwner(List<SQPerson> pParticipants) {
        mSpinnerOwner.setAdapter(new SQImageSpinnerAdapter<>(this, pParticipants));
        int vPosition = 0;
        for (SQPerson owner : pParticipants) {
            if (owner.getId().equals(getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_OWNER_ID))) {
                break;
            }
            vPosition++;
        }
        mSpinnerOwner.setSelection(vPosition);
    }

    private void initSpinnerType() {
        List<SQDealCategory> vList = Arrays.asList(SQDealCategory.values());
        mSpinnerType.setAdapter(new SQIconSpinnerAdapter<>(this, vList));
        /*int vPosition = 0;
        for (SQPerson owner : vList) {
            if (owner.getId().equals(getIntent().getExtras().getLong(SQConstants.EXTRA_EVENT_OWNER_ID))) {
                break;
            }
            vPosition++;
        }
        mSpinnerOwner.setSelection(vPosition);*/
    }

    private void initRecyclerView(List<SQPerson> pParticipants) {
        mListDebts = new ArrayList<>();
        SQDebt debt;
        for (SQPerson vParticipant : pParticipants) {
            debt = new SQDebt(vParticipant);
            mListDebts.add(debt);
        }
        mAdapterDebts = new SQDealDebtsListAdapter(mListDebts);
        mAdapterDebts.setOnDebtActionListener(new SQDealDebtsListAdapter.OnDebtActionListener() {
            @Override
            public SQActivity getActivity() {
                return SQEditDealActivity.this;
            }

            @Override
            public void onToggle() {
                SQLog.v("onToggle");
                mUIThread.post(() -> refreshDebts());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterDebts);
    }
    //endregion
}
