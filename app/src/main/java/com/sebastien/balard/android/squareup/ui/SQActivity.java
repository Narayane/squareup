/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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

package com.sebastien.balard.android.squareup.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.activities.SQAboutActivity;
import com.sebastien.balard.android.squareup.ui.activities.SQSettingsActivity;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQActivity extends AppCompatActivity {

    @BindView(R.id.sq_widget_appbar_toolbar)
    protected Toolbar mToolbar;

    protected CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        overridePendingTransition(0, 0);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.sq_activity_enter, R.anim.sq_activity_exit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case android.R.id.home:
                SQUIUtils.SoftInput.hide(this);
                onBackPressed();
                return true;
            case R.id.sq_menu_default_item_settings:
                startActivity(SQSettingsActivity.getIntent(this));
                return true;
            case R.id.sq_menu_default_item_about:
                startActivity(SQAboutActivity.getIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(pMenuItem);
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
        super.onDestroy();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
