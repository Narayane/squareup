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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.models.SQDeal;
import com.sebastien.balard.android.squareup.data.models.SQEvent;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.ui.SQFragment;
import com.sebastien.balard.android.squareup.ui.activities.SQEventActivity;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQEventDealsSectionRecyclerViewAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.listeners.SQRecyclerViewItemTouchListener;
import com.sebastien.balard.android.squareup.ui.widgets.recyclerviews.SQSectionRecyclerView;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 13/02/2017.
 */

public class SQEventDealsListFragment extends SQFragment {

    public static final String TAG = SQEventSynthesisFragment.class.getSimpleName();

    @BindView(R.id.sq_fragment_event_deals_list_textview_empty)
    protected AppCompatTextView mTextViewEmpty;
    @BindView(R.id.sq_fragment_event_deals_list_recyclerview)
    protected SQSectionRecyclerView mRecyclerView;

    private SQEventDealsSectionRecyclerViewAdapter mSectionAdapter;
    private Map<DateTime, List<SQDeal>> mSections;
    private List<SQDeal> mListDeals;

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
        View vView = pInflater.inflate(R.layout.sq_fragment_event_deals_list, pContainer, false);
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

        SQDeal vDeal = new SQDeal("Essence");
        try {
            vDeal.setCurrency(SQCurrencyUtils.getBaseCurrency());
        } catch (SQLException pE) {
            pE.printStackTrace();
        }
        vDeal.setValue(100.95f);
        vDeal.setDate(DateTime.now().minusMinutes(35));
        mListDeals.add(vDeal);

        vDeal = new SQDeal("Bijou");
        try {
            vDeal.setCurrency(SQCurrencyUtils.getBaseCurrency());
        } catch (SQLException pE) {
            pE.printStackTrace();
        }
        vDeal.setValue(51.25f);
        mListDeals.add(vDeal);

        vDeal = new SQDeal("Bijou");
        try {
            vDeal.setCurrency(SQCurrencyUtils.getBaseCurrency());
        } catch (SQLException pE) {
            pE.printStackTrace();
        }
        vDeal.setValue(51.25f);
        vDeal.setDate(DateTime.now().minusHours(50));
        mListDeals.add(vDeal);

        vDeal = new SQDeal("Kdo");
        try {
            vDeal.setCurrency(SQCurrencyUtils.getBaseCurrency());
        } catch (SQLException pE) {
            pE.printStackTrace();
        }
        vDeal.setValue(188.5f);
        vDeal.setDate(DateTime.now().minusDays(10));
        mListDeals.add(vDeal);

        vDeal = new SQDeal("Bouffe");
        try {
            vDeal.setCurrency(SQCurrencyUtils.getBaseCurrency());
        } catch (SQLException pE) {
            pE.printStackTrace();
        }
        vDeal.setValue(14.5f);
        vDeal.setDate(DateTime.now().minusDays(3));
        mListDeals.add(vDeal);

        if (getEvent() != null) {
            //if (getEvent().getParticipants().size() > 0) {
                mTextViewEmpty.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSections.clear();
                mSections.putAll(new TreeMap<>(generateSections(mListDeals)).descendingMap());
                mSectionAdapter.notifyDataSetChanged();
            /*} else {
                mTextViewEmpty.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }*/
        } else {
            mTextViewEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private Map<DateTime, List<SQDeal>> generateSections(List<SQDeal> pListDeals) {
        Map<DateTime, List<SQDeal>> vSections = new HashMap<>();
        List<SQDeal> vList;
        for (SQDeal vDeal : pListDeals) {
            boolean vFound = false;
            for (Map.Entry<DateTime, List<SQDeal>> vEntry : vSections.entrySet()) {
                if (vEntry.getKey().toLocalDate().equals(vDeal.getDate().toLocalDate())) {
                    vEntry.getValue().add(vDeal);
                    vFound = true;
                    break;
                }
            }
            if (!vFound) {
                vList = new ArrayList<>();
                vList.add(vDeal);
                vSections.put(vDeal.getDate(), vList);
            }
        }
        return vSections;
    }

    private void initRecyclerView() {
        mListDeals = new ArrayList<>();
        mSections = new LinkedHashMap<>();
        mSectionAdapter = new SQEventDealsSectionRecyclerViewAdapter(mSections);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mSectionAdapter);
        mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(getActivity(), mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {
                    @Override
                    public void onClick(View pView, int pPosition) {
                        SQLog.v("onClick");
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
