package com.sebastien.balard.android.squareup.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sebastien.balard.android.squareup.R;

/**
 * Created by Sébastien BALARD on 27/02/2016.
 */
public class SQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.sq_activity_enter, R.anim.sq_activity_exit);
    }
}
