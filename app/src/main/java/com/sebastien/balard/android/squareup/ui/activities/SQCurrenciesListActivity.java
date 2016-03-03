package com.sebastien.balard.android.squareup.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.ui.SQActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SQCurrenciesListActivity extends SQActivity {

    @Bind(R.id.sq_activity_currencies_list_layout_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.sq_activity_currencies_list_navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.sq_widget_app_bar_imageview)
    ImageView mAppBarImageView;
    @Bind(R.id.sq_widget_app_bar_toolbar)
    Toolbar mToolbar;

    private SearchView mSearchView;
    private SimpleCursorAdapter mSearchViewCursorAdapter;
    private List<Currency> mAllCurrencies;
    private List<Currency> mShownCurrencies;

    public final static Intent getIntent(Context pContext) {
        return new Intent(pContext, SQCurrenciesListActivity.class)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP)*/;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sq_activity_currencies_list);
        ButterKnife.bind(this);
        SQLog.v("onCreate");

        setSupportActionBar(mToolbar);

        //mAppBarImageView.setImageResource(R.drawable.sq_img_currency);

        ActionBarDrawerToggle vDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .sq_action_open_drawer, R.string.sq_action_close_drawer);
        mDrawerLayout.addDrawerListener(vDrawerToggle);
        vDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem pMenuItem) {
                switch (pMenuItem.getItemId()) {
                    case R.id.sq_menu_drawer_item_event:
                        SQLog.i("click on drawer menu item: event");
                        startActivity(SQHomeActivity.getIntent(SQCurrenciesListActivity.this));
                        break;
                    case R.id.sq_menu_drawer_item_currency:
                        SQLog.i("click on drawer menu item: currency");
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mNavigationView.setCheckedItem(R.id.sq_menu_drawer_item_currency);

        Set<Currency> vSet = Currency.getAvailableCurrencies();
        SQLog.d("android currencies count: " + vSet.size());
        mAllCurrencies = new ArrayList<Currency>();
        mAllCurrencies.addAll(vSet);
        Collections.sort(mAllCurrencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency pFirst, Currency pSecond) {
                return pFirst.getDisplayName(Locale.getDefault()).compareTo(pSecond.getDisplayName(Locale.getDefault
                        ()));
            }
        });
        mSearchViewCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, new
                String[]{"label"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mShownCurrencies = new ArrayList<Currency>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        getMenuInflater().inflate(R.menu.sq_menu_search, pMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pMenu) {
        final MenuItem vMenuItem = pMenu.findItem(R.id.sq_menu_search_item_search);
        mSearchView = (SearchView) vMenuItem.getActionView();
        mSearchView.setQueryHint("Activate a currency");
        mSearchView.setSuggestionsAdapter(mSearchViewCursorAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pNewText) {
                if (pNewText.length() > 1) {
                    filter(pNewText);
                    return true;
                }
                return false;
            }
        });
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int pPosition) {
                SQLog.i("select suggestion: " + pPosition);
                return false;
            }

            @Override
            public boolean onSuggestionClick(int pPosition) {
                SQLog.i("click on suggestion: " + pPosition);
                Snackbar.make(mSearchView, mShownCurrencies.get(pPosition).getDisplayName(Locale.getDefault()), Snackbar
                        .LENGTH_LONG).setAction("Action", null).show();
                mSearchView.setQuery("", false);
                mSearchView.clearFocus();
                vMenuItem.collapseActionView();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void filter(String pQuery) {

        final MatrixCursor vCursor = new MatrixCursor(new String[]{BaseColumns._ID, "label"});
        mShownCurrencies.clear();

        int vIndex = 0;
        for (Currency vCurrency : mAllCurrencies) {
            if (vCurrency.getDisplayName(Locale.getDefault()).toLowerCase().contains(pQuery.toLowerCase()) ||
                    vCurrency.getCurrencyCode().toLowerCase().contains(pQuery.toLowerCase())) {
                vCursor.addRow(new Object[]{vIndex, vCurrency.getDisplayName(Locale.getDefault()) + " (" +
                        vCurrency.getCurrencyCode() + ")"});
                mShownCurrencies.add(vCurrency);
                vIndex++;
            }
        }
        mSearchViewCursorAdapter.changeCursor(vCursor);
    }
}
