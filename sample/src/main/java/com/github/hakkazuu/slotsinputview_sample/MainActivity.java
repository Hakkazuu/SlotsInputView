package com.github.hakkazuu.slotsinputview_sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.TextView;

import com.github.hakkazuu.slotsinputview.SlotsInputView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mInfoTextView;
    private SlotsInputView mSlotsInputView1;

    private Button mGoButton;
    private SlotsInputView mSlotsInputView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Created programmatically
        mInfoTextView = findViewById(R.id.info_text_view);
        mSlotsInputView1 = findViewById(R.id.view_slots_input1);
        mSlotsInputView1.setLength(5);
        mSlotsInputView1.setHint("*****");
        mSlotsInputView1.setSlotTextSize(30);
        mSlotsInputView1.setSlotTextColor(R.color.text_color_states);
        mSlotsInputView1.setHintTextColor(R.color.hint_color_states);
        mSlotsInputView1.setUnderlineColor(R.color.underline_color_states);
        mSlotsInputView1.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSlotsInputView1.setOnSlotsTextChangedListener(new SlotsInputView.OnSlotsTextChangedListener() {
            @Override
            public void onSlotsTextChanged(String text, ArrayList<String> textArrayList, boolean isFilled) {
                mInfoTextView.setText(
                        "mSlotsInputView1" +
                        "\ntext: " + text +
                        "\ntextArrayList: " + textArrayList.toString() +
                        "\nisFilled: " + isFilled);
            }
        });

        // Created with XML attrs
        mGoButton = findViewById(R.id.go_button);
        mSlotsInputView2 = findViewById(R.id.view_slots_input2);
        mSlotsInputView2.setOnSlotsTextChangedListener(new SlotsInputView.OnSlotsTextChangedListener() {
            @Override
            public void onSlotsTextChanged(String text, ArrayList<String> textArrayList, boolean isFilled) {
                mGoButton.setEnabled(isFilled);
            }
        });

    }

}
