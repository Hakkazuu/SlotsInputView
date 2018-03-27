package com.github.hakkazuu.slotsinputview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by hakkazuu on 14.03.2018 at 20:07.
 */

public class SlotsInputView extends LinearLayout {

    private View mRoot;
    private LinearLayout mLayout;

    private int mLength;
    private int mSlotLength = DEFAULT_SLOT_LENGTH;
    private int mInputType;
    private float mTextSize;
    @ColorRes private int mTextColor;
    @ColorRes private int mHintColor;
    @ColorRes private int mUnderlineColor;
    private boolean mIsEnabled;
    private String mHint;
    private ArrayList<Slot> mSlotList = new ArrayList<>();
    private OnSlotsTextChangedListener mOnSlotsTextChangedListener = null;

    private static final int DEFAULT_LENGTH = 4;
    private static final int DEFAULT_INPUT_TYPE = InputType.TYPE_CLASS_NUMBER;
    private static final int DEFAULT_SLOT_LENGTH = 1;
    private static final float DEFAULT_TEXT_SIZE = 20f;
    @ColorRes private static final int DEFAULT_TEXT_COLOR = android.R.color.black;
    @ColorRes private static final int DEFAULT_HINT_COLOR = android.R.color.darker_gray;
    @ColorRes private static final int DEFAULT_UNDERLINE_COLOR = android.R.color.black;
    private static final boolean DEFAULT_ENABLED = true;
    private static final String DEFAULT_HINT = "****";

    public SlotsInputView(Context context) {
        super(context);
        init(context, null);
    }

