/*
 * Square up android app
 * Copyright (C) 2016 Sebastien BALARD
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

package com.sebastien.balard.android.squareup.ui.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.db.SQDatabaseHelper;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQConstants;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQContactUtils;
import com.sebastien.balard.android.squareup.misc.utils.SQUIUtils;
import com.sebastien.balard.android.squareup.ui.widgets.adapters.SQContactCursorAdapter;
import com.sebastien.balard.android.squareup.ui.widgets.chips.SQChipsView;
import com.sebastien.balard.android.squareup.ui.widgets.listeners.SQRecyclerViewItemTouchListener;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sebastien BALARD on 20/04/2016.
 */
public class SQSearchContactFragment extends Fragment {

    public static final String TAG = SQSearchContactFragment.class.getSimpleName();

    @BindView(R.id.sq_fragment_search_contact_chipsview_participants)
    protected SQChipsView mChipsViewParticipants;
    @BindView(R.id.sq_fragment_search_contact_recyclerview)
    protected RecyclerView mRecyclerView;

    private OnContactsSelectionListener mListener;

    private SQContactCursorAdapter mAdapter;

    public interface OnContactsSelectionListener {
        void onContactsSelected(List<SQPerson> vContacts);
    }

    public static final SQSearchContactFragment newInstance(String pStartContent) {
        SQSearchContactFragment vFragment = new SQSearchContactFragment();
        Bundle vBundle = new Bundle(2);
        vBundle.putString("START_CONTENT", pStartContent);
        vBundle.putBoolean("HAS_CONTACTS_PERMISSIONS", true);
        vFragment.setArguments(vBundle);
        return vFragment;
    }

    public static final SQSearchContactFragment newInstanceWithoutContactsPermissions(String pStartContent) {
        SQSearchContactFragment vFragment = new SQSearchContactFragment();
        Bundle vBundle = new Bundle(2);
        vBundle.putString("START_CONTENT", pStartContent);
        vBundle.putBoolean("HAS_CONTACTS_PERMISSIONS", false);
        vFragment.setArguments(vBundle);
        return vFragment;
    }

