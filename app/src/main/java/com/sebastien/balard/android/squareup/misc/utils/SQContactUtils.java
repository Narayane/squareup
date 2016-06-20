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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Sebastien BALARD on 20/06/2016.
 */
public class SQContactUtils {

    public static String getDisplayName(Context pContext, long pContactId) {

        Uri vUri = ContactsContract.Contacts.CONTENT_URI;
        String[] vProjection = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
        String vSelection = ContactsContract.Contacts._ID + "=?";
        String[] vArgs = new String[] { String.valueOf(pContactId) };

        final Cursor vCursor = pContext.getContentResolver().query(vUri, vProjection, vSelection, vArgs, null);

        String vDisplayName = "-";
        if (vCursor != null && vCursor.moveToFirst()) {
            vDisplayName = vCursor.getString(vCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            vCursor.close();
        }
        return vDisplayName;

    }

    public static Uri getPhotoUri(Context pContext, long pContactId) {

        Uri vUri = ContactsContract.Contacts.CONTENT_URI;
        String[] vProjection = new String[] { ContactsContract.Contacts.PHOTO_URI };
        String vSelection = ContactsContract.Contacts._ID + "=?";
        String[] vArgs = new String[] { String.valueOf(pContactId) };

        final Cursor vCursor = pContext.getContentResolver().query(vUri, vProjection, vSelection, vArgs, null);

        String vPhotoUri = null;
        if (vCursor != null && vCursor.moveToFirst()) {
            vPhotoUri = vCursor.getString(vCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            vCursor.close();
        }

        if (vPhotoUri != null) {
            return Uri.parse(vPhotoUri);
        }
        return null;
    }

    public static String getEmail(Context pContext, long pContactId) {

        Uri vUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] vProjection = new String[] { ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY };
        String vSelection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String[] vArgs = new String[] { String.valueOf(pContactId) };
        String vOrderBy = ContactsContract.CommonDataKinds.Email.IS_PRIMARY + " DESC";

        final Cursor vCursor = pContext.getContentResolver().query(vUri, vProjection, vSelection, vArgs, vOrderBy);

        String vEmail = "-";
        if (vCursor != null && vCursor.moveToFirst()) {
            vEmail = vCursor.getString(vCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            vCursor.close();
        }
        return vEmail;
    }
}
