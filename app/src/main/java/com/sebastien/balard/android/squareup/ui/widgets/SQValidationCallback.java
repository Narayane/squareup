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

package com.sebastien.balard.android.squareup.ui.widgets;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.callback.SimpleCallback;

/**
 * Created by Sebastien BALARD on 21/11/2016.
 */

public class SQValidationCallback extends SimpleCallback {

    public SQValidationCallback(Context pContext) {
        super(pContext, false);
    }

    public SQValidationCallback(Context pContext, boolean pFocusFirstFail) {
        super(pContext, pFocusFirstFail);
    }

    @Override
    public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations, List<View> passedValidations) {
        if (!failedValidations.isEmpty()) {
            if (mFocusFirstFail) {
                FormValidator.ValidationFail firstFail = failedValidations.get(0);
                firstFail.view.requestFocus();
                showValidationMessage(firstFail);
            } else {
                for (FormValidator.ValidationFail vFail : failedValidations) {
                    showValidationMessage(vFail);
                }
            }
        }

        if (! passedValidations.isEmpty()) {
            showViewIsValid(passedValidations);
        }
    }

    @Override
    protected void showValidationMessage(FormValidator.ValidationFail pValidationFail) {
        if (pValidationFail.view instanceof TextInputEditText) {
            ((TextInputLayout) pValidationFail.view.getParent().getParent()).setError(pValidationFail.message);
        } else {
            //TODO: use snackbar?
            Toast.makeText(mContext, pValidationFail.message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void showViewIsValid(Collection<View> pPassedValidations) {
        for (View vView : pPassedValidations) {
            if (vView instanceof TextInputEditText) {
                ((TextInputLayout) vView.getParent().getParent()).setError(null);
                ((TextInputLayout) vView.getParent().getParent()).setErrorEnabled(false);
            }
        }
    }
}
