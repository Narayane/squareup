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

package com.sebastien.balard.android.squareup.ui.widgets.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sebastien BALARD on 17/03/2016.
 */
public class SQRecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemTouchListener {

        void onClick(View pView, int pPosition);

        void onLongClick(View pView, int pPosition);
		
		boolean isEnabled(int pPosition);
    }

    protected GestureDetector mGestureDetector;

    protected OnItemTouchListener mItemTouchListener;

    public SQRecyclerViewItemTouchListener(Context pContext, final RecyclerView pRecyclerView, final
            OnItemTouchListener pItemTouchListener) {

        mItemTouchListener = pItemTouchListener;
        mGestureDetector = new GestureDetector(pContext, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent pMotionEvent) {
                View vView = pRecyclerView.findChildViewUnder(pMotionEvent.getX(), pMotionEvent.getY());
                if (vView != null && mItemTouchListener != null) {
                    mItemTouchListener.onClick(vView, pRecyclerView.getChildAdapterPosition(vView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent pMotionEvent) {
                View vView = pRecyclerView.findChildViewUnder(pMotionEvent.getX(), pMotionEvent.getY());
                if (vView != null && mItemTouchListener != null) {
                    mItemTouchListener.onLongClick(vView, pRecyclerView.getChildAdapterPosition(vView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView pRecyclerView, MotionEvent pMotionEvent) {
        View vView = pRecyclerView.findChildViewUnder(pMotionEvent.getX(), pMotionEvent.getY());
        if (!mItemTouchListener.isEnabled(pRecyclerView.getChildAdapterPosition(vView))) {
            onRequestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            mGestureDetector.onTouchEvent(pMotionEvent);
            return false;
        }
    }

    @Override
    public void onTouchEvent(RecyclerView pRecyclerView, MotionEvent pMotionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
