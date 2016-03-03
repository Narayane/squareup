package com.sebastien.balard.android.squareup.ui.widgets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;

import java.util.Currency;
import java.util.List;

/**
 * Created by sbalard on 03/03/2016.
 */
public class SQCurrenciesListAdapter extends SimpleCursorAdapter implements Filterable {

    List<Currency> mCurrenciesList;

    public SQCurrenciesListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    /*public SQCurrenciesListAdapter() {
        mCurrenciesList = new ArrayList<Currency>();
        mCurrenciesList.addAll(Currency.getAvailableCurrencies());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        return new ViewHolder(LayoutInflater.from(pParent.getContext()).inflate(R.layout.sq_item_currencies_list,
                pParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder pHolder, int pPosition) {
        Currency vCurrency = mCurrenciesList.get(pPosition);
        String vLabel = vCurrency.getDisplayName(Locale.getDefault());
        vLabel += " (";
        vLabel += vCurrency.getCurrencyCode() + ")";
        pHolder.mLabelTextView.setText(vLabel);
    }

    @Override
    public int getItemCount() {
        return mCurrenciesList.size();
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mLabelTextView;

        public ViewHolder(View pView) {
            super(pView);

            mLabelTextView = (TextView) itemView.findViewById(R.id.sq_item_currencies_list_label);
        }
    }
}
