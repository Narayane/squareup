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

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.models.SQDeal;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.ui.widgets.recyclerviews.SQSectionRecyclerViewAdapter;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 14/10/2016.
 */

public class SQEventDealsSectionRecyclerViewAdapter extends SQSectionRecyclerViewAdapter<DateTime, SQDeal,
        SQEventDealsSectionRecyclerViewAdapter.ViewHolder> {

    public SQEventDealsSectionRecyclerViewAdapter(Map<DateTime, List<SQDeal>> pSections) {
        super(pSections);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R.layout
                .sq_item_sectionrecyclerview_event_deals, pParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder pViewHolder, int pPosition) {
        super.onBindViewHolder(pViewHolder, pPosition);

        SQDeal vDeal = getItem(pPosition);
        pViewHolder.getTextViewPinnedHeaderLabel().setText(getSections()[getSectionIndexForPosition(pPosition)]);
        pViewHolder.mTextViewHour.setText(SQFormatUtils.formatTime(vDeal.getDate()));
        pViewHolder.mTextViewName.setText(vDeal.getTag());
        pViewHolder.mTextViewAmount.setText(SQFormatUtils.formatAmount(vDeal.getValue(), vDeal.getCurrency().getSymbol()));
    }

    @Override
    public void configureFloatingHeader(View pHeader, int pPosition) {
        AppCompatTextView vSectionHeader = (AppCompatTextView) pHeader;
        vSectionHeader.setText(getSections()[getSectionIndexForPosition(pPosition)]);
        vSectionHeader.setBackgroundColor(ContextCompat.getColor(SQApplication.getContext(), R.color
                .sq_color_grey_100));
    }

    @Override
    public String getSectionLabel(DateTime pKey) {
        return SQFormatUtils.formatLongDate(pKey);
    }

    public static class ViewHolder extends SQSectionRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.sq_item_recyclerview_event_deals_imageview_type)
        AppCompatImageView mImageView;
        @BindView(R.id.sq_item_recyclerview_event_deals_textview_hour)
        AppCompatTextView mTextViewHour;
        @BindView(R.id.sq_item_recyclerview_event_deals_textview_name)
        AppCompatTextView mTextViewName;
        @BindView(R.id.sq_item_recyclerview_event_deals_textview_amount)
        AppCompatTextView mTextViewAmount;

        public ViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }
}
