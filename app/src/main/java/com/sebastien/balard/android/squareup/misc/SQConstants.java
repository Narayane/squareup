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
    String TABLE_CURRENCY_COLUMN_ID = "currency_id";
    String TABLE_CURRENCY_COLUMN_CODE = "currency_code";
    String TABLE_CURRENCY_COLUMN_IS_BASE = "is_reference";

    String TABLE_CONVERSION_BASE_NAME = "sq_conversion_bases";
    String TABLE_CONVERSION_BASE_COLUMN_ID = "id";
    String TABLE_CONVERSION_BASE_COLUMN_LAST_UPDATE = "last_update";
    String TABLE_CONVERSION_BASE_COLUMN_CODE = "code";
    String TABLE_CONVERSION_BASE_COLUMN_RATES = "rates";
    String TABLE_CONVERSION_BASE_COLUMN_IS_DEFAULT = "is_default";

    String TABLE_EVENT_NAME = "sq_events";
    String TABLE_EVENT_COLUMN_ID = "event_id";
    String TABLE_EVENT_COLUMN_NAME = "event_name";
    String TABLE_EVENT_COLUMN_START_DATE = "event_start_date";
    String TABLE_EVENT_COLUMN_END_DATE = "event_end_date";
    String TABLE_EVENT_COLUMN_FK_CURRENCY_ID = "fk_currency_id";

    String TABLE_PERSON_NAME = "sq_persons";
    String TABLE_PERSON_COLUMN_ID = "person_id";
    String TABLE_PERSON_COLUMN_NAME = "name";
    String TABLE_PERSON_COLUMN_EMAIL = "email";
    String TABLE_PERSON_COLUMN_WEIGHT = "weight";
    String TABLE_PERSON_COLUMN_FK_CONTACT_ID = "fk_contact_id";
    String TABLE_PERSON_COLUMN_FK_EVENT_ID = "fk_event_id";

    String ACTION_UPDATE_CURRENCIES_RATES = "ACTION_UPDATE_CURRENCIES_RATES";

    String USER_PREFERENCE_CURRENCIES_UPDATE_FREQUENCY = "preference_currency_update_frequency";
    String USER_PREFERENCE_CURRENCIES_LAST_UPDATE = "preference_currency_last_update_date";

    int NOTIFICATION_REQUEST_CREATE_EVENT = 10000;
    int NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS = 10001;
    int NOTIFICATION_REQUEST_EDIT_EVENT = 10002;

    String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
}
