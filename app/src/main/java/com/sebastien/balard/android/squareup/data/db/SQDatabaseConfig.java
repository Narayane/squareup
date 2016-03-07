package com.sebastien.balard.android.squareup.data.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.sebastien.balard.android.squareup.data.models.SQCurrency;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by sbalard on 06/03/2016.
 */
public class SQDatabaseConfig extends OrmLiteConfigUtil {

    public static void main(String[] pArgs) {
        try {
            final Class[] vClasses = {SQCurrency.class};
            writeConfigFile("sq_config_ormlite.txt", vClasses);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
