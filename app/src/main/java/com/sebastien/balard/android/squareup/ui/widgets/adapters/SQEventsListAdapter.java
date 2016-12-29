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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.widgets.SQMultiChoiceModeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 10/04/2016.
 */
public class SQEventsListAdapter extends SQMultiChoiceModeAdapter<SQEvent, SQEventsListAdapter
        .EventsListItemViewHolder> {

    public static class EventsListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sq_item_events_list_layout_clickable)
        LinearLayout mLayoutClickable;
        @BindView(R.id.sq_item_events_list_textview_name)
        AppCompatTextView mTextViewName;
        @BindView(R.id.sq_item_events_list_textview_start_date)
        AppCompatTextView mTextViewStartDate;
        @BindView(R.id.sq_item_events_list_textview_end_date)
        AppCompatTextView mTextViewEndDate;
        @BindView(R.id.sq_item_events_list_textview_participants_count)
        AppCompatTextView mTextViewParticipantsCount;
        @BindView(R.id.sq_item_events_list_textview_amout)
        AppCompatTextView mTextViewAmount;
        @BindView(R.id.sq_item_events_list_button_edit)
        AppCompatButton mButtonEdit;
        @BindView(R.id.sq_item_events_list_button_more)
        AppCompatImageButton mImageButtonMore;

        public EventsListItemViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }

    public interface OnEventActionListener {
        SQActivity getActivity();
        void onOpen(Long pEventId);
        void onEdit(Long pEventId);
        void onDuplicate(Long pEventId);
        void onShare(Long pEventId);
        void onDelete(Long pEventId);
    }

    private OnEventActionListener mListener;

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
    public void onBindViewHolder(final EventsListItemViewHolder pViewHolder, int pPosition) {
        SQEvent vEvent = mItemsList.get(pPosition);

        pViewHolder.mLayoutClickable.setOnClickListener(pView -> mListener.onOpen(vEvent.getId()));
        pViewHolder.mTextViewName.setText(vEvent.getName());
        if (vEvent.getEndDate().toLocalDate().equals(vEvent.getStartDate().toLocalDate())) {
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
        pViewHolder.mTextViewParticipantsCount.setText(SQApplication.getContext().getResources().getQuantityString
                (R.plurals.sq_commons_participants_count, vEvent.getParticipants().size(), vEvent.getParticipants()
                        .size()));
        pViewHolder.mTextViewAmount.setText(SQFormatUtils.formatAmount(0f, vEvent.getCurrency().getSymbol()));
        pViewHolder.mButtonEdit.setOnClickListener(pView1 -> {
            SQLog.i("click on button: edit event");
            mListener.onEdit(vEvent.getId());
        });
        pViewHolder.mImageButtonMore.setOnClickListener(pView -> {
            SQLog.i("click on button: more action");
            PopupMenu vPopupMenu = new PopupMenu(mListener.getActivity(), pViewHolder.mImageButtonMore, Gravity.END);
            vPopupMenu.getMenuInflater().inflate(R.menu.sq_menu_contextual_event, vPopupMenu.getMenu());
            vPopupMenu.setOnMenuItemClickListener(pMenuItem -> {
                switch (pMenuItem.getItemId()) {
                    case R.id.sq_menu_contextual_event_item_duplicate:
                        SQLog.i("click on button: duplicate event");
                        mListener.onDuplicate(vEvent.getId());
                        return true;
                    case R.id.sq_menu_contextual_event_item_share:
                        SQLog.i("click on button: share event");
                        /*SQDialogUtils.createSnackBarWarning(mListener.getActivity().getToolbar(), mListener
                                .getActivity().getString(R.string.sq_message_warning_not_yet_implemented), Snackbar
                                .LENGTH_LONG).show();*/
                        return true;
                    case R.id.sq_menu_contextual_event_item_delete:
                        SQLog.i("click on button: delete event");
                        mListener.onDelete(vEvent.getId());
                        return true;
                    default:
                        return false;
                }
            });
            vPopupMenu.show();
        });
    }

    public void setOnEventActionListener(OnEventActionListener pListener) {
        mListener = pListener;
    }
}
