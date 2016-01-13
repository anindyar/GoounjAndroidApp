package com.orgware.polling.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by nandagopal on 13/1/16.
 */
public class CustomViewPager extends ViewPager {

    private boolean pageEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setPageEnabled(boolean enabled) {
        pageEnabled = enabled;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!pageEnabled)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!pageEnabled)
            return false;
        return super.onTouchEvent(ev);
    }
}