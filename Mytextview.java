package com.globalwebsoft.a8to8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 05-Jan-18.
 */

@SuppressLint("AppCompatCustomView")

public class Mytextview extends TextView {

    public Mytextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Mytextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mytextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/arlrdbd.ttf");
        setTypeface(tf ,1);

    }
}

