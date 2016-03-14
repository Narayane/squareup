/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;

import java.util.List;

/**
 * Created by sbalard on 03/03/2016.
 */
public class SQCurrenciesListAdapter extends RecyclerView.Adapter<SQCurrenciesListAdapter.ViewHolder> {

    List<SQCurrency> mCurrenciesList;

    public SQCurrenciesListAdapter(List<SQCurrency> pCurrenciesList) {
        mCurrenciesList = pCurrenciesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R.layout.sq_item_currencies_list,
                pParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder pHolder, int pPosition) {
        SQCurrency vCurrency = mCurrenciesList.get(pPosition);

        pHolder.mCodeTextView.setText(vCurrency.getCode());
        String vLabel = vCurrency.getName();
        vLabel += " (";
        vLabel += vCurrency.getSymbol() + ")";
        pHolder.mLabelTextView.setText(vLabel);
        pHolder.mRateTextView.setText(SQFormatUtils.formatRate(vCurrency.getRate()));
    }

    @Override
    public int getItemCount() {
        return mCurrenciesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCodeTextView;
        TextView mLabelTextView;
        TextView mRateTextView;

        public ViewHolder(View pView) {
            super(pView);

            mCodeTextView = (TextView) pView.findViewById(R.id.sq_item_currencies_list_textview_code);
            mLabelTextView = (TextView) pView.findViewById(R.id.sq_item_currencies_list_textview_label);
            mRateTextView = (TextView) pView.findViewById(R.id.sq_item_currencies_list_textview_rate);
        }
    }
}