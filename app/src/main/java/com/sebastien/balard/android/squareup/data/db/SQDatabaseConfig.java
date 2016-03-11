package com.sebastien.balard.android.squareup.data.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;
import com.sebastien.balard.android.squareup.data.models.SQConversionBase;
import com.sebastien.balard.android.squareup.misc.SQLog;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Square up android app
 * Copyright (C) 2016  Sebastien BALARD
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQDatabaseConfig extends OrmLiteConfigUtil {

    public static void main(String[] pArgs) {
        try {
            final Class[] vClasses = {SQCurrency.class, SQConversionBase.class};
            writeConfigFile("sq_config_ormlite.txt", vClasses);
        } catch (IOException pException) {
            SQLog.e("fail to write in ormlite config file");
        } catch (SQLException pException) {
            SQLog.e("fail to get model classes");
        }
    }
}
