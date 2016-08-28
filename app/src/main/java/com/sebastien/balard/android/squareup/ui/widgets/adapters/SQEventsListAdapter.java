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
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.SQApplication;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQEditEventActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQHomeActivity;
import com.sebastien.balard.android.squareup.ui.widgets.SQMultiChoiceModeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 10/04/2016.
 */
public class SQEventsListAdapter extends SQMultiChoiceModeAdapter<SQEvent, SQEventsListAdapter
        .EventsListItemViewHolder> {

    private SQActivity mActivity;

    public SQEventsListAdapter(SQActivity pActivity, List<SQEvent> pEventList) {
        super();
        mItemsList = pEventList;
        mActivity = pActivity;
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
        pViewHolder.mTextViewParticipantsCount.setText(SQApplication.getContext().getResources().getQuantityString
                (R.plurals.sq_commons_participants_count, vEvent.getParticipants().size(), vEvent.getParticipants()
                        .size()));
        pViewHolder.mTextViewAmount.setText(SQFormatUtils.formatAmount(0f, vEvent.getCurrency().getSymbol()));
        pViewHolder.mButtonEdit.setOnClickListener(pView1 -> {
            SQLog.i("click on button: edit event");
            mActivity.startActivityForResult(SQEditEventActivity.getIntentToEdit(mActivity, vEvent.getId()),
                    SQConstants.NOTIFICATION_REQUEST_EDIT_EVENT);
        });
        pViewHolder.mImageButtonMore.setOnClickListener(pView -> {
            SQLog.i("click on button: more action");
            PopupMenu vPopupMenu = new PopupMenu(mActivity, pViewHolder.mImageButtonMore, Gravity.END);
            vPopupMenu.getMenuInflater().inflate(R.menu.sq_menu_contextual_event, vPopupMenu.getMenu());
            vPopupMenu.setOnMenuItemClickListener(pMenuItem -> {
                switch (pMenuItem.getItemId()) {
                    case R.id.sq_menu_contextual_event_item_duplicate:
                        SQLog.i("click on button: duplicate event");
                        return true;
                    case R.id.sq_menu_contextual_event_item_share:
                        SQLog.i("click on button: share event");
                        SQDialogUtils.createSnackBarWarning(mActivity.getToolbar(), mActivity.getString(R.string
                                .sq_message_warning_not_yet_implemented), Snackbar.LENGTH_LONG).show();
                        return true;
                    case R.id.sq_menu_contextual_event_item_delete:
                        SQLog.i("click on button: delete event");
                        SQDialogUtils.showDialogYesNo(mActivity, R.string.sq_dialog_title_warning, R
                                .string.sq_dialog_message_delete_event, android.R.string.ok, android.R.string.cancel,
                                (pDialogInterface, pWhich) -> {
                                    ((SQHomeActivity) mActivity).deleteEvent(vEvent);
                            pDialogInterface.dismiss();
                        }, (pDialogInterface, pWhich) -> pDialogInterface.dismiss());
                        return true;
                    default:
                        return false;
                }
            });
            vPopupMenu.show();
        });
    }

    public static class EventsListItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sq_item_events_list_textview_name)
        AppCompatTextView mTextViewName;
        @Bind(R.id.sq_item_events_list_textview_start_date)
        AppCompatTextView mTextViewStartDate;
        @Bind(R.id.sq_item_events_list_textview_end_date)
        AppCompatTextView mTextViewEndDate;
        @Bind(R.id.sq_item_events_list_textview_participants_count)
        AppCompatTextView mTextViewParticipantsCount;
        @Bind(R.id.sq_item_events_list_textview_amout)
        AppCompatTextView mTextViewAmount;
        @Bind(R.id.sq_item_events_list_button_edit)
        AppCompatButton mButtonEdit;
        @Bind(R.id.sq_item_events_list_button_more)
        AppCompatImageButton mImageButtonMore;

        public EventsListItemViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }
    }
}
