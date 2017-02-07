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

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.models.SQDebt;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 23/01/2017.
 */

public class SQDealDebtsListAdapter extends RecyclerView.Adapter<SQDealDebtsListAdapter.ViewHolder> {

    public interface OnDebtActionListener {
        SQActivity getActivity();
        void onToggle();
    }

    private OnDebtActionListener mListener;

    private List<SQDebt> mListDebts;

    public SQDealDebtsListAdapter(List<SQDebt> pListDebts) {
        super();
        mListDebts = pListDebts;
    }

    @Override
    public SQDealDebtsListAdapter.ViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new SQDealDebtsListAdapter.ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R
                .layout.sq_item_deal_debts_list, pParent, false));
    }

    @Override
    public void onBindViewHolder(SQDealDebtsListAdapter.ViewHolder pViewHolder, int pPosition) {

        SQDebt vDebt = mListDebts.get(pPosition);
        pViewHolder.mCheckBox.setChecked(vDebt.isActive());
        pViewHolder.mCheckBox.setOnClickListener(pView -> {
            vDebt.setActive(!vDebt.isActive());
            mListener.onToggle();
        });
        String vPhotoUriString = vDebt.getRecipient().getPhotoUriString();
        if (vPhotoUriString != null) {
            pViewHolder.mImageViewPicture.setImageURI(Uri.parse(vPhotoUriString));
        } else {
            TextDrawable vPlaceholder = TextDrawable.builder().buildRound(Character.toString(vDebt.getRecipient()
                    .getName().charAt(0)), ContextCompat.getColor(SQApplication.getContext(), R.color.sq_color_primary_dark));
            pViewHolder.mImageViewPicture.setImageDrawable(vPlaceholder);
        }
        pViewHolder.mTextViewName.setText(vDebt.getRecipient().getName());
        pViewHolder.mEditTextValue.setText(SQFormatUtils.formatAmount(vDebt.getValue()));
        pViewHolder.mEditTextValue.setEnabled(vDebt.isActive());
    }

    @Override
    public int getItemCount() {
        return mListDebts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sq_item_deal_debts_list_checkbox)
        AppCompatCheckBox mCheckBox;
        @BindView(R.id.sq_item_deal_debts_list_imageview_picture)
        AppCompatImageView mImageViewPicture;
        @BindView(R.id.sq_item_deal_debts_list_textview_name)
        AppCompatTextView mTextViewName;
        @BindView(R.id.sq_item_deal_debts_list_edittext_value)
        AppCompatEditText mEditTextValue;

        public ViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }

    public void setOnDebtActionListener(OnDebtActionListener pListener) {
        mListener = pListener;
    }
}
