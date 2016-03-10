/*
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
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

package com.sebastien.balard.android.squareup.io.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by Sebastien BALARD on 08/03/2016.
 */
public class SQTimestampConverter implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement pJsonElement, Type pTypeOfT, JsonDeserializationContext
            pDeserializationContext) throws JsonParseException {
        return new DateTime(pJsonElement.getAsString(), DateTimeZone.UTC);
    }

    @Override
    public JsonElement serialize(DateTime pDateTime, Type pTypeOfT, JsonSerializationContext pSerializationContext) {
        return new JsonPrimitive(ISODateTimeFormat.dateTimeNoMillis().print(pDateTime));
    }
}
