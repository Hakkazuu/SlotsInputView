package com.github.hakkazuu.slotsinputview_sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.TextView;

import com.github.hakkazuu.slotsinputview.SlotsInputView;


public class MainActivity extends Activity {

    private TextView mInfoTextView;
    private SlotsInputView mSlotsInputView1;
    private Button mAddSlotButton;
    private Button mRemoveSlotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoTextView = findViewById(R.id.info_text_view);

        mAddSlotButton = findViewById(R.id.add_slot_button);
        mAddSlotButton.setOnClickListener(view -> {
            mSlotsInputView1.setLength(mSlotsInputView1.getLength() + 1);
        });

        mRemoveSlotButton = findViewById(R.id.remove_slot_button);
        mRemoveSlotButton.setOnClickListener(view -> {
            mSlotsInputView1.setLength(mSlotsInputView1.getLength() - 1);
        });

        // Created programmatically
        mSlotsInputView1 = findViewById(R.id.view_slots_input1);
        mSlotsInputView1.setLength(4);
        mSlotsInputView1.setHint('*');
        mSlotsInputView1.setSlotTextSize(40);
        mSlotsInputView1.setSlotTextColor(R.color.text_color_states);
        mSlotsInputView1.setHintTextColor(R.color.hint_color_states);
        mSlotsInputView1.setUnderlineColor(R.color.underline_color_states);
        mSlotsInputView1.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSlotsInputView1.setOnSlotsTextChangedListener((text, textArrayList, isFilled) -> mInfoTextView.setText(
                "mSlotsInputView1" +
                "\ntext: " + text +
                "\ntextArrayList: " + textArrayList.toString() +
                "\nisFilled: " + isFilled));

    }

}
