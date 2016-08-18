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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchContactFragment;
import com.sebastien.balard.android.squareup.ui.fragments.SQSearchCurrencyFragment;
import com.sebastien.balard.android.squareup.ui.widgets.chips.SQChipsView;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Sebastien BALARD on 22/03/2016.
 */
public class SQEditEventActivity extends SQActivity implements SQSearchCurrencyFragment.OnCurrencySelectionListener,
        SQSearchContactFragment.OnContactsSelectionListener {

    @Bind(R.id.sq_widget_app_bar_toolbar)
    protected Toolbar mToolbar;
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

    private DateTime mStartDate;
    private DateTime mEndDate;
    private Currency mCurrency;

    //region static methods
    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQEditEventActivity.class);
    }
    //endregion

    //region activity lifecycle methods
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        setContentView(R.layout.sq_activity_edit_event);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DateTime vNow = DateTime.now();
        mStartDate = vNow;
        mEndDate = vNow;
        mTextViewStartDate.setText(SQFormatUtils.formatLongDate(mStartDate));
        mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mStartDate));
        mCurrency = SQCurrencyUtils.getLocaleCurrency();

        mEditTextCurrency.setText(getString(R.string.sq_format_currency_label, mCurrency.getDisplayName(Locale
                .getDefault()), mCurrency.getCurrencyCode()));
        mChipsViewParticipants.setChipsListener(new SQChipsView.ChipsListener() {
            @Override
            public void onChipAdded(SQChipsView.SQChip pChip) {

            }

            @Override
            public void onChipDeleted(SQChipsView.SQChip pChip) {

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
        SQUIUtils.SoftInput.showForced(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_create_event, pMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.sq_menu_create_event_item_create:
                SQLog.v("click on menu item: create");
                try {
                    //TODO: validation
                    String vName = mEditTextName.getText().toString();
                    createEvent(vName);
                    setResult(Activity.RESULT_OK, SQHomeActivity.getIntentForNewEvent(vName));
                    finish();
                } catch (SQLException pException) {
                    SQLog.e("fail to create event", pException);
                    SQDialogUtils.createSnackBarError(mToolbar, getString(R.string.sq_message_error_create_event),
                            Snackbar.LENGTH_LONG);
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
        SQLog.i("click on textview: start date");
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker pDatePicker, int pYear, int pMonthOfYear, int pDayOfMonth) {
                mStartDate = new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth);
                mTextViewStartDate.setText(SQFormatUtils.formatLongDate(mStartDate));
                mEndDate = mStartDate;
                mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mStartDate));
            }
        }, mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mStartDate.getDayOfMonth()).show();
    }

    @OnClick(R.id.sq_activity_edit_event_textview_end_date)
    protected void onEndDateTextViewClick(View pView) {
        SQLog.i("click on textview: end date");
        DatePickerDialog vDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker pDatePicker, int pYear, int pMonthOfYear, int pDayOfMonth) {
                mEndDate = new DateTime().withDate(pYear, pMonthOfYear + 1, pDayOfMonth);
                mTextViewEndDate.setText(SQFormatUtils.formatLongDate(mEndDate));
            }
        }, mEndDate.getYear(), mEndDate.getMonthOfYear() - 1, mEndDate.getDayOfMonth());
        vDatePickerDialog.getDatePicker().setMinDate(mStartDate.getMillis());
        vDatePickerDialog.show();
    }
    //endregion

    //region public methods
    public void openSearchContactFragment(String pContent) {
        SQSearchContactFragment vSearchContactFragment = SQSearchContactFragment.newInstance(pContent);
        getFragmentManager().beginTransaction().replace(R.id.sq_activity_edit_event_layout_content,
                vSearchContactFragment, SQSearchContactFragment.TAG).addToBackStack(SQSearchContactFragment.TAG)
                .commit();
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
        mCurrency = pCurrency;
        mEditTextCurrency.setText(getString(R.string.sq_format_currency_label, pCurrency.getDisplayName(Locale
                .getDefault()), pCurrency.getCurrencyCode()));
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
    private void createEvent(String pName) throws SQLException {
        SQCurrency vCurrency = SQDatabaseHelper.getInstance(this).getCurrencyDao().findByCode(mCurrency
                .getCurrencyCode());
        SQEvent vNewEvent = new SQEvent(pName, mStartDate, mEndDate, vCurrency);
        List<SQPerson> vParticipants = mChipsViewParticipants.getContacts();
        if (vParticipants.size() > 0) {
            SQDatabaseHelper.getInstance(this).getPersonDao().createAll(vParticipants, vNewEvent);
            SQLog.d("create new event with " + vParticipants.size() + " participants");
        } else {
            SQDatabaseHelper.getInstance(this).getEventDao().createOrUpdate(vNewEvent);
            SQLog.d("create new event");
        }
    }
    //endregion
}
