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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Status;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFirebaseUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQGoogleSignInUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUserPreferencesUtils;
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

    private ProgressDialog mProgressDialog;
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
            SQGoogleSignInUtils.processSignInResultIntent(pData, new SQGoogleSignInUtils.OnSignInListener() {
                @Override
                public void onSuccess(GoogleSignInAccount pSignInAccount) {
                    mProgressDialog = ProgressDialog.show(SQLoginActivity.this, "Please wait", "Sign in in " +
                            "progress...", true);
                    /*SQFirebaseUtils.authenticateByGoogle(SQLoginActivity.this, pSignInAccount.getIdToken(), pTask -> {
                        if (!pTask.isSuccessful()) {
                            SQLog.e("fail to authenticate in Firebase", pTask.getException());
                            SQGoogleSignInUtils.signOut();
                            SQDialogUtils.createSnackBarWarning(mToolbar, getString(R.string
                                    .sq_message_warning_sign_in_with_multiple_google_accounts), Snackbar.LENGTH_LONG).show();
                        } else {
                            SQLog.d("succeed in authenticate in Firebase");
                            SQUserPreferencesUtils.setSocialProvider("Google");
                            SQUserPreferencesUtils.setUserConnected();
                            SQUserPreferencesUtils.setUserDisplayName(pSignInAccount.getDisplayName());
                            SQUserPreferencesUtils.setUserEmail(pSignInAccount.getEmail());
                            SQUserPreferencesUtils.setUserPhotoUri(pSignInAccount.getPhotoUrl());
                            SQGoogleSignInUtils.signOut();
                            setResult(RESULT_OK);
                            finish();
                        }
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    });*/
                    SQFirebaseUtils.authenticateByGoogle(pSignInAccount.getEmail(), pSignInAccount.getIdToken(),
                            pAuthResult -> {
                        SQLog.d("succeed in authenticate in Firebase");
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        SQUserPreferencesUtils.setSocialProvider("Google");
                        SQUserPreferencesUtils.setUserConnected();
                        SQUserPreferencesUtils.setUserDisplayName(pSignInAccount.getDisplayName());
                        SQUserPreferencesUtils.setUserEmail(pSignInAccount.getEmail());
                        SQUserPreferencesUtils.setUserPhotoUri(pSignInAccount.getPhotoUrl());
                        SQGoogleSignInUtils.signOut();
                        setResult(RESULT_OK);
                        finish();
                    }, pException -> {
                        SQLog.e("fail to authenticate in Firebase", pException);
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        SQGoogleSignInUtils.signOut();
                        SQDialogUtils.createSnackBarWarning(mToolbar, getString(R.string
                                .sq_message_warning_sign_in_with_multiple_google_accounts), Snackbar.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onError(Status pStatus) {
                    SQLog.e("fail to authenticate in Google: " + pStatus);
                }
            });
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
                mProgressDialog = ProgressDialog.show(SQLoginActivity.this, "Please wait", "Sign in in " +
                        "progress...", true);
                /*SQFirebaseUtils.authenticateByFacebook(SQLoginActivity.this, pLoginResult.getAccessToken().getToken
                        (), pTask -> {
                    SQLog.d("authenticateByFacebook: " + pTask.isSuccessful());
                    if (!pTask.isSuccessful()) {
                        SQLog.w("authenticateByFacebook", pTask.getException());
                        Toast.makeText(SQLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        AccessToken.setCurrentAccessToken(null);
                        LoginManager.getInstance().logOut();
                    } else {
                        SQUserPreferencesUtils.setSocialProvider("Facebook");
                        SQUserPreferencesUtils.setUserConnected();
                        Profile vProfile = Profile.getCurrentProfile();
                        SQUserPreferencesUtils.setUserDisplayName(vProfile.getFirstName() + " " + vProfile
                                .getLastName());
                        SQUserPreferencesUtils.setUserPhotoUri(vProfile.getProfilePictureUri(300, 300));

                        Bundle vBundle = new Bundle();
                        vBundle.putString("fields", "email");
                        GraphRequest vGraphRequest = GraphRequest.newMeRequest(pLoginResult.getAccessToken(),
                                (pJSONObject, pGraphResponse) -> {
                            if (pGraphResponse.getError() != null) {
                                SQLog.e("fail to get Facebook email", pGraphResponse.getError().getException());
                            } else {
                                String vEmail = pJSONObject.optString("email");
                                SQLog.d("succeed in get Facebook email: " + vEmail);
                                SQUserPreferencesUtils.setUserEmail(pJSONObject.optString("email"));
                            }
                            AccessToken.setCurrentAccessToken(null);
                            LoginManager.getInstance().logOut();
                            setResult(RESULT_OK);
                            finish();
                        });
                        vGraphRequest.setParameters(vBundle);
                        vGraphRequest.executeAsync();
                    }
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                });*/
                SQFirebaseUtils.authenticateByFacebook(pLoginResult.getAccessToken().getToken(), pAuthResult -> {
                    SQLog.d("succeed in authenticate in Firebase");
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    SQUserPreferencesUtils.setSocialProvider("Facebook");
                    SQUserPreferencesUtils.setUserConnected();
                    Profile vProfile = Profile.getCurrentProfile();
                    SQUserPreferencesUtils.setUserDisplayName(vProfile.getFirstName() + " " + vProfile.getLastName());
                    SQUserPreferencesUtils.setUserPhotoUri(vProfile.getProfilePictureUri(300, 300));

                    Bundle vBundle = new Bundle();
                    vBundle.putString("fields", "email");
                    GraphRequest vGraphRequest = GraphRequest.newMeRequest(pLoginResult.getAccessToken(),
                            (pJSONObject, pGraphResponse) -> {
                        if (pGraphResponse.getError() != null) {
                            SQLog.e("fail to get Facebook email", pGraphResponse.getError().getException());
                        } else {
                            String vEmail = pJSONObject.optString("email");
                            SQLog.d("succeed in get Facebook email: " + vEmail);
                            SQUserPreferencesUtils.setUserEmail(pJSONObject.optString("email"));
                        }
                        AccessToken.setCurrentAccessToken(null);
                        LoginManager.getInstance().logOut();
                        setResult(RESULT_OK);
                        finish();
                    });
                    vGraphRequest.setParameters(vBundle);
                    vGraphRequest.executeAsync();
                }, pException -> {
                    SQLog.e("fail to authenticate in Firebase", pException);
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(SQLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    AccessToken.setCurrentAccessToken(null);
                    LoginManager.getInstance().logOut();
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
            startActivityForResult(SQGoogleSignInUtils.getSignInIntent(this), SQConstants
                    .NOTIFICATION_REQUEST_GOOGLE_SIGN_IN);
        });
    }
}
