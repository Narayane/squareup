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

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFirebaseUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 25/09/2016.
 */

public class SQLoginActivity extends SQActivity {

    @BindView(R.id.sq_activity_login_button_google_sign_in)
    SignInButton mButtonGoogleSignIn;
    @BindView(R.id.sq_activity_login_button_facebook_login)
    LoginButton mButtonFacebookLogin;

    private CallbackManager mFacebookCallback;

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQLoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sq_activity_login);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        initToolbar();
        initButtonGoogleSignIn();
        initButtonFacebookLogin();
    }

    @Override
    public void onStart() {
        super.onStart();
        SQFirebaseUtils.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        SQFirebaseUtils.stop();
    }

    @Override
    public void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        super.onActivityResult(pRequestCode, pResultCode, pData);
        SQLog.v("onActivityResult");
        if (pRequestCode == SQConstants.NOTIFICATION_REQUEST_GOOGLE_SIGN_IN) {
            GoogleSignInResult vSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(pData);
            if (vSignInResult.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                SQFirebaseUtils.authWithGoogle(SQLoginActivity.this, vSignInResult.getSignInAccount(), pTask -> {
                    SQLog.d("signInWithCredential:onComplete:" + pTask.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!pTask.isSuccessful()) {
                        SQLog.w("signInWithCredential", pTask.getException());
                        Toast.makeText(SQLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            } else {
                // Signed out, show unauthenticated UI.
            }
        } else {
            mFacebookCallback.onActivityResult(pRequestCode, pResultCode, pData);
        }
    }

    private void initButtonFacebookLogin() {
        mFacebookCallback = CallbackManager.Factory.create();
        mButtonFacebookLogin.setReadPermissions("email", "public_profile");
        mButtonFacebookLogin.registerCallback(mFacebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult pLoginResult) {
                SQLog.v("onSuccess:" + pLoginResult);
                SQFirebaseUtils.authWithFacebook(SQLoginActivity.this, pLoginResult.getAccessToken(), pTask -> {
                    SQLog.d("signInWithCredential:onComplete:" + pTask.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!pTask.isSuccessful()) {
                        SQLog.w("signInWithCredential", pTask.getException());
                        Toast.makeText(SQLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }

            @Override
            public void onCancel() {
                SQLog.v("onCancel");
            }

            @Override
            public void onError(FacebookException pError) {
                SQLog.v("onError", pError);
            }
        });
    }

    private void initButtonGoogleSignIn() {
        mButtonGoogleSignIn.setSize(SignInButton.SIZE_STANDARD);
        mButtonGoogleSignIn.setOnClickListener(pView -> {
            Intent vSignInIntent = Auth.GoogleSignInApi.getSignInIntent(SQFirebaseUtils.signIn(this,
                    pConnectionResult -> {
                SQLog.e("google api client connection failed");
                if (pConnectionResult.hasResolution()) {
                    try {
                        SQLog.d("start resolution");
                        pConnectionResult.startResolutionForResult(SQLoginActivity.this, SQConstants
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
            }));
            startActivityForResult(vSignInIntent, SQConstants.NOTIFICATION_REQUEST_GOOGLE_SIGN_IN);
        });
    }
}
