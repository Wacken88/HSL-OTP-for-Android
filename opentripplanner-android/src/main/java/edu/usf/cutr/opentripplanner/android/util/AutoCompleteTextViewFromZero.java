package edu.usf.cutr.opentripplanner.android.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * Created by laptop on 4/5/15.
 */
public class AutoCompleteTextViewFromZero extends AutoCompleteTextView{

    public AutoCompleteTextViewFromZero(Context context) {
        super(context);
    }

    public AutoCompleteTextViewFromZero(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteTextViewFromZero(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        // Perform validation if the view is empty
        if (focused && (getFilter() != null) && isTextEmpty()) {
             super.performFiltering(getText().toString(), KeyEvent.KEYCODE_UNKNOWN);
        }
    }

    /**
     * Returns <code>true</code> if the text is empty - only used for recent
     * address feature
     */
    public boolean isTextEmpty() {
        return getText().length() == 0;
    }
}
