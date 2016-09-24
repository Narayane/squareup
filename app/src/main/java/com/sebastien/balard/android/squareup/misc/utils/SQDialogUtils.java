/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.sebastien.balard.android.squareup.misc.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;

/**
 * Created by Sebastien BALARD on 07/03/2016.
 */
public class SQDialogUtils {

    public static Snackbar createSnackBarWithAction(Context pContext, View pAnchorView, int pDuration, int
            pBackgroundColor, int pTextColor, int pMessage, int pActionName, int pActionColor, View.OnClickListener pActionListener) {
        Snackbar vSnackbar = Snackbar.make(pAnchorView, pMessage, pDuration).setAction(pActionName, pActionListener)
                .setActionTextColor(pActionColor);
        vSnackbar.getView().setBackgroundColor(ContextCompat.getColor(pContext, pBackgroundColor));
        TextView textView = (TextView) vSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);
        textView.setTextColor(pTextColor);
        return vSnackbar;
    }

    public static Snackbar createSnackBarError(View pAnchorView, String pMessage, int pDuration) {
        Snackbar vSnackbar = Snackbar.make(pAnchorView, pMessage, pDuration);
        vSnackbar.getView().setBackgroundColor(ContextCompat.getColor(SQApplication.getContext(), R.color
                .sq_color_red_500));

        return vSnackbar;
    }

    public static Snackbar createSnackBarWarning(View pAnchorView, String pMessage, int pDuration) {
        Snackbar vSnackbar = Snackbar.make(pAnchorView, pMessage, pDuration);
        vSnackbar.getView().setBackgroundColor(ContextCompat.getColor(SQApplication.getContext(), R.color
                .sq_color_orange_500));

        return vSnackbar;
    }

    public static Snackbar createSnackBarSuccess(View pAnchorView, String pMessage, int pDuration) {
        Snackbar vSnackbar = Snackbar.make(pAnchorView, pMessage, pDuration);
        vSnackbar.getView().setBackgroundColor(ContextCompat.getColor(SQApplication.getContext(), R.color
                .sq_color_green_500));

        return vSnackbar;
    }

    public static AlertDialog showDialogYesNo(Context pContext, int pTitle, int pMessage, int pYesButtonLabel, int
            pNoButtonLabel, DialogInterface.OnClickListener pYesClickListener, DialogInterface.OnClickListener
                                                      pNoClickListener) {

        AlertDialog vDialog = new AlertDialog.Builder(pContext).setTitle(pTitle).setMessage(pMessage)
                .setPositiveButton(pYesButtonLabel, pYesClickListener).setNegativeButton(pNoButtonLabel,
                        pNoClickListener).create();

        //setText(pContext, vDialog);
        vDialog.show();
        return vDialog;
    }

    public static AlertDialog createDialogWithCustomView(final Activity pContext, int pTitle, int pView, Integer
            pYesButtonLabel, Integer pNoButtonLabel, DialogInterface.OnClickListener pYesClickListener,
                                                         DialogInterface.OnClickListener pNoClickListener, boolean
                                                                 pIsCancelable) {

        View vView = pContext.getLayoutInflater().inflate(pView, null);
        //float dpi = pContext.getResources().getDisplayMetrics().density;
        //int margin = Float.valueOf(24 * dpi).intValue();
        AlertDialog.Builder vBuilder = new AlertDialog.Builder(pContext).setTitle(pTitle).setView(vView/*, margin,
                margin, margin, margin*/);
        if (pYesButtonLabel != null) {
            vBuilder.setPositiveButton(pYesButtonLabel, pYesClickListener);
        }
        if (pNoButtonLabel != null) {
            vBuilder.setNegativeButton(pNoButtonLabel, pNoClickListener);
        }

        AlertDialog vDialog = vBuilder.create();
        vDialog.setCancelable(pIsCancelable);
        vDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        //showView(pContext, vDialog);
        vDialog.show();

        return vDialog;
    }

    private static void showView(final Context pContext, AlertDialog pAlertDialog) {
        pAlertDialog.setOnShowListener(pDialogInterface -> {

            Dialog vDialog = ((Dialog) pDialogInterface);

            /*View divider = vDialog.findViewById(vDialog.getContext().getResources().getIdentifier
                    ("android:id/titleDivider", null, null));
            divider.setBackgroundColor(ContextCompat.getColor(pContext, R.color.colorPrimaryDark));*/

            TextView pTitleView = (TextView) vDialog.findViewById(vDialog.getContext().getResources()
                    .getIdentifier("alertTitle", "id", "android"));
            pTitleView.setTextColor(ContextCompat.getColor(pContext, R.color.sq_color_primary));
        });
        pAlertDialog.show();
    }
}
