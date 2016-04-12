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

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.ui.widgets.SQMultiChoiceModeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 10/04/2016.
 */
public class SQEventsListAdapter extends SQMultiChoiceModeAdapter<SQEvent, SQEventsListAdapter
        .EventsListItemViewHolder> {

    public SQEventsListAdapter(List<SQEvent> pEventList) {
        super();
        mItemsList = pEventList;
    }

    @Override
    public EventsListItemViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new EventsListItemViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R.layout
                .sq_item_events_list, pParent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(EventsListItemViewHolder pViewHolder, int pPosition) {
        SQEvent vEvent = mItemsList.get(pPosition);

        pViewHolder.mTextViewName.setText(vEvent.getName());
        if (vEvent.getEndDate().equals(vEvent.getStartDate())) {
            pViewHolder.mTextViewStartDate.setText(SQApplication.getContext().getString(R.string.sq_commons_the,
                    SQFormatUtils.formatLongDate(vEvent.getStartDate())));
            pViewHolder.mTextViewEndDate.setVisibility(View.GONE);
        } else {
            pViewHolder.mTextViewStartDate.setText(SQApplication.getContext().getString(R.string.sq_commons_from,
                    SQFormatUtils.formatLongDate(vEvent.getStartDate())));
            pViewHolder.mTextViewEndDate.setVisibility(View.VISIBLE);
            pViewHolder.mTextViewEndDate.setText(SQApplication.getContext().getString(R.string.sq_commons_to,
                    SQFormatUtils.formatLongDate(vEvent.getEndDate())));
        }
        pViewHolder.mTextViewAmount.setText(SQFormatUtils.formatAmount(1000f, vEvent.getCurrency().getSymbol()));
    }

    public static class EventsListItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sq_item_events_list_textview_name)
        TextView mTextViewName;
        @Bind(R.id.sq_item_events_list_textview_start_date)
        TextView mTextViewStartDate;
        @Bind(R.id.sq_item_events_list_textview_end_date)
        TextView mTextViewEndDate;
        @Bind(R.id.sq_item_events_list_textview_amout)
        TextView mTextViewAmount;

        public EventsListItemViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }
}
