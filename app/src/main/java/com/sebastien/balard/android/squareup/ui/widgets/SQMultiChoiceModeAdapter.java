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

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastien BALARD on 18/03/2016.
 */
public abstract class SQMultiChoiceModeAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mItemsList;
    protected SparseBooleanArray mSelectedItems;

    protected SQMultiChoiceModeAdapter() {
        mSelectedItems = new SparseBooleanArray();
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public T getItem(int pPosition) {
        return mItemsList.get(pPosition);
    }

    public void toggleSelection(int pPosition) {
        if (mSelectedItems.get(pPosition, false)) {
            mSelectedItems.delete(pPosition);
        }
        else {
            mSelectedItems.put(pPosition, true);
        }
        notifyItemChanged(pPosition);
    }

    public void clearSelection() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount() {
        return mSelectedItems.size();
    }

    public List<Integer> getSelectedItemsPositions() {
        List<Integer> vItems = new ArrayList<Integer>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            vItems.add(mSelectedItems.keyAt(i));
        }
        return vItems;
    }
}
