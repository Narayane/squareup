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

package com.sebastien.balard.android.squareup.ui.widgets.adapters.spinners;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.sebastien.balard.android.squareup.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 22/01/2017.
 */

public class SQImageSpinnerAdapter<T extends SQImageSpinnerAdapter.SQImageSpinnerAdapterItem> extends
        SQAbstractSpinnerAdapter<T> {

    public interface SQImageSpinnerAdapterItem extends SQAbstractSpinnerAdapterItem {
        String getItemImageStringUri();
        TextDrawable getItemImageDrawable();
    }

    public SQImageSpinnerAdapter(Context pContext, List<T> pElements) {
        super(pContext, pElements);
    }

    @NonNull
    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pViewGroup) {

        ViewHolder vViewHolder;
        if (pConvertView != null) {
            vViewHolder = (ViewHolder) pConvertView.getTag();
        } else {
            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pConvertView = layout.inflate(R.layout.sq_item_image_spinner, null);
            vViewHolder = new ViewHolder(pConvertView);
            pConvertView.setTag(vViewHolder);
        }

        String vPhotoUriString = mListItems.get(pPosition).getItemImageStringUri();
        if (vPhotoUriString != null) {
            vViewHolder.mImageView.setImageURI(Uri.parse(vPhotoUriString));
        } else {
            vViewHolder.mImageView.setImageDrawable(mListItems.get(pPosition).getItemImageDrawable());
        }
        vViewHolder.mTextViewLabel.setText(mListItems.get(pPosition).getItemLabel());

        return pConvertView;
    }

    @Override
    public View getDropDownView(int pPosition, View pConvertView, ViewGroup pViewGroup) {

        ViewHolder vViewHolder;
        if (pConvertView != null) {
            vViewHolder = (ViewHolder) pConvertView.getTag();
        } else {
            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pConvertView = layout.inflate(R.layout.sq_item_image_spinner, null);
            vViewHolder = new ViewHolder(pConvertView);
            pConvertView.setTag(vViewHolder);
        }

        String vPhotoUriString = mListItems.get(pPosition).getItemImageStringUri();
        if (vPhotoUriString != null) {
            vViewHolder.mImageView.setImageURI(Uri.parse(vPhotoUriString));
        } else {
            vViewHolder.mImageView.setImageDrawable(mListItems.get(pPosition).getItemImageDrawable());
        }
        vViewHolder.mTextViewLabel.setText(mListItems.get(pPosition).getItemLabel());

        return pConvertView;
    }

    static class ViewHolder {

        @BindView(R.id.sq_item_image_spinner_imageview)
        AppCompatImageView mImageView;
        @BindView(R.id.sq_item_image_spinner_textview_label)
        AppCompatTextView mTextViewLabel;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}
