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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;

/**
 * Created by Sebastien BALARD on 25/09/2016.
 */

public class SQFirebaseUtils {

    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseUser mFirebaseUser;

    public static void start() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = pFirebaseAuth -> {
                SQLog.v("onAuthStateChanged");
                mFirebaseUser = pFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // user is signed in
                    SQLog.d("authenticated as: " + mFirebaseUser.getDisplayName());
                    //FIXME: put a listener
                } else {
                    // user is signed out
                    SQLog.d("not authenticated");
                }
            };
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static void stop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public static FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void authenticateByGoogle(SQActivity pActivity, String pToken,
                                            OnCompleteListener<AuthResult> pOnCompleteListener) {
        SQLog.d("authenticate by Google with token: " + pToken);

        AuthCredential vCredential = GoogleAuthProvider.getCredential(pToken, null);
        if (mFirebaseUser != null && mFirebaseUser.getProviders().size() > 1) {
            SQLog.v("link with in Firebase");
            mFirebaseUser.linkWithCredential(vCredential).addOnCompleteListener(pActivity, pOnCompleteListener);
        } else {
            SQLog.v("sign in Firebase");
            mAuth.signInWithCredential(vCredential).addOnCompleteListener(pActivity, pOnCompleteListener);
        }
    }

    public static void authenticateByGoogle(String pEmail, String pToken, OnSuccessListener<AuthResult>
            pSuccessListener, OnFailureListener pFailureListener) {
        SQLog.d("authenticate by Google with token: " + pToken);

        AuthCredential vCredential = GoogleAuthProvider.getCredential(pToken, null);
        if (mFirebaseUser != null && mFirebaseUser.getProviders().size() > 1) {
            SQLog.v("link with in Firebase");
            mFirebaseUser.linkWithCredential(vCredential).addOnSuccessListener(pSuccessListener).addOnFailureListener
                    (pException -> {
                //FIXME: improve
                if (pException.getMessage().equals("User has already been linked to the given provider.")) {
                    mAuth.signInWithCredential(vCredential).addOnSuccessListener(pSuccessListener)
                            .addOnFailureListener(pFailureListener);
                } else {
                    pFailureListener.onFailure(pException);
                }
            });
        } else {
            SQLog.v("sign in Firebase");
            mAuth.signInWithCredential(vCredential).addOnSuccessListener(pSuccessListener).addOnFailureListener
                    (pFailureListener);
        }
    }

    public static void authenticateByFacebook(SQActivity pActivity, String pToken,
                                              OnCompleteListener<AuthResult> pOnCompleteListener) {
        SQLog.d("authenticateByFacebook: " + pToken);

        AuthCredential vCredential = FacebookAuthProvider.getCredential(pToken);
        if (mFirebaseUser != null && mFirebaseUser.getProviders().size() > 1) {
            SQLog.v("link with in Firebase");
            mFirebaseUser.linkWithCredential(vCredential).addOnCompleteListener(pActivity, pOnCompleteListener);
        } else {
            SQLog.v("sign in Firebase");
            mAuth.signInWithCredential(vCredential).addOnCompleteListener(pActivity, pOnCompleteListener);
        }
    }

    public static void authenticateByFacebook(String pToken, OnSuccessListener<AuthResult> pSuccessListener,
                                              OnFailureListener pFailureListener) {
        SQLog.d("authenticateByFacebook: " + pToken);

        AuthCredential vCredential = FacebookAuthProvider.getCredential(pToken);
        if (mFirebaseUser != null && mFirebaseUser.getProviders().size() > 1) {
            SQLog.v("link with in Firebase");
            mFirebaseUser.linkWithCredential(vCredential).addOnSuccessListener(pSuccessListener).addOnFailureListener
                    (pException -> {
                //FIXME: improve
                if (pException.getMessage().equals("User has already been linked to the given provider.")) {
                    mAuth.signInWithCredential(vCredential).addOnSuccessListener(pSuccessListener)
                            .addOnFailureListener(pFailureListener);
                } else {
                    pFailureListener.onFailure(pException);
                }
            });
        } else {
            SQLog.v("sign in Firebase");
            mAuth.signInWithCredential(vCredential).addOnSuccessListener(pSuccessListener).addOnFailureListener
                    (pFailureListener);
        }
    }

    public static void updateEmail(String pEmail, OnCompleteListener<Void> pOnCompleteListener) {
        mFirebaseUser.updateEmail(pEmail).addOnCompleteListener(pOnCompleteListener);
    }

    public static void updateProfile(UserProfileChangeRequest pRequest, OnCompleteListener<Void> pOnCompleteListener) {
        mFirebaseUser.updateProfile(pRequest).addOnCompleteListener(pOnCompleteListener);
    }
}
