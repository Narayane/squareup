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

package com.sebastien.balard.android.squareup;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sebastien.balard.android.squareup.ui.activities.SQCurrenciesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Sebastien BALARD on 20/03/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SQCurrenciesListActivityTest {

    @Rule
    public ActivityTestRule<SQCurrenciesListActivity> mActivityRule = new ActivityTestRule<>(SQCurrenciesListActivity
            .class);

    @Test
    public void testDrawerMenuChecked() {
        onView(withId(R.id.sq_activity_currencies_list_layout_drawer)).perform(DrawerActions.open());
        onView(withId(R.id.sq_activity_currencies_list_layout_drawer)).check(matches(isOpen()));
        /*onData(withId(R.id.sq_activity_currencies_list_navigation_view)).onChildView(withText(R.string
                .sq_commons_my_currencies)).check(matches(isSelected()));
        onData(withId(R.id.sq_activity_currencies_list_navigation_view)).inAdapterView(allOf(withText(R.string
                .sq_commons_my_currencies))).check(matches(isSelected()));
        onData(allOf(withText(R.string.sq_commons_my_currencies), is(instanceOf(AppCompatCheckedTextView.class))))
                .inAdapterView(withId(R.id.sq_activity_currencies_list_navigation_view)).check(matches(isSelected()));
        onData(allOf(withText(R.string.sq_commons_my_currencies), isDescendantOfA(withId(R.id
                .sq_activity_currencies_list_navigation_view)))).check(matches(isSelected()));*/
    }
}
