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

package com.sebastien.balard.android.squareup.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.widgets.SQValidationCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Sebastien BALARD on 14/12/2016.
 */

public class SQNewPersonDialogFragment extends DialogFragment {

    public static final String TAG = SQNewPersonDialogFragment.class.getSimpleName();

    public interface OnNewPersonListener {
        void onCreated(SQPerson pPerson);
    }

    @BindView(R.id.sq_dialog_create_contact_layout)
    protected LinearLayout mLayout;
    @BindView(R.id.sq_dialog_create_contact_edittext_name)
    @NotEmpty(messageId = R.string.sq_validation_required_field)
    protected TextInputEditText mEditTextName;
    @BindView(R.id.sq_dialog_create_contact_edittext_weight)
    @NotEmpty(messageId = R.string.sq_validation_required_field)
    protected TextInputEditText mEditTextWeight;
    @BindView(R.id.sq_dialog_create_contact_edittext_email)
    @NotEmpty(messageId = R.string.sq_validation_required_field)
    protected TextInputEditText mEditTextEmail;

    protected AlertDialog mAlertDialog;
    protected Unbinder mUnbinder;

    private SQValidationCallback mValidationCallback;

    protected OnNewPersonListener mListener;

    //region static methods
    public static SQNewPersonDialogFragment newInstance(OnNewPersonListener pListener, String pName) {
        SQNewPersonDialogFragment vDialogFragment = new SQNewPersonDialogFragment();
        vDialogFragment.setListener(pListener);
        Bundle vBundle = new Bundle();
        vBundle.putString("name", pName);
        vDialogFragment.setArguments(vBundle);
        return vDialogFragment;
    }
    //endregion

    @Override
    public void onViewCreated(View pView, @Nullable Bundle pSavedInstanceState) {
        SQLog.v("onViewCreated");
        mValidationCallback = new SQValidationCallback(getContext(), false);
        FormValidator.startLiveValidation(getActivity(), mLayout, mValidationCallback);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle pSavedInstanceState) {
        SQLog.v("onCreateDialog");
        View vView = getActivity().getLayoutInflater().inflate(R.layout.sq_dialog_create_contact, null);
        mUnbinder = ButterKnife.bind(this, vView);

        mEditTextName.append(getArguments().getString("name"));
        mEditTextName.requestFocus();
        mEditTextWeight.append("1");

        mAlertDialog = SQDialogUtils.createDialogWithCustomView(getActivity(), R.string
                .sq_dialog_title_create_contact, vView, R.string.sq_actions_create, null, (pDialogInterface, pWhich)
                -> {

            String vName = mEditTextName.getText().toString();
            String vEmail = mEditTextEmail.getText().toString();
            int vWeight = Integer.valueOf(mEditTextWeight.getText().toString());

            SQPerson vContact = new SQPerson(vName, vEmail, vWeight);
            mListener.onCreated(vContact);
        }, null, false);
        return mAlertDialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        SQLog.v("onStop");
        FormValidator.stopLiveValidation(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
    //endregion

    //region ui events
    @OnEditorAction(R.id.sq_dialog_create_contact_edittext_email)
    protected boolean onEmailEditTextEditorAction(int pActionId) {
        if (pActionId == EditorInfo.IME_ACTION_DONE) {
            mEditTextEmail.clearFocus();
            SQUIUtils.SoftInput.hide(getActivity());
            return true;
        }
        return false;
    }
    //endregion

    private void setListener(OnNewPersonListener pListener) {
        mListener = pListener;
    }
}
