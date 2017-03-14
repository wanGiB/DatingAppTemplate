package com.app.shixelsdating.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.app.shixelsdating.R;
import com.app.shixelsdating.utils.FontUtils;

/**
 * Created by wan on 3/13/17.
 */

public class WeMeetTextView extends AppCompatTextView {

    public WeMeetTextView(Context context) {
        super(context);
    }

    public WeMeetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WeMeetTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (!isInEditMode()) {
            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WeMeetTextView);
                String fontName = a.getString(R.styleable.WeMeetTextView_fontName);
                if (fontName != null) {
                    if (!isInEditMode()) {
                        Typeface myTypeface = FontUtils.getTypeface(getContext(), fontName);
                        setTypeface(myTypeface);
                    }
                }
                a.recycle();
            }
        }
    }
}
