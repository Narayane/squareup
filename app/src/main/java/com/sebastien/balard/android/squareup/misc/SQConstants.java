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

    String TABLE_DEAL_NAME = "sq_deals";
    String TABLE_DEAL_COLUMN_ID = "deal_id";
    String TABLE_DEAL_COLUMN_TYPE = "deal_type";
    String TABLE_DEAL_COLUMN_TAG = "deal_tag";
    String TABLE_DEAL_COLUMN_DATE = "deal_date";
    String TABLE_DEAL_COLUMN_VALUE = "deal_value";
    String TABLE_DEAL_COLUMN_CURRENCY_RATE = "deal_currency_rate";
    String TABLE_DEAL_COLUMN_CONVERSION_BASE_CODE = "deal_conversion_base_code";
    String TABLE_DEAL_COLUMN_LATITUDE = "deal_latitude";
    String TABLE_DEAL_COLUMN_LONGITUDE = "deal_longitude";
    String TABLE_DEAL_COLUMN_FK_CURRENCY_ID = "fk_currency_id";
    String TABLE_DEAL_COLUMN_FK_OWNER_ID = "fk_owner_id";
    String TABLE_DEAL_COLUMN_FK_EVENT_ID = "fk_event_id";

    String TABLE_DEBT_NAME = "sq_debts";
    String TABLE_DEBT_COLUMN_ID = "debt_id";
    String TABLE_DEBT_COLUMN_IS_ACTIVE = "is_active";
    String TABLE_DEBT_COLUMN_VALUE = "debt_value";
    String TABLE_DEBT_COLUMN_CURRENCY_RATE = "debt_currency_rate";
    String TABLE_DEBT_COLUMN_FK_RECIPIENT_ID = "fk_recipient_id";
    String TABLE_DEBT_COLUMN_FK_CURRENCY_ID = "fk_currency_id";
    String TABLE_DEBT_COLUMN_FK_DEAL_ID = "fk_deal_id";

    String ACTION_UPDATE_CURRENCIES_RATES = "ACTION_UPDATE_CURRENCIES_RATES";

    String PREFERENCE_CURRENCIES_UPDATE_FREQUENCY = "preference_currency_update_frequency";
    String PREFERENCE_CURRENCIES_LAST_UPDATE = "preference_currency_last_update_date";
    String PREFERENCE_USER_IS_LOGGED = "preference_user_is_logged";
    String PREFERENCE_USER_DISPLAY_NAME = "preference_user_display_name";
    String PREFERENCE_USER_EMAIL = "preference_user_email";
    String PREFERENCE_USER_PHOTO_URL = "preference_user_photo_url";
    String PREFERENCE_SOCIAL_PROVIDER = "preference_social_provider";

    int NOTIFICATION_REQUEST_CREATE_EVENT = 10000;
    int NOTIFICATION_REQUEST_PERMISSION_READ_CONTACTS = 10001;
    int NOTIFICATION_REQUEST_EDIT_EVENT = 10002;
    int NOTIFICATION_REQUEST_GOOGLE_SIGN_IN = 10003;
    int NOTIFICATION_REQUEST_LOGIN = 10004;
    int NOTIFICATION_REQUEST_LOGIN_TO_CREATE_EVENT = 10005;
    int NOTIFICATION_REQUEST_CREATE_DEAL = 10006;

    String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
    String EXTRA_ARRAY_PERSON_IDS = "EXTRA_ARRAY_PERSON_IDS";
    String EXTRA_EVENT_OWNER_ID = "EXTRA_EVENT_OWNER_ID";
}
