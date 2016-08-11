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

import com.sebastien.balard.android.squareup.ui.activities.SQHomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Sebastien BALARD on 20/03/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SQHomeActivityTest {

    @Rule
    public ActivityTestRule<SQHomeActivity> mActivityRule = new ActivityTestRule<>(SQHomeActivity
            .class);

    @Test
    public void testDrawerMenuChecked() {
        onView(withId(R.id.sq_activity_home_layout_drawer)).perform(DrawerActions.open());
        onView(withId(R.id.sq_activity_home_layout_drawer)).check(matches(isOpen()));
        //onView(withText(R.string.sq_commons_my_events)).check(matches(isSelected()));
        onView(withText(R.string.sq_commons_my_currencies)).check(matches(not(isSelected())));
    }

    @Test
    public void testFabClicked() {
        onView(withId(R.id.sq_activity_home_nestedscrollview_empty)).check(matches(isDisplayed()));
        onView(withId(R.id.sq_activity_home_fab)).perform(click());
        onView(withId(R.id.sq_activity_home_nestedscrollview_empty)).check(matches(not(isDisplayed())));
    }
}
