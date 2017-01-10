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

package com.sebastien.balard.android.squareup.ui.widgets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.utils.SQContactUtils;
import com.sebastien.balard.android.squareup.ui.widgets.SQAbstractCursorAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by s.balard on 02/03/2015.
 */
public class SQContactCursorAdapter extends SQAbstractCursorAdapter<SQContactCursorAdapter.ViewHolder> {

    private Context mContext;

    public SQContactCursorAdapter(Context pContext, int pResource, Cursor pCursor) {
        super(pContext, pResource, pCursor);
        mContext = pContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup pParent, int viewType) {
        return new ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(mItemLayout, pParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder pViewHolder, Cursor pCursor) {

        long vContactId = pCursor.getLong(pCursor.getColumnIndex(ContactsContract.Contacts._ID));

        Uri vPhotoUri = SQContactUtils.getPhotoUri(mContext, vContactId);
        if (vPhotoUri != null) {
            pViewHolder.mImageViewPicture.setImageURI(vPhotoUri);
        } else {
            pViewHolder.mImageViewPicture.setImageResource(R.drawable.sq_ic_person_24dp);
        }
        pViewHolder.mTextViewName.setText(SQContactUtils.getDisplayName(mContext, vContactId));
        pViewHolder.mTextViewEmail.setText(SQContactUtils.getEmail(mContext, vContactId));
    }

    @Override
    public CharSequence convertToString(Cursor pCursor) {
        return pCursor.getString(pCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sq_item_contacts_list_imageview_picture)
        public AppCompatImageView mImageViewPicture;
        @BindView(R.id.sq_item_contacts_list_textview_name)
        public AppCompatTextView mTextViewName;
        @BindView(R.id.sq_item_contacts_list_textview_email)
        public AppCompatTextView mTextViewEmail;

        public ViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }
}
