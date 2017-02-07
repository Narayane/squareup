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

package com.sebastien.balard.android.squareup.misc.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sebastien BALARD on 08/04/2016.
 */
public class SQUIUtils {

    public static final class SoftInput {

        public static void show(Activity pContext, View pView) {
            InputMethodManager vImm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!vImm.isAcceptingText()) {
                vImm.showSoftInput(pView, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        public static void showForced(Activity pContext) {
            InputMethodManager vImm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            vImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        public static void hide(Activity pContext, View pView) {
            InputMethodManager vImm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (vImm.isAcceptingText()) {
                vImm.hideSoftInputFromWindow(pView.getWindowToken(), 0);
            }
        }

        public static void hide(Activity pContext) {
            InputMethodManager vImm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (vImm.isAcceptingText()) {
                vImm.hideSoftInputFromWindow(pContext.getCurrentFocus().getWindowToken(), 0);
            }
        }

    }
}