    public SlotsInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlotsInputView, 0, 0);
        init(context, typedArray);
    }

    public SlotsInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlotsInputView, 0, 0);
        init(context, typedArray);
    }

    /**
     * Returns text
     * @return text
     */
    public String getText() {
        String text = "";
        for(Slot slot : mSlotList) {
            if(slot.mValue != null) {
                text = text + slot.mValue;
            }
        }
        return text;
    }

    /**
     * Sets new slots count and recreates slots
     * @param length - new slots count
     */
    public void setLength(int length) {
        mLength = length;
        createSlots();
    }

    /**
     * Returns slots count
     * @return - slot array's length
     */
    public int getLength() {
        return mLength;
    }

    /**
     * Sets text size
     * @param textSize - new text size
     */
    public void setSlotTextSize(float textSize) {
        mTextSize = textSize;
        for(Slot slot : mSlotList) {
            slot.mSlot.setTextSize(mTextSize);
        }
    }

    /**
     * Sets text color
     * @param color - simple color value or color resource XML with states
     */
    public void setSlotTextColor(@ColorRes int color) {
        mTextColor = color;
        for(Slot slot : mSlotList) {
            slot.mSlot.setTextColor(getResources().getColorStateList(color));
        }
    }

    /**
     * Sets hint color
     * @param color - simple color value or color resource XML with states
     */
    public void setHintTextColor(@ColorRes int color) {
        mHintColor = color;
        for(Slot slot : mSlotList) {
            slot.mSlot.setHintTextColor(getResources().getColorStateList(color));
        }
    }

    /**
     * Sets underline color
     * @param color - simple color value or color resource XML with states
     */
    public void setUnderlineColor(@ColorRes int color) {
        mUnderlineColor = color;
        for(Slot slot : mSlotList) {
            slot.mSlot.setBackgroundTintList(getResources().getColorStateList(color));
        }
    }

    /**
     * Sets hint text
     * @param hint - string
     */
    public void setHint(String hint) {
        mHint = hint;
        for(Slot slot : mSlotList) {
            if(mHint.length() > slot.mSlotIndex)
                slot.mSlot.setHint(String.valueOf(mHint.charAt(slot.mSlotIndex)));
            else slot.mSlot.setHint(null);
        }
    }

    /**
     * Sets input type
     * @param inputType - vars of interface InputType
     */
    public void setInputType(int inputType) {
        mInputType = inputType;
        for(Slot slot : mSlotList) {
            slot.mSlot.setInputType(mInputType);
        }
    }

    /**
     * Sets enable state
     * @param isEnabled - new enable state
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        mIsEnabled = isEnabled;
        for(Slot slot : mSlotList) {
            slot.mSlot.setEnabled(mIsEnabled);
            slot.mSlot.setTextColor(getResources().getColorStateList(mIsEnabled ? mTextColor : mHintColor));
        }
    }

    /**
     * Recreates slots
     */
    public void clear() {
        createSlots();
        checkSlots();
    }

    /**
     * Provides method which have a current text as string and as array,
     * also have a current fill state
     */
    public interface OnSlotsTextChangedListener {
        void onSlotsTextChanged(String text, ArrayList<String> textArrayList, boolean isFilled);
    }

    private void init(Context context, TypedArray typedArray) {
        mRoot = inflate(context, R.layout.view_slots_input, this);
        mLayout = mRoot.findViewById(R.id.view_slots_input_layout);
        if(typedArray != null) initAttributes(typedArray);
        createSlots();
    }

    private void initAttributes(@NonNull TypedArray typedArray) {
        mLength = typedArray.getInteger(R.styleable.SlotsInputView_siv_length, DEFAULT_LENGTH);
        mIsEnabled = typedArray.getBoolean(R.styleable.SlotsInputView_siv_enabled, DEFAULT_ENABLED);
        switch (typedArray.getInt(R.styleable.SlotsInputView_siv_input_type, 0)) {
            case 0:
                mInputType = InputType.TYPE_CLASS_NUMBER;
                break;
            case 1:
                mInputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                break;
            case 2:
                mInputType = InputType.TYPE_CLASS_TEXT;
                break;
            case 3:
                mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            default:
                mInputType = DEFAULT_INPUT_TYPE;
        }

        mTextSize = typedArray.getDimension(R.styleable.SlotsInputView_siv_text_size, DEFAULT_TEXT_SIZE);
        mTextColor = typedArray.getResourceId(R.styleable.SlotsInputView_siv_text_color, DEFAULT_TEXT_COLOR);

        String hintAttr = typedArray.getString(R.styleable.SlotsInputView_siv_hint);
        mHint = hintAttr == null ? DEFAULT_HINT : hintAttr;
        mHintColor = typedArray.getResourceId(R.styleable.SlotsInputView_siv_hint_color, DEFAULT_HINT_COLOR);

        mUnderlineColor = typedArray.getResourceId(R.styleable.SlotsInputView_siv_underline_color, DEFAULT_UNDERLINE_COLOR);
    }

    private void createSlots() {
        mLayout.removeAllViews();
        mSlotList.clear();

        for (int i = 0; i < mLength; i++) {
            Slot slot = new Slot(new EditText(mRoot.getContext()), i);
            mLayout.addView(slot.mSlot);
            mSlotList.add(slot);
        }
    }

    private void checkSlots() {
        boolean isAllFilled = true;
        String text = "";
        ArrayList<String> textArrayList = new ArrayList<>();
        for(Slot slot : mSlotList) {
            slot.mValue = slot.mSlot.getText().toString();
            if(slot.mValue != null) {
                text = text + slot.mValue;
                textArrayList.add(slot.mValue);
            }
            if(!slot.mIsFilled)
                isAllFilled = false;
        }
        if(mOnSlotsTextChangedListener != null)
            mOnSlotsTextChangedListener.onSlotsTextChanged(text, textArrayList, isAllFilled);
    }

    private void requestFocusOnNextSlot(Slot currentSlot) {
        if(mSlotList.indexOf(currentSlot) != mLength - 1)
            mSlotList.get(mSlotList.indexOf(currentSlot) + 1).mSlot.requestFocus();
        else {
            try {
                InputMethodManager imm = (InputMethodManager) mRoot.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRoot.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentSlot.mSlot.clearFocus();
            mLayout.requestFocus();
        }
    }

    private void requestFocusOnPrevSlot(Slot currentSlot) {
        if(mSlotList.indexOf(currentSlot) != 0)
            mSlotList.get(mSlotList.indexOf(currentSlot) - 1).mSlot.requestFocus();
        else {
            try {
                InputMethodManager imm = (InputMethodManager) mRoot.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRoot.getWindowToken(), 0);
            } catch (Exception e) {}
            mLayout.getFocusedChild().clearFocus();
        }
    }

    public void setOnSlotsTextChangedListener(OnSlotsTextChangedListener listener) {
        mOnSlotsTextChangedListener = listener;
    }

    private class Slot {

        private EditText mSlot;
        private int mSlotIndex;
        private boolean mIsFilled;
        private String mValue;

        Slot(EditText slot, int slotIndex) {
            mSlot = slot;
            mSlotIndex = slotIndex;
            mSlot.setEnabled(mIsEnabled);
            mSlot.setTextColor(getResources().getColorStateList(mIsEnabled ? mTextColor : mHintColor));
            mSlot.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mSlot.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mSlot.setEms(mSlotLength);
            mSlot.setTypeface(Typeface.MONOSPACE);
            mSlot.setSelectAllOnFocus(true);
            mSlot.setBackgroundTintList(getResources().getColorStateList(mUnderlineColor));
            mSlot.setHintTextColor(getResources().getColorStateList(mHintColor));
            if(mHint.length() > mSlotIndex)
                mSlot.setHint(String.valueOf(mHint.charAt(mSlotIndex)));
            mSlot.setMaxLines(1);
            mSlot.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSlotLength)});
            mSlot.setInputType(mInputType);
            mSlot.setOnFocusChangeListener((view, hasFocus) -> {
                if(hasFocus) {
                    mSlot.setHintTextColor(Color.TRANSPARENT);
                } else {
                    mSlot.setHintTextColor(getResources().getColorStateList(mHintColor));
                }
            });
            mSlot.setOnKeyListener((view, keyCode, event) -> {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    requestFocusOnPrevSlot(Slot.this);
                    checkSlots();
                }
                return false;
            });
            mSlot.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence text, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable editable) {
                    mValue = editable.toString();
                    if (editable.length() == mSlotLength) {
                        mIsFilled = true;
                        requestFocusOnNextSlot(Slot.this);
                    } else mIsFilled = false;
                    checkSlots();
                }
            });

            mIsFilled = false;
        }

    }

}