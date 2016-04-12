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

package com.sebastien.balard.android.squareup.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQFormatUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUserPreferencesUtils;

import org.joda.time.DateTime;

import java.sql.SQLException;

/**
 * Created by Sebastien BALARD on 14/03/2016.
 */
public class SQSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = SQSettingsFragment.class.getSimpleName();

    private ListPreference mFrequencyUpdatePreferenceList;

    private Preference mLastUpdatePreference;

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        SQLog.v("onCreate");
        addPreferencesFromResource(R.xml.sq_settings);

        mFrequencyUpdatePreferenceList = (ListPreference) getPreferenceScreen().findPreference(SQConstants.USER_PREFERENCE_CURRENCIES_UPDATE_FREQUENCY);
        mLastUpdatePreference = (Preference) getPreferenceScreen().findPreference(SQConstants.USER_PREFERENCE_CURRENCIES_LAST_UPDATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLog.v("onResume");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        refreshSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pSharedPreferences, String pKey) {
        SQLog.v("onSharedPreferenceChanged");
        refreshSettings();
    }

    private void refreshSettings() {
        String label;
        switch (SQUserPreferencesUtils.getRatesUpdateFrequency()) {
            case 1:
                label = getString(R.string.sq_commons_one_day);
                break;
            case 2:
                label = getString(R.string.sq_commons_two_days);
                break;
            case 3:
                label = getString(R.string.sq_commons_three_days);
                break;
            default:
                label = getString(R.string.sq_commons_one_week);
                break;
        }
        mFrequencyUpdatePreferenceList.setSummary(label);

        DateTime vLastUpdate = null;
        try {
            vLastUpdate = SQCurrencyUtils.getDefaultConversionBase().getLastUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mLastUpdatePreference.setSummary(SQFormatUtils.formatMediumDateAndTime(vLastUpdate));
        }
    }

}
