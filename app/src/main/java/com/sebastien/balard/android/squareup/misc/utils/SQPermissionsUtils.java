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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

/**
 * Created by Sebastien BALARD on 26/08/2016.
 */
public class SQPermissionsUtils {

    public static Intent getIntentForApplicationSettings(Context pContext) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pContext
                .getPackageName())).addCategory(Intent.CATEGORY_DEFAULT).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     *
     * @param pContext
     * @param pPermission
     * @return
     */
    public static boolean hasPermission(Context pContext, String pPermission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(pContext,
                pPermission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     * @param pContext
     * @param pPermissions
     * @return
     */
    public static boolean hasPermissions(Context pContext, String[] pPermissions) {
        boolean vResult = true;
        for (String vPermission : pPermissions) {
            vResult &= hasPermission(pContext, vPermission);
        }
        return vResult;
    }

    /**
     *
     * @param pContext
     * @param pPermission
     * @param pRequestCode
     */
    public static void requestPermission(Activity pContext, String pPermission, int pRequestCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermission(pContext, pPermission)) {
                pContext.requestPermissions(new String[]{pPermission}, pRequestCode);
            }
        }
    }

    /**
     *
     * @param pContext
     * @param pPermissions
     * @param pRequestCode
     */
    public static void requestPermissions(Activity pContext, String[] pPermissions, int pRequestCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(pContext, pPermissions)) {
                pContext.requestPermissions(pPermissions, pRequestCode);
            }
        }
    }
}
