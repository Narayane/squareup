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

package com.sebastien.balard.android.squareup.misc;

/**
 * Created by sbalard on 06/03/2016.
 */
public interface SQConstants {

    String TABLE_CURRENCY_NAME = "sq_currencies";
    String TABLE_CURRENCY_COLUMN_NAME_ID = "currency_id";
    String TABLE_CURRENCY_COLUMN_NAME_CODE = "currency_code";
    String TABLE_CURRENCY_COLUMN_NAME_IS_BASE = "is_reference";

    String TABLE_CONVERSION_BASE_NAME = "sq_conversion_bases";
    String TABLE_CONVERSION_BASE_COLUMN_NAME_ID = "id";
    String TABLE_CONVERSION_BASE_COLUMN_NAME_LAST_UPDATE = "last_update";
    String TABLE_CONVERSION_BASE_COLUMN_NAME_CODE = "code";
    String TABLE_CONVERSION_BASE_COLUMN_NAME_RATES = "rates";
    String TABLE_CONVERSION_BASE_COLUMN_NAME_IS_DEFAULT = "is_default";

    String TABLE_EVENT_NAME = "sq_events";
    String TABLE_EVENT_COLUMN_NAME_ID = "event_id";
    String TABLE_EVENT_COLUMN_NAME_NAME = "event_name";
    String TABLE_EVENT_COLUMN_NAME_START_DATE = "event_start_date";
    String TABLE_EVENT_COLUMN_NAME_END_DATE = "event_end_date";
    String TABLE_EVENT_COLUMN_NAME_FK_CURRENCY = "fk_currency_id";

    String ACTION_UPDATE_CURRENCIES_RATES = "ACTION_UPDATE_CURRENCIES_RATES";

    String USER_PREFERENCE_CURRENCIES_UPDATE_FREQUENCY = "preference_currency_update_frequency";
    String USER_PREFERENCE_CURRENCIES_LAST_UPDATE = "preference_currency_last_update_date";

    int REQUEST_NEW_EVENT = 10000;

    String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
}
