package com.example.austin.menu;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by flame on 10/29/2017.
 */

class MarkerWindow extends AppCompatTextView {
    public MarkerWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.argb(255, 255, 127, 0));
        this.setTextColor(Color.argb(255, 255, 255, 255));
        this.setFrame(10, 10, 10, 10);

    }
}
