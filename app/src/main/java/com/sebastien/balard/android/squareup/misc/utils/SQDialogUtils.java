/*
 * *
 *  * Square up android app
 *  * Copyright (C) 2016  Sebastien BALARD
 *  *
 *  * This program is free software; you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation; either version 2 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License along
 *  * with this program; if not, write to the Free Software Foundation, Inc.,
 *  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 */

package com.sebastien.balard.android.squareup.misc.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;

/**
 * Created by Sebastien BALARD on 07/03/2016.
 */
public class SQDialogUtils {

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
}
