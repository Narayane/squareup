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

package com.sebastien.balard.android.squareup.ui.widgets.chips;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastien BALARD on 06/05/2016.
 */
public class SQChipsVerticalLinearLayout extends LinearLayout {

    private List<LinearLayout> mLineLayouts = new ArrayList<>();

    private float mDensity;
    private int mRowSpacing;

    public SQChipsVerticalLinearLayout(Context context, int rowSpacing) {
        super(context);

        mDensity = getResources().getDisplayMetrics().density;
        mRowSpacing = rowSpacing;

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public TextLineParams onChipsChanged(List<SQChipsView.SQChip> chips) {
        clearChipsViews();

        int width = getWidth();
        if (width == 0) {
            return null;
        }
        int widthSum = 0;
        int rowCounter = 0;

        LinearLayout ll = createHorizontalView();

        for (SQChipsView.SQChip chip : chips) {
            View view = chip.getView();
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            // if width exceed current width. create a new LinearLayout
            if (widthSum + view.getMeasuredWidth() > width) {
                rowCounter++;
                widthSum = 0;
                ll = createHorizontalView();
            }

            widthSum += view.getMeasuredWidth();
            ll.addView(view);
        }

        // check if there is enough space left
        if (width - widthSum < width * 0.1f) {
            widthSum = 0;
            rowCounter++;
        }
        return new TextLineParams(rowCounter, widthSum);
    }

    private LinearLayout createHorizontalView() {
        LinearLayout ll = new LinearLayout(getContext());
        ll.setPadding(0, 0, 0, mRowSpacing);
        ll.setOrientation(HORIZONTAL);
        addView(ll);
        mLineLayouts.add(ll);
        return ll;
    }

    private void clearChipsViews() {
        for (LinearLayout linearLayout : mLineLayouts) {
            linearLayout.removeAllViews();
        }
        mLineLayouts.clear();
        removeAllViews();
    }

    public static class TextLineParams {
        public int row;
        public int lineMargin;

        public TextLineParams(int row, int lineMargin) {
            this.row = row;
            this.lineMargin = lineMargin;
        }
    }
}
