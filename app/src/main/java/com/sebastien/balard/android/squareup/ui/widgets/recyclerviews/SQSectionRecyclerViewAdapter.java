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

package com.sebastien.balard.android.squareup.ui.widgets.recyclerviews;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sebastien.balard.android.squareup.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 19/08/2016.
 */

public abstract class SQSectionRecyclerViewAdapter<V, T, VH extends SQSectionRecyclerViewAdapter.ViewHolder> extends
        RecyclerView
        .Adapter<VH> {

    public static final int HEADER_GONE = 0;

    public static final int HEADER_VISIBLE = 1;

    public static final int HEADER_PUSHED_UP = 2;

    public int getHeaderState(int pPosition) {
        if (pPosition < 0 || getItemCount() == 0) {
            return HEADER_GONE;
        }

        int vSection = getSectionIndexForPosition(pPosition);
        int vNextSectionPosition = getPositionForSectionIndex(vSection + 1);
        if (vNextSectionPosition != -1 && pPosition == vNextSectionPosition - 1) {
            return HEADER_PUSHED_UP;
        }

        return HEADER_VISIBLE;
    }

    protected Map<V, List<T>> mSections;

    public SQSectionRecyclerViewAdapter(Map<V, List<T>> pSections) {
        super();
        mSections = pSections;
    }

    public abstract void configureFloatingHeader(View pHeader, int pPosition);

    public abstract String getSectionLabel(V pKey);

    public int getPositionForSectionIndex(int pSectionIndex) {
        if (pSectionIndex < 0) {
            pSectionIndex = 0;
        }
        if (pSectionIndex >= mSections.size()) {
            pSectionIndex = mSections.size() - 1;
        }
        int vElementIndex = 0;
        int vSectionIndex = 0;
        for (Map.Entry<V, List<T>> vSection : mSections.entrySet()) {
            if (pSectionIndex == vSectionIndex) {
                return vElementIndex;
            }
            vElementIndex += vSection.getValue().size();
            vSectionIndex++;
        }
        return 0;
    }

    public int getSectionIndexForPosition(int pPosition) {
        int vElementIndex = 0;
        int vSectionIndex = 0;
        for (Map.Entry<V, List<T>> vSection : mSections.entrySet()) {
            if (pPosition >= vElementIndex && pPosition < vElementIndex + vSection.getValue().size()) {
                return vSectionIndex;
            }
            vElementIndex += vSection.getValue().size();
            vSectionIndex++;
        }
        return -1;
    }

    public String[] getSections() {
        String[] res = new String[mSections.size()];
        int vIndex = 0;
        for (Map.Entry<V, List<T>> vSection : mSections.entrySet()) {
            res[vIndex] = getSectionLabel(vSection.getKey());
            vIndex++;
        }
        return res;
    }

    public T getItem(int pPosition) {
        int vIndex = 0;
        for (Map.Entry<V, List<T>> vSection : mSections.entrySet()) {
            if (pPosition >= vIndex && pPosition < vIndex + vSection.getValue().size()) {
                return vSection.getValue().get(pPosition - vIndex);
            }
            vIndex += vSection.getValue().size();
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sq_widget_sectionrecyclerview_textview_pinned_header_label)
        AppCompatTextView mTextViewPinnedHeaderLabel;

        public ViewHolder(View pView) {
            super(pView);
            ButterKnife.bind(this, pView);
        }

        public AppCompatTextView getTextViewPinnedHeaderLabel() {
            return mTextViewPinnedHeaderLabel;
        }
    }

    @Override
    public int getItemCount() {
        int vCount = 0;
        for (Map.Entry<V, List<T>> vSection : mSections.entrySet()) {
            vCount += vSection.getValue().size();
        }
        return vCount;
    }

    @Override
    public void onBindViewHolder(VH pViewHolder, int pPosition) {
        final int vSectionIndex = getSectionIndexForPosition(pPosition);
        boolean vDisplaySectionHeaders = (getPositionForSectionIndex(vSectionIndex) == pPosition);

        if (vDisplaySectionHeaders) {
            pViewHolder.mTextViewPinnedHeaderLabel.setVisibility(View.VISIBLE);
            pViewHolder.mTextViewPinnedHeaderLabel.setText(getSections()[getSectionIndexForPosition(pPosition)]);
        } else {
            pViewHolder.mTextViewPinnedHeaderLabel.setVisibility(View.GONE);
        }
    }
}
