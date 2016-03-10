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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sebastien BALARD on 08/03/2016.
 */
public class SQMapConverter implements JsonDeserializer<Map<String, Float>>, JsonSerializer<Map<String, Float>> {

    @Override
    public Map<String, Float> deserialize(JsonElement pJsonElement, Type pTypeOfT, JsonDeserializationContext
            pDeserializationContext) throws JsonParseException {
        Map<String, Float> vMap = new HashMap<String, Float>();
        for (Map.Entry<String, JsonElement> vEntry : pJsonElement.getAsJsonObject().entrySet()) {
            vMap.put(vEntry.getKey(), vEntry.getValue().getAsFloat());
        }
        return vMap;
    }

    @Override
    public JsonElement serialize(Map<String, Float> pMap, Type pTypeOfSrc, JsonSerializationContext
            pSerializationContext) {
        JsonObject vJsonObject = new JsonObject();
        for (Map.Entry<String, Float> vEntry : pMap.entrySet()) {
            vJsonObject.add(vEntry.getKey(), new JsonPrimitive(vEntry.getValue()));
        }
        return vJsonObject;
    }
}
