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

package com.sebastien.balard.android.squareup.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQSearchCurrenciesListAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.listeners.SQRecyclerViewItemTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 01/04/2016.
 */
public class SQSearchCurrencyFragment extends Fragment {

    public static final String TAG = SQSearchCurrencyFragment.class.getSimpleName();

    @Bind(R.id.sq_fragment_search_currency_editext_currency)
    protected EditText mCurrencyEditText;
    @Bind(R.id.sq_fragment_search_currency_recyclerview)
    protected RecyclerView mRecyclerView;

    private OnCurrencySelectionListener mListener;

    private List<Currency> mCurrenciesList;
    private SQSearchCurrenciesListAdapter mAdapter;

    public interface OnCurrencySelectionListener {
        void onCurrencySelected(Currency pCurrency);
    }

    public static final SQSearchCurrencyFragment newInstance(String pStartContent) {
        SQSearchCurrencyFragment vFragment = new SQSearchCurrencyFragment();
        Bundle vBundle = new Bundle(1);
        vBundle.putString("START_CONTENT", pStartContent);
        vFragment.setArguments(vBundle);
        return vFragment;
    }

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        SQLog.v("onCreate");
        try {
            mListener = (OnCurrencySelectionListener) getActivity();
        } catch (ClassCastException pException) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() + " must implement " +
                    "OnCurrencySelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater pInflater, @Nullable ViewGroup pContainer, @Nullable Bundle
            pSavedInstanceState) {
        SQLog.v("onCreateView");
        View vView = pInflater.inflate(R.layout.sq_fragment_search_currency, pContainer, false);
        ButterKnife.bind(this, vView);
        return vView;
    }

    @Override
    public void onViewCreated(View pView, @Nullable Bundle pSavedInstanceState) {
        SQLog.v("onViewCreated");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mCurrenciesList = new ArrayList<Currency>(Currency.getAvailableCurrencies());
        Collections.sort(mCurrenciesList, new Comparator<Currency>() {

            @Override
            public int compare(Currency pCurrency, Currency pCurrency2) {
                return pCurrency.getDisplayName().compareToIgnoreCase(pCurrency2.getDisplayName());
            }
        });
        mAdapter = new SQSearchCurrenciesListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(getActivity(), mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {

            @Override
            public void onClick(View pView, int pPosition) {
                SQLog.v("onClick");
                mCurrencyEditText.removeTextChangedListener(mTextWatcher);
                SQUIUtils.SoftInput.hide(getActivity());
                mListener.onCurrencySelected(mAdapter.getItem(pPosition));
                getFragmentManager().popBackStack();
            }

            @Override
            public void onLongClick(View pView, int pPosition) {
                SQLog.v("onLongClick");

            }
        }));

        mCurrencyEditText.addTextChangedListener(mTextWatcher);
        mCurrencyEditText.setText(getArguments().getString("START_CONTENT"));
        mCurrencyEditText.requestFocus();
    }

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence pCharSequence, int pStart, int pCount, int pAfter) {

        }

        @Override
        public void onTextChanged(CharSequence pCharSequence, int pStart, int pBefore, int pCount) {
            //if (pCharSequence.length() > 2) {
            mAdapter.filter(pCharSequence.toString());
                /*} else {
                    getSQActivity().onBackPressed();
                }*/
        }

        @Override
        public void afterTextChanged(Editable pEditable) {

        }
    };
}
