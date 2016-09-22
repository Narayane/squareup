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

package com.sebastien.balard.android.squareup.ui.widgets.chips;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sebastien.balard.android.squareup.R;
import com.sebastien.balard.android.squareup.data.models.SQPerson;
import com.sebastien.balard.android.squareup.misc.SQLog;
import com.sebastien.balard.android.squareup.misc.utils.SQDialogUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sebastien BALARD on 06/05/2016.
 */
public class SQChipsView extends ScrollView implements SQChipsEditText.InputConnectionWrapperInterface {

    //<editor-fold desc="Static Fields">
    private static final String TAG = "ChipsView";
    private static final int CHIP_HEIGHT = 32; // dp
    private static final int SPACING_TOP = 2; // dp
    private static final int SPACING_BOTTOM = 2; // dp
    public static final int DEFAULT_VERTICAL_SPACING = 2; // dp
    private static final int DEFAULT_MAX_HEIGHT = -1;
    //</editor-fold>

    //<editor-fold desc="Attributes">
    private int mMaxHeight; // px
    private int mVerticalSpacing;

    private int mChipsColor;
    private int mChipsColorClicked;
    private int mChipsBgColor;
    private int mChipsBgColorClicked;
    private int mChipsTextColor;
    private int mChipsTextColorClicked;
    private int mChipsPlaceholderResId;
    private int mChipsDeleteResId;
    //</editor-fold>

    //<editor-fold desc="Private Fields">
    private float mDensity;
    private RelativeLayout mChipsContainer;
    private ChipsListener mChipsListener;
    private SQChipsEditText mEditText;
    private SQChipsVerticalLinearLayout mRootChipsLayout;
    private EditTextListener mEditTextListener;
    private List<SQChip> mChipList = new ArrayList<>();
    private Object mCurrentEditTextSpan;
    public Activity mContext;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public SQChipsView(Context context) {
        super(context);
        init();
    }

    public SQChipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initAttr(context, attrs);
        mMaxHeight = DEFAULT_MAX_HEIGHT;
        mChipsColor = ContextCompat.getColor(context, R.color.base30);
        mChipsColorClicked = ContextCompat.getColor(context, R.color.sq_color_primary_dark);

        mChipsBgColor = ContextCompat.getColor(context, R.color.base10);
        mChipsBgColorClicked = ContextCompat.getColor(context, R.color.blue);

        mChipsTextColor = ContextCompat.getColor(context, R.color.sq_color_black);
        mChipsTextColorClicked = ContextCompat.getColor(context, R.color.sq_color_white);

