# **SlotsInputView**

`SlotsInputView` is a simple view for input activation codes, PIN-codes etc.

![](https://imgur.com/N08gwIl.gif)

## **Integration**

Keep in mind that `SlotsInputView` has min API level 14 and this dependency:
```groovy
implementation 'androidx.appcompat:appcompat:1.2.0'
```

In Project `build.gradle`:
```groovy
repositories {
    jcenter()
}
```

In module `build.gradle`:
```groovy
compile 'com.github.hakkazuu:slotsinputview:0.0.8'
```

## **Sample**
First declare `SlotsInputView` in your `layout.xml`:

```xml
    <com.github.hakkazuu.slotsinputview.SlotsInputView
        android:id="@+id/view_slots_input"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

Then you can configure view programmatically:
```java
        SlotsInputView slotsInputView = findViewById(R.id.view_slots_input);
        
        // Number of slots (default - 4)
        slotsInputView.setLength(5);
        
        // Hint text (default - '*')
        slotsInputView.setHint('*');
        
        // Text size in SP (default - 20)
        slotsInputView.setSlotTextSize(30);
        
        // Simple colors from values folder or declared XMLs with states in res/color
        // (default - black for text and underline, gray for hint)
        slotsInputView.setSlotTextColor(R.color.text_color_states);
        slotsInputView.setHintTextColor(R.color.hint_color_states);
        slotsInputView.setUnderlineColor(R.color.underline_color_states);
        
        // InputType for all slots (default - InputType.TYPE_CLASS_NUMBER)
        slotsInputView.setInputType(InputType.TYPE_CLASS_NUMBER);
```

Or you can do the same with XML attributes: 

```xml
    <com.github.hakkazuu.slotsinputview.SlotsInputView
        app:siv_length="5"
        app:siv_hint="*"
        app:siv_text_size="30sp"
        app:siv_text_color="@color/text_color_states"
        app:siv_hint_color="@color/hint_color_states"
        app:siv_underline_color="@color/underline_color_states"
        app:siv_input_type="number"/>
```

Finally, handle SlotInputView's text and status of filling slots in your code as you want: 
```java
    // as example, "anyButton" will be enabled when all slots are filled
    slotsInputView.setOnSlotsTextChangedListener(
        new SlotsInputView.OnSlotsTextChangedListener() {
            @Override
            public void onSlotsTextChanged(String text, ArrayList<String> textArrayList, boolean isFilled) {
                anyButton.setEnabled(isFilled);
            }
    });
```

## **What's new**
* **0.0.7**
    * Now hint value is a `char` (`String` in older)

## **License**

    Copyright 2020 Anton Shakhov
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
