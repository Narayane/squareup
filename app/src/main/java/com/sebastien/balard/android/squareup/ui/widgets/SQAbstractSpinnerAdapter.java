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

package com.sebastien.balard.android.squareup.ui.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.BaseAdapter;

import com.sebastien.balard.android.squareup.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 02/02/2017.
 */

public abstract class SQAbstractSpinnerAdapter<T> extends BaseAdapter implements android.widget.SpinnerAdapter {

    public interface SQSpinnerAdapterItemAdapter<T> {
        Long getId(T pItem);
        String getStringUri(T pItem);
        String getLabel(T pItem);
    }

    protected Context mContext;

    protected List<T> mListItems;

    protected SQAbstractSpinnerAdapter.SQSpinnerAdapterItemAdapter<T> mItemAdapter;

    public SQAbstractSpinnerAdapter(Context pContext, List<T> pElements, SQAbstractSpinnerAdapter.SQSpinnerAdapterItemAdapter<T> pItemAdapter) {
        mContext = pContext;
        mListItems = new ArrayList<>();
        mListItems.addAll(pElements);
        mItemAdapter = pItemAdapter;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public T getItem(int pPosition) {
        return mListItems.get(pPosition);
    }

    @Override
    public long getItemId(int pPosition) {
        return mItemAdapter.getId(mListItems.get(pPosition));
    }
}