        mChipsPlaceholderResId = R.drawable.sq_ic_person_24dp;
        mChipsDeleteResId = R.drawable.sq_ic_close_24dp;
        init();
    }

    public SQChipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initAttr(context, attrs);
        mMaxHeight = DEFAULT_MAX_HEIGHT;
        mChipsColor = ContextCompat.getColor(context, R.color.base30);
        mChipsColorClicked = ContextCompat.getColor(context, R.color.sq_color_primary_dark);

        mChipsBgColor = ContextCompat.getColor(context, R.color.base10);
        mChipsBgColorClicked = ContextCompat.getColor(context, R.color.blue);

        mChipsTextColor = ContextCompat.getColor(context, R.color.sq_color_black);
        mChipsTextColorClicked = ContextCompat.getColor(context, R.color.sq_color_white);

        mChipsPlaceholderResId = R.drawable.sq_ic_person_24dp;
        mChipsDeleteResId = R.drawable.sq_ic_close_24dp;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SQChipsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //initAttr(context, attrs);
        mMaxHeight = DEFAULT_MAX_HEIGHT;
        mChipsColor = ContextCompat.getColor(context, R.color.base30);
        mChipsColorClicked = ContextCompat.getColor(context, R.color.sq_color_primary_dark);

        mChipsBgColor = ContextCompat.getColor(context, R.color.base10);
        mChipsBgColorClicked = ContextCompat.getColor(context, R.color.blue);

        mChipsTextColor = ContextCompat.getColor(context, R.color.sq_color_black);
        mChipsTextColorClicked = ContextCompat.getColor(context, R.color.sq_color_white);

        mChipsPlaceholderResId = R.drawable.sq_ic_person_24dp;
        mChipsDeleteResId = R.drawable.sq_ic_close_24dp;
        init();
    }
    //</editor-fold>

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mMaxHeight != DEFAULT_MAX_HEIGHT) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    //<editor-fold desc="Initialization">
    /*private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChipsView,
                0, 0);
        try {
            mMaxHeight = a.getDimensionPixelSize(R.styleable.ChipsView_cv_max_height, DEFAULT_MAX_HEIGHT);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.ChipsView_cv_vertical_spacing, (int) (DEFAULT_VERTICAL_SPACING * mDensity));
            mChipsColor = a.getColor(R.styleable.ChipsView_cv_color,
                    ContextCompat.getColor(context, R.color.base30));
            mChipsColorClicked = a.getColor(R.styleable.ChipsView_cv_color_clicked,
                    ContextCompat.getColor(context, R.color.colorPrimaryDark));
            mChipsColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_color_error_clicked,
                    ContextCompat.getColor(context, R.color.color_error));

            mChipsBgColor = a.getColor(R.styleable.ChipsView_cv_bg_color,
                    ContextCompat.getColor(context, R.color.base10));
            mChipsBgColorClicked = a.getColor(R.styleable.ChipsView_cv_bg_color_clicked,
                    ContextCompat.getColor(context, R.color.blue));

            mChipsBgColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_bg_color_clicked,
                    ContextCompat.getColor(context, R.color.color_error));

            mChipsTextColor = a.getColor(R.styleable.ChipsView_cv_text_color,
                    Color.BLACK);
            mChipsTextColorClicked = a.getColor(R.styleable.ChipsView_cv_text_color_clicked,
                    Color.WHITE);
            mChipsTextColorErrorClicked = a.getColor(R.styleable.ChipsView_cv_text_color_clicked,
                    Color.WHITE);

            mChipsPlaceholderResId = a.getResourceId(R.styleable.ChipsView_cv_icon_placeholder,
                    R.drawable.ic_person_24dp);
            mChipsDeleteResId = a.getResourceId(R.styleable.ChipsView_cv_icon_delete,
                    R.drawable.ic_close_24dp);
        } finally {
            a.recycle();
        }
    }*/

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mVerticalSpacing = (int) (DEFAULT_VERTICAL_SPACING * mDensity);
        mChipsContainer = new RelativeLayout(getContext());
        addView(mChipsContainer);

        // Dummy item to prevent AutoCompleteTextView from receiving focus
        LinearLayout linearLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
        linearLayout.setLayoutParams(params);
        linearLayout.setFocusable(true);
        linearLayout.setFocusableInTouchMode(true);

        mChipsContainer.addView(linearLayout);

        mEditText = new SQChipsEditText(getContext(), this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) (SPACING_TOP * mDensity);
        layoutParams.bottomMargin = (int) (SPACING_BOTTOM * mDensity) + mVerticalSpacing;
        mEditText.setLayoutParams(layoutParams);
        mEditText.setMinHeight((int) (CHIP_HEIGHT * mDensity));
        mEditText.setPadding(0, 0, 0, 0);
        mEditText.setLineSpacing(mVerticalSpacing, (CHIP_HEIGHT * mDensity) / mEditText.getLineHeight());
        mEditText.setBackgroundColor(Color.argb(0, 0, 0, 0));
        mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType
                .TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        mChipsContainer.addView(mEditText);

        mRootChipsLayout = new SQChipsVerticalLinearLayout(getContext(), mVerticalSpacing);
        mRootChipsLayout.setOrientation(LinearLayout.VERTICAL);
        mRootChipsLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootChipsLayout.setPadding(0, (int) (SPACING_TOP * mDensity), 0, 0);
        mChipsContainer.addView(mRootChipsLayout);

        initListener();
    }

    private void initListener() {
        mChipsContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.requestFocus();
                unselectAllChips();
            }
        });

        mEditTextListener = new EditTextListener();
        mEditText.setHint(R.string.sq_hint_add_participants);
        mEditText.addTextChangedListener(mEditTextListener);
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    unselectAllChips();
                }
            }
        });
        /*mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView pTextView, int pKeyCode, KeyEvent pKeyEvent) {
                if ((pKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (pKeyCode == KeyEvent.KEYCODE_ENTER)) {
                    SQLog.v("KeyEvent.KEYCODE_ENTER");
                    return true;
                }
                return false;
            }
        });*/
    }
    //</editor-fold>

    //<editor-fold desc="Public Methods">
    /*public void addChip(String displayName, String avatarUrl, SQPerson contact) {
        addChip(displayName, Uri.parse(avatarUrl), contact);
    }*/

    public void addChip(String displayName, Uri avatarUrl, SQPerson contact) {
        addChip(displayName, avatarUrl, contact, false);
        mEditText.setText("");
        addLeadingMarginSpan();
    }

    public void addChip(String displayName, Uri avatarUrl, SQPerson contact, boolean isIndelible) {
        SQChip chip = new SQChip(displayName, avatarUrl, contact, isIndelible);
        mChipList.add(chip);
        if (mChipsListener != null) {
            mChipsListener.onChipAdded(chip);
        }

        onChipsChanged(true);
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @NonNull
    public List<SQChip> getChips() {
        return Collections.unmodifiableList(mChipList);
    }

    public List<SQPerson> getContacts() {
        List<SQPerson> vContacts = new ArrayList<>();
        for (SQChip vChip : mChipList) {
            vContacts.add(vChip.getContact());
        }
        Collections.sort(vContacts);
        return vContacts;
    }

    public boolean removeChipBy(SQPerson contact) {
        for (int i = 0; i < mChipList.size(); i++) {
            if (mChipList.get(i).getContact() != null && mChipList.get(i).getContact().equals(contact)) {
                mChipList.remove(i);
                onChipsChanged(true);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        mChipList.clear();
        onChipsChanged(true);
    }

    /*public Contact tryToRecognizeAddress() {
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            if (Common.isValidEmail(text)) {
                return new Contact(text, "", null, text, null);
            }
        }
        return null;
    }*/

    public void setChipsListener(ChipsListener chipsListener) {
        this.mChipsListener = chipsListener;
    }

    public EditText getEditText() {
        return mEditText;
    }
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    /**
     * rebuild all chips and place them right
     */
    private void onChipsChanged(final boolean moveCursor) {
        SQChipsVerticalLinearLayout.TextLineParams textLineParams = mRootChipsLayout.onChipsChanged(mChipList);

        // if null then run another layout pass
        if (textLineParams == null) {
            post(new Runnable() {
                @Override
                public void run() {
                    onChipsChanged(moveCursor);
                }
            });
            return;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
        params.topMargin = (int) ((SPACING_TOP + textLineParams.row * CHIP_HEIGHT) * mDensity) + textLineParams.row * mVerticalSpacing;
        mEditText.setLayoutParams(params);
        addLeadingMarginSpan(textLineParams.lineMargin);
        if (moveCursor) {
            mEditText.setSelection(mEditText.length());
        }
    }

    private void addLeadingMarginSpan(int margin) {
        Spannable spannable = mEditText.getText();
        if (mCurrentEditTextSpan != null) {
            spannable.removeSpan(mCurrentEditTextSpan);
        }
        mCurrentEditTextSpan = new android.text.style.LeadingMarginSpan.LeadingMarginSpan2.Standard(margin, 0);
        spannable.setSpan(mCurrentEditTextSpan, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mEditText.setText(spannable);
    }

    private void addLeadingMarginSpan() {
        Spannable spannable = mEditText.getText();
        if (mCurrentEditTextSpan != null) {
            spannable.removeSpan(mCurrentEditTextSpan);
        }
        spannable.setSpan(mCurrentEditTextSpan, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mEditText.setText(spannable);
    }

    private void onEnterPressed(String text) {
        if (text != null && text.length() > 0) {

            /*if (Common.isValidEmail(text)) {
                onEmailRecognized(text);
            } else {
                onNonEmailRecognized(text);
            }*/
            mEditText.setSelection(0);
        }
    }

    /*private void onEmailRecognized(String email) {
        onEmailRecognized(new Contact(email, "", null, email, null));
    }

    private void onEmailRecognized(Contact contact) {
        SQChip chip = new SQChip(contact.getDisplayName(), null, contact);
        mChipList.add(chip);
        if (mChipsListener != null) {
            mChipsListener.onChipAdded(chip);
        }
        post(new Runnable() {
            @Override
            public void run() {
                onChipsChanged(true);
            }
        });
    }*/

    private void selectOrDeleteLastChip() {
        if (mChipList.size() > 0) {
            onChipInteraction(mChipList.size() - 1);
        }
    }

    private void onChipInteraction(int position) {
        try {
            SQChip chip = mChipList.get(position);
            if (chip != null) {
                onChipInteraction(chip, true);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Out of bounds", e);
        }
    }

    private void onChipInteraction(SQChip chip, boolean nameClicked) {
        unselectChipsExcept(chip);
        if (chip.isSelected()) {
            mChipList.remove(chip);
            if (mChipsListener != null) {
                mChipsListener.onChipDeleted(chip);
            }
            onChipsChanged(true);
            if (nameClicked) {
                //mEditText.setText(chip.getContact().getEmailAddress());
                addLeadingMarginSpan();
                mEditText.requestFocus();
                mEditText.setSelection(mEditText.length());
            }
        } else {
            chip.setSelected(true);
            onChipsChanged(false);
        }
    }

    private void unselectChipsExcept(SQChip rootChip) {
        for (SQChip chip : mChipList) {
            if (chip != rootChip) {
                chip.setSelected(false);
            }
        }
        onChipsChanged(false);
    }

    private void unselectAllChips() {
        unselectChipsExcept(null);
    }
    //</editor-fold>

    //<editor-fold desc="InputConnectionWrapperInterface Implementation">
    @Override
    public InputConnection getInputConnection(InputConnection target) {
        return new KeyInterceptingInputConnection(target);
    }
    //</editor-fold>

    //<editor-fold desc="Inner Classes / Interfaces">
    private class EditTextListener implements TextWatcher {

        private boolean mIsPasteTextChange = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*if (count > 1) {
                mIsPasteTextChange = true;
            }*/
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("\n")) {
                SQLog.d("back");
                mChipsListener.onContentValidated();
            } else if (s.toString().endsWith("\n") || s.toString().endsWith(",") || s.toString().endsWith(";")) {
                SQLog.d("new person");
                Dialog vNewPersonDialog = createNewPersonDialog();
                vNewPersonDialog.show();
                AppCompatEditText vEditTextName = (AppCompatEditText) vNewPersonDialog.findViewById(R
                        .id.sq_dialog_create_contact_edittext_name);
                AppCompatEditText vEditTextWeight = (AppCompatEditText) vNewPersonDialog.findViewById(R
                        .id.sq_dialog_create_contact_edittext_weight);
                String vTypedText = s.toString().substring(0, s.toString().length() - 1);
                vEditTextName.append(vTypedText);
                vEditTextWeight.append("1");
                s.clear();
                mEditText.setSelection(0);
            } else {
                if (mChipsListener != null) {
                    mChipsListener.onTextChanged(s);
                }
            }
        }

        private Dialog createNewPersonDialog() {
             return SQDialogUtils.createDialogWithCustomView(mContext, R.string
                    .sq_dialog_title_create_contact, R.layout
                     .sq_dialog_create_contact, R.string.sq_actions_create,
                    null, (pDialogInterface, pWhich) -> {
                        Dialog vDialog = (Dialog) pDialogInterface;
                        AppCompatEditText vEditTextName = (AppCompatEditText) vDialog.findViewById(R
                                .id.sq_dialog_create_contact_edittext_name);
                        AppCompatEditText vEditTextWeight = (AppCompatEditText) vDialog.findViewById(R
                                .id.sq_dialog_create_contact_edittext_weight);
                        AppCompatEditText vEditTextEmail = (AppCompatEditText) vDialog.findViewById(R
                                .id.sq_dialog_create_contact_edittext_email);
                        String vName = vEditTextName.getText().toString();
                        String vEmail = vEditTextEmail.getText().toString();
                        int vWeight = Integer.valueOf(vEditTextWeight.getText().toString());
                        SQPerson vChipsContact = new SQPerson(vName, vEmail, vWeight);
                        SQChip chip = new SQChip(vName, null, vChipsContact);
                        mChipList.add(chip);
                        if (mChipsListener != null) {
                            mChipsListener.onChipAdded(chip);
                        }
                        post(() -> onChipsChanged(true));
                    }, null, false);
        }
    }

    private class KeyInterceptingInputConnection extends InputConnectionWrapper {

        public KeyInterceptingInputConnection(InputConnection target) {
            super(target, true);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (mEditText.length() == 0) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    selectOrDeleteLastChip();
                    return true;
                }
            } else {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mEditText.append("\n");
                    return true;
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (mEditText.length() == 0 && beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public interface ChipsListener {
        void onChipAdded(SQChip chip);

        void onChipDeleted(SQChip chip);

        void onTextChanged(CharSequence text);

        void onContentValidated();
    }

    public class SQChip implements View.OnClickListener {

        private static final int MAX_LABEL_LENGTH = 30;

        private int mChipsBgRes = R.drawable.sq_shape_chip_background;

        private String mLabel;
        private final Uri mPhotoUri;
        private final SQPerson mContact;
        private final boolean mIsIndelible;

        private RelativeLayout mView;
        private View mIconWrapper;
        private TextView mTextView;

        private ImageView mAvatarView;
        private ImageView mPersonIcon;
        private ImageView mCloseIcon;

        private boolean mIsSelected = false;

        public SQChip(String label, Uri photoUri, SQPerson contact) {
            this(label, photoUri, contact, false);
        }

        public SQChip(String label, Uri photoUri, SQPerson contact, boolean isIndelible) {
            this.mLabel = label;
            this.mPhotoUri = photoUri;
            this.mContact = contact;
            this.mIsIndelible = isIndelible;

            /*if (mLabel == null) {
                mLabel = contact.getEmailAddress();
            }*/

            if (mLabel.length() > MAX_LABEL_LENGTH) {
                mLabel = mLabel.substring(0, MAX_LABEL_LENGTH) + "...";
            }
        }

        public View getView() {
            if (mView == null) {
                mDensity = getResources().getDisplayMetrics().density;
                mView = (RelativeLayout) inflate(getContext(), R.layout.sq_widget_chip, null);
                mView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (CHIP_HEIGHT * mDensity)));
                mAvatarView = (ImageView) mView.findViewById(R.id.ri_ch_avatar);
                mIconWrapper = mView.findViewById(R.id.rl_ch_avatar);
                mTextView = (TextView) mView.findViewById(R.id.tv_ch_name);
                mPersonIcon = (ImageView) mView.findViewById(R.id.iv_ch_person);
                mCloseIcon = (ImageView) mView.findViewById(R.id.iv_ch_close);

                // set inital res & attrs
                mView.setBackgroundResource(mChipsBgRes);
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.getBackground().setColorFilter(mChipsBgColor, PorterDuff.Mode.SRC_ATOP);
                    }
                });
                mIconWrapper.setBackgroundResource(R.drawable.sq_shape_circle);
                mTextView.setTextColor(mChipsTextColor);

                // set icon resources
                mPersonIcon.setBackgroundResource(mChipsPlaceholderResId);
                mCloseIcon.setBackgroundResource(mChipsDeleteResId);


                mView.setOnClickListener(this);
                mIconWrapper.setOnClickListener(this);
            }
            updateViews();
            return mView;
        }

        private void updateViews() {
            mTextView.setText(mLabel);
            if (mPhotoUri != null) {
                Picasso.with(getContext()).load(mPhotoUri).noPlaceholder().into(mAvatarView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mPersonIcon.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            if (isSelected()) {

                mView.getBackground().setColorFilter(mChipsBgColorClicked, PorterDuff.Mode.SRC_ATOP);
                mTextView.setTextColor(mChipsTextColorClicked);
                mIconWrapper.getBackground().setColorFilter(mChipsColorClicked, PorterDuff.Mode.SRC_ATOP);

                mPersonIcon.animate().alpha(0.0f).setDuration(200).start();
                mAvatarView.animate().alpha(0.0f).setDuration(200).start();
                mCloseIcon.animate().alpha(1f).setDuration(200).setStartDelay(100).start();

            } else {

                mView.getBackground().setColorFilter(mChipsBgColor, PorterDuff.Mode.SRC_ATOP);
                mTextView.setTextColor(mChipsTextColor);
                mIconWrapper.getBackground().setColorFilter(mChipsColor, PorterDuff.Mode.SRC_ATOP);

                mPersonIcon.animate().alpha(0.3f).setDuration(200).setStartDelay(100).start();
                mAvatarView.animate().alpha(1f).setDuration(200).setStartDelay(100).start();
                mCloseIcon.animate().alpha(0.0f).setDuration(200).start();
            }
        }

        @Override
        public void onClick(View v) {
            mEditText.clearFocus();
            if (v.getId() == mView.getId()) {
                onChipInteraction(this, true);
            } else {
                onChipInteraction(this, false);
            }
        }

        public boolean isSelected() {
            return mIsSelected;
        }

        public void setSelected(boolean isSelected) {
            if (mIsIndelible) {
                return;
            }
            this.mIsSelected = isSelected;
        }

        public SQPerson getContact() {
            return mContact;
        }

        @Override
        public boolean equals(Object o) {
            if (mContact != null && o instanceof SQPerson) {
                return mContact.equals(o);
            }
            return super.equals(o);
        }
    }
}
