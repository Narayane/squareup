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

import android.content.IntentSender;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;

/**
 * Created by Sebastien BALARD on 09/10/2016.
 */

public class SQGoogleSignInUtils {

    private static GoogleApiClient mGoogleApiClient;

    public static GoogleApiClient getApiClient(SQActivity pActivity) {
        if (mGoogleApiClient == null) {
            SQLog.v("create google api client");
            GoogleSignInOptions vGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions
                    .DEFAULT_SIGN_IN).requestIdToken(pActivity.getString(R.string.default_web_client_id))
                    .requestProfile().requestEmail().requestScopes(new Scope(Scopes.PLUS_LOGIN)).build();

            mGoogleApiClient = new GoogleApiClient.Builder(pActivity).enableAutoManage(pActivity, pConnectionResult -> {
                SQLog.e("google api client connection failed");
                if (pConnectionResult.hasResolution()) {
                    try {
                        SQLog.d("start resolution");
                        pConnectionResult.startResolutionForResult(pActivity, SQConstants
                                .NOTIFICATION_REQUEST_GOOGLE_SIGN_IN);
                    } catch (IntentSender.SendIntentException pSendIntentException) {
                        SQLog.e("could not resolve connection result", pSendIntentException);
                        //mGoogleApiClient.connect();
                    }
                } else {
                    SQLog.d("could not resolve connection result");
                    // Could not resolve the connection result, show the user an error dialog.
                    //processError(mContext, pConnectionResult);
                }
            }).addApi(Auth.GOOGLE_SIGN_IN_API, vGoogleSignInOptions).build();
        }
        return mGoogleApiClient;
    }

    public static void connect(SQActivity pActivity) {
        SQLog.v("connect");
        getApiClient(pActivity).connect();
    }

    public static void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            SQLog.v("disconnect");
            mGoogleApiClient.disconnect();
        }
    }

    public static void signOut() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(pStatus -> {
                if (pStatus.isSuccess()) {
                    SQLog.d("google sign out succeeded");
                    mGoogleApiClient.disconnect();
                } else {
                    SQLog.w("google sign out failed");
                }
            });
        }
    }
}
