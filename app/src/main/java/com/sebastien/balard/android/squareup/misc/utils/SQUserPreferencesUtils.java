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

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.misc.SQConstants;

/**
 * Created by Sebastien BALARD on 14/03/2016.
 */
public class SQUserPreferencesUtils {

    public static int getRatesUpdateFrequency() {
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.PREFERENCE_CURRENCIES_UPDATE_FREQUENCY, "7"));
    }

    public static boolean isUserConnected() {
        return PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getBoolean
                (SQConstants.PREFERENCE_USER_IS_LOGGED, false);
    }

    public static String getUserDisplayName() {
        return PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.PREFERENCE_USER_DISPLAY_NAME, null);
    }

    public static String getUserEmail() {
        return PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.PREFERENCE_USER_EMAIL, null);
    }

    public static Uri getUserPhotoUri() {
        String vUrl = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.PREFERENCE_USER_PHOTO_URL, null);
        if (vUrl != null) {
            return Uri.parse(vUrl);
        }
        return null;
    }

    public static String getSocialProvider() {
        return PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext()).getString
                (SQConstants.PREFERENCE_SOCIAL_PROVIDER, null);
    }

    public static void setUserConnected() {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putBoolean(SQConstants.PREFERENCE_USER_IS_LOGGED, true);
        vEditor.apply();
    }

    public static void setSocialProvider(String pProviderName) {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putString(SQConstants.PREFERENCE_SOCIAL_PROVIDER, pProviderName);
        vEditor.apply();
    }

    public static void setUserDisplayName(String pDisplayName) {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putString(SQConstants.PREFERENCE_USER_DISPLAY_NAME, pDisplayName);
        vEditor.apply();
    }

    public static void setUserEmail(String pEmail) {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putString(SQConstants.PREFERENCE_USER_EMAIL, pEmail);
        vEditor.apply();
    }

    public static void setUserPhotoUri(Uri pPhotoUri) {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putString(SQConstants.PREFERENCE_USER_PHOTO_URL, pPhotoUri.toString());
        vEditor.apply();
    }

    /*public static void clearUserProfile() {
        SharedPreferences.Editor vEditor = PreferenceManager.getDefaultSharedPreferences(SQApplication.getContext())
                .edit();
        vEditor.putBoolean(SQConstants.PREFERENCE_USER_IS_LOGGED, false);
        vEditor.putString(SQConstants.PREFERENCE_USER_DISPLAY_NAME, null);
        vEditor.putString(SQConstants.PREFERENCE_USER_EMAIL, null);
        vEditor.putString(SQConstants.PREFERENCE_USER_PHOTO_URL, null);
        vEditor.apply();
    }*/
}
