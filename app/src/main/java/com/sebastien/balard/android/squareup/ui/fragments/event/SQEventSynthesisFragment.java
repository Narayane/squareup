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

package com.sebastien.balard.android.squareup.ui.fragments.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQFragment;
import com.sebastien.balard.android.squareup.ui.activities.SQEditDealActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQEventActivity;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQEventSynthesisAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.listeners.SQRecyclerViewItemTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 09/01/2017.
 */

public class SQEventSynthesisFragment extends SQFragment {

    public static final String TAG = SQEventSynthesisFragment.class.getSimpleName();

    @BindView(R.id.sq_fragment_event_synthesis_textview_empty)
    protected AppCompatTextView mTextViewEmpty;
    @BindView(R.id.sq_fragment_event_synthesis_recyclerview)
    protected RecyclerView mRecyclerView;

    private SQEventSynthesisAdapter mAdapterPersons;
    private List<SQPerson> mListPersons;

    //region fragment lifecycle methods
    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        SQLog.v("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater pInflater, @Nullable ViewGroup pContainer, @Nullable Bundle
            pSavedInstanceState) {
        SQLog.v("onCreateView");
        View vView = pInflater.inflate(R.layout.sq_fragment_event_synthesis, pContainer, false);
        ButterKnife.bind(this, vView);
        return vView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SQLog.v("onViewCreated");
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLog.v("onResume");

        refreshLayout();
    }
    //endregion

    //region public methods
    public SQEvent getEvent() {
        return ((SQEventActivity) getActivity()).getEvent();
    }
    //endregion

    //region private methods
    private void refreshLayout() {
        if (getEvent() != null) {
            if (getEvent().getParticipants().size() > 0) {
                mTextViewEmpty.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mListPersons.clear();
                mListPersons.addAll(getEvent().getParticipants());
            } else {
                mTextViewEmpty.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            mTextViewEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        mListPersons = new ArrayList<>();
        mAdapterPersons = new SQEventSynthesisAdapter(mListPersons);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterPersons);
        mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(getActivity(), mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {
                    @Override
                    public void onClick(View pView, int pPosition) {
                        SQLog.v("onClick");
                        startActivity(SQEditDealActivity.getIntentToCreate(getActivity(), getEvent().getId(),
                                getEvent().getParticipants().get(pPosition).getId()));
                    }

                    @Override
                    public void onLongClick(View pView, int pPosition) {
                        SQLog.v("onLongClick");
                    }

                    @Override
                    public boolean isEnabled(int pPosition) {
                        return true;
                    }
                }));
    }
    //endregion
}