    public static final SQSearchContactFragment newInstance(List<SQPerson> pParticipants, String pStartContent) {
        SQSearchContactFragment vFragment = new SQSearchContactFragment();
        Bundle vBundle = new Bundle(3);
        vBundle.putString("START_CONTENT", pStartContent);
        long[] vIds = new long[pParticipants.size()];
        for (int vIndex = 0;vIndex < pParticipants.size();vIndex++) {
            vIds[vIndex] = pParticipants.get(vIndex).getId();
        }
        vBundle.putLongArray(SQConstants.EXTRA_ARRAY_PERSON_IDS, vIds);
        /*vBundle.putLongArray(SQConstants.EXTRA_ARRAY_PERSON_IDS, pParticipants.stream().map(SQPerson::getId).collect
                (Collectors.<long>toList()).toArray());*/
        vBundle.putBoolean("HAS_CONTACTS_PERMISSIONS", true);
        vFragment.setArguments(vBundle);
        return vFragment;
    }

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        SQLog.v("onCreate");
        try {
            mListener = (OnContactsSelectionListener) getActivity();
        } catch (ClassCastException pException) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() + " must implement " +
                    "OnContactsSelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater pInflater, @Nullable ViewGroup pContainer, @Nullable Bundle
            pSavedInstanceState) {
        SQLog.v("onCreateView");
        View vView = pInflater.inflate(R.layout.sq_fragment_search_contact, pContainer, false);
        ButterKnife.bind(this, vView);
        return vView;
    }

    @Override
    public void onViewCreated(View pView, @Nullable Bundle pSavedInstanceState) {
        SQLog.v("onViewCreated");

        initChipsView();
        long[] vExistingParticipantsIds = getArguments().getLongArray(SQConstants.EXTRA_ARRAY_PERSON_IDS);
        if (vExistingParticipantsIds != null) {
            try {
                fillChipsView(vExistingParticipantsIds);
            } catch (SQLException pException) {
                SQLog.e("fail to load existing people", pException);
            }
        }
        mChipsViewParticipants.getEditText().setText(getArguments().getString("START_CONTENT"));
        mChipsViewParticipants.getEditText().post(() -> {
            mChipsViewParticipants.getEditText().requestFocus();
            mChipsViewParticipants.getEditText().setSelection(3);
            /*mChipsViewParticipants.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View pView, boolean pHasFocus) {
                    if (pHasFocus) {
                        SQUIUtils.SoftInput.show(getActivity(), pView);
                    }
                }
            });*/
        });
        if (getArguments().getBoolean("HAS_CONTACTS_PERMISSIONS")) {
            initRecyclerView();
            loadCursor();
        }
    }

    private void fillChipsView(long[] pPeopleIds) throws SQLException {
        SQPerson vPerson;
        for (long vId : pPeopleIds) {
            vPerson = SQDatabaseHelper.getInstance(getActivity()).getPersonDao().queryForId(vId);
            Uri vUri = null;
            if (vPerson.getPhotoUriString() != null) {
                vUri = Uri.parse(vPerson.getPhotoUriString()) ;
            }
            mChipsViewParticipants.addChip(vPerson.getName(), vUri, vPerson);
        }
    }

    private void loadCursor() {
        mAdapter = new SQContactCursorAdapter(getActivity(), R.layout.sq_item_contacts_list, getCursor(null));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setFilterQueryProvider(this::getCursor);
        mAdapter.getFilter().filter(getArguments().getString("START_CONTENT"));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new SQRecyclerViewItemTouchListener(getActivity(), mRecyclerView, new
                SQRecyclerViewItemTouchListener.OnItemTouchListener() {
                    @Override
                    public void onClick(View pView, int pPosition) {
                        Cursor vCursor = mAdapter.getCursor();
                        vCursor.moveToPosition(pPosition);

                        long vContactId = vCursor.getLong(vCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String vDisplayName = SQContactUtils.getDisplayName(getActivity(), vContactId);
                        Uri vPhotoUri = SQContactUtils.getPhotoUri(getActivity(), vContactId);

                        SQPerson vChipsContact = new SQPerson(vContactId);
                        mChipsViewParticipants.addChip(vDisplayName, vPhotoUri, vChipsContact);
                    }

                    @Override
                    public void onLongClick(View pView, int pPosition) {
                        // do nothing
                    }

                    @Override
                    public boolean isEnabled(int pPosition) {
                        return true;
                    }
                }));
    }

    private void initChipsView() {
        mChipsViewParticipants.mContext = getActivity();
        mChipsViewParticipants.getEditText().setHint(getString(R.string.sq_hint_add_participants));
        mChipsViewParticipants.setChipsListener(new SQChipsView.ChipsListener() {
            @Override
            public void onChipAdded(SQChipsView.SQChip pChip) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter("zzzz");
                }
                mChipsViewParticipants.requestFocus();
                SQUIUtils.SoftInput.show(getActivity(), mChipsViewParticipants.getEditText());
            }

            @Override
            public void onChipDeleted(SQChipsView.SQChip pChip) {

            }

            @Override
            public void onTextChanged(CharSequence pCharSequence) {
                if (mAdapter != null) {
                    if (pCharSequence.length() > 2) {
                        mAdapter.getFilter().filter(pCharSequence);
                    } else {
                        mAdapter.getFilter().filter("zzzz");
                    }
                }
            }

            @Override
            public void onContentValidated() {
                mListener.onContactsSelected(mChipsViewParticipants.getContacts());
                SQUIUtils.SoftInput.hide(getActivity());
                getFragmentManager().popBackStack();
            }
        });
    }

    private Cursor getCursor(CharSequence pConstraint) {
        Uri vUri = ContactsContract.Contacts.CONTENT_URI;
        String[] vProjection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        String vSelection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
        String[] vArgs = null;
        if (pConstraint != null) {
            vSelection += "AND LOWER(" + ContactsContract.Contacts.DISPLAY_NAME + ") LIKE ?";
            vArgs = new String[] { "%" + pConstraint.toString().toLowerCase(Locale.getDefault()) + "%" };
        }
        String vOrderBy = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return getActivity().getContentResolver().query(vUri, vProjection, vSelection, vArgs, vOrderBy);
    }
}
