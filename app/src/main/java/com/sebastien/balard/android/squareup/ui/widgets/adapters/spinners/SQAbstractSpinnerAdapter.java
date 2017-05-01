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

package com.sebastien.balard.android.squareup.ui.widgets.adapters.spinners;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastien BALARD on 02/02/2017.
 */

public abstract class SQAbstractSpinnerAdapter<T extends SQAbstractSpinnerAdapter.SQAbstractSpinnerAdapterItem> extends
        BaseAdapter implements android.widget.SpinnerAdapter {

    public interface SQAbstractSpinnerAdapterItem {
        Long getItemId();
        String getItemLabel();
    }

    protected Context mContext;

    protected List<T> mListItems;

    public SQAbstractSpinnerAdapter(Context pContext, List<T> pElements) {
        mContext = pContext;
        mListItems = new ArrayList<>();
        mListItems.addAll(pElements);
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
        return mListItems.get(pPosition).getItemId();
    }
}
