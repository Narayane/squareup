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
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQCurrencyUtils;
import com.sebastien.balard.android.squareup.models.SQCurrency;
import com.sebastien.balard.android.squareup.ui.SQActivity;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQCurrenciesListAdapter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

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
    @Bind(R.id.sq_activity_currencies_list_nestedscrollview_empty)
    NestedScrollView mEmptyView;
    @Bind(R.id.sq_activity_currencies_list_recyclerview)
    RecyclerView mRecyclerView;

    private SearchView mSearchView;
    private SimpleCursorAdapter mSearchViewCursorAdapter;
    private List<Currency> mAllCurrencies;
    private List<Currency> mShownCurrencies;
    private List<SQCurrency> mActivatedCurrencies;
    private SQCurrenciesListAdapter mAdapter;

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

        mAllCurrencies = SQCurrencyUtils.getAllCurrencies();
        mSearchViewCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, new
                String[]{"label"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mShownCurrencies = new ArrayList<Currency>();

        mActivatedCurrencies = new ArrayList<SQCurrency>();
        mAdapter = new SQCurrenciesListAdapter(mActivatedCurrencies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        SQLog.v("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.sq_menu_search, pMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pMenu) {
        SQLog.v("onPrepareOptionsMenu");
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
                Currency vSelected = mShownCurrencies.get(pPosition);
                Snackbar.make(mSearchView, vSelected.getDisplayName(Locale.getDefault()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mSearchView.setQuery("", false);
                mSearchView.clearFocus();
                vMenuItem.collapseActionView();
                SQCurrency vActivatedCurrency = new SQCurrency(vSelected.getCurrencyCode(), 1.0f);
                mActivatedCurrencies.add(vActivatedCurrency);
                mAdapter.notifyDataSetChanged();
                refreshLayout();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLog.v("onResume");

        refreshLayout();
    }

    @Override
    public void onBackPressed() {
        SQLog.v("onBackPressed");
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void refreshLayout() {
        if (mActivatedCurrencies.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
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
                        vCurrency.getCurrencyCode() + ", " + vCurrency.getSymbol(Locale.getDefault()) + ")"});
                mShownCurrencies.add(vCurrency);
                vIndex++;
            }
        }
        mSearchViewCursorAdapter.changeCursor(vCursor);
    }
}
