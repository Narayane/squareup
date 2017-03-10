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

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.sebastien.balard.android.squareup.R;

/**
 * Created by Sebastien BALARD on 19/08/2016.
 */

public class SQSectionRecyclerView extends RecyclerView {

    private SQSectionRecyclerViewAdapter mAdapter;

    private View mHeaderView;
    private boolean mIsHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public SQSectionRecyclerView(Context pContext) {
        super(pContext);
    }

    public SQSectionRecyclerView(Context pContext, @Nullable AttributeSet pAttrs) {
        super(pContext, pAttrs);
    }

    public SQSectionRecyclerView(Context pContext, @Nullable AttributeSet pAttrs, int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
    }

    @Override
    protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
        super.onMeasure(pWidthMeasureSpec, pHeightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, pWidthMeasureSpec, pHeightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean pChanged, int pLeft, int pTop, int pRight, int pBottom) {
        super.onLayout(pChanged, pLeft, pTop, pRight, pBottom);

        if (mHeaderView != null) {
            setFadingEdgeLength(0);
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition());
        }
    }

    protected void configureHeaderView(int pPosition) {

        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout
                    .sq_widget_sectionrecyclerview_pinned_header,
                    this, false);
        }

        int state = mAdapter.getHeaderState(pPosition);
        switch (state) {
            case SQSectionRecyclerViewAdapter.HEADER_GONE: {
                mIsHeaderViewVisible = false;
                break;
            }
            case SQSectionRecyclerViewAdapter.HEADER_VISIBLE: {
                mAdapter.configureFloatingHeader(mHeaderView, pPosition);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mIsHeaderViewVisible = true;
                break;
            }
            case SQSectionRecyclerViewAdapter.HEADER_PUSHED_UP: {
                View vFirstView = getChildAt(0);
                if (vFirstView != null) {
                    int vBottom = vFirstView.getBottom();
                    int mHeaderViewHeight = mHeaderView.getHeight();
                    int vY;
                    if (vBottom < mHeaderViewHeight) {
                        vY = (vBottom - mHeaderViewHeight);
                    } else {
                        vY = 0;
                    }
                    mAdapter.configureFloatingHeader(mHeaderView, pPosition);
                    if (mHeaderView.getTop() != vY) {
                        mHeaderView.layout(0, vY, mHeaderViewWidth, this.mHeaderViewHeight + vY);
                    }
                    mIsHeaderViewVisible = true;
                }
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas pCanvas) {
        super.dispatchDraw(pCanvas);
        if (mIsHeaderViewVisible) {
            drawChild(pCanvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public void setAdapter(Adapter pAdapter) {
        if (!(pAdapter instanceof SQSectionRecyclerViewAdapter)) {
            throw new IllegalArgumentException(SQSectionRecyclerView.class.getSimpleName() + " must use an adapter " +
                    "of type " + SQSectionRecyclerViewAdapter.class.getSimpleName());
        }

        if (this.mAdapter != null) {
            this.clearOnScrollListeners();
        }

        this.mAdapter = (SQSectionRecyclerViewAdapter) pAdapter;
        this.addOnScrollListener(new SQRecyclerViewOnScrollListener());

        super.setAdapter(pAdapter);
    }

    @Override
    public SQSectionRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }
}
