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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sebastien BALARD on 04/04/2016.
 */
public class SQSearchCurrenciesListAdapter extends RecyclerView.Adapter<SQSearchCurrenciesListAdapter.ViewHolder> {

    private List<Currency> mCurrenciesList;
    private List<Currency> mFilteredCurrenciesList;

    public SQSearchCurrenciesListAdapter() {
        super();
        mCurrenciesList = SQCurrencyUtils.getAllCurrencies();
        mFilteredCurrenciesList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R.layout
                .sq_item_search_currencies_list, pParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder pViewHolder, int pPosition) {

        Currency vCurrency = mFilteredCurrenciesList.get(pPosition);

        String vLabel = SQApplication.getContext().getString(R.string.sq_format_currency_code_symbol, vCurrency
                .getDisplayName(), vCurrency.getCurrencyCode(), vCurrency.getSymbol());
        pViewHolder.mTextViewLabel.setText(vLabel);
    }

    @Override
    public int getItemCount() {
        return mFilteredCurrenciesList == null ? 0 : mFilteredCurrenciesList.size();
    }

    public Currency getItem(int pPosition) {
        return mFilteredCurrenciesList.get(pPosition);
    }

    public void filter(String pCharSequence) {
        pCharSequence = pCharSequence.toLowerCase(Locale.getDefault());
        mFilteredCurrenciesList.clear();
        if (pCharSequence.length() == 0) {
            mFilteredCurrenciesList.addAll(mCurrenciesList);
        } else {
            for (Currency vCurrency : mCurrenciesList) {
                String vCurrencyLabel = vCurrency.getDisplayName().toLowerCase(Locale.getDefault()) + " (" +
                        vCurrency.getCurrencyCode().toLowerCase(Locale.getDefault()) + ")";
                String vContent = pCharSequence.toString().trim().toLowerCase(Locale.getDefault());
                if (vCurrencyLabel.contains(vContent)) {
                    mFilteredCurrenciesList.add(vCurrency);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewLabel;

        public ViewHolder(View pView) {
            super(pView);
            mTextViewLabel = (TextView) itemView.findViewById(R.id.sq_item_search_currencies_list_textview_label);
        }
    }
}
