package com.zb.bilateral.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by yaos on 2018/5/6.
 */

public class PhotoScrollView extends ScrollView{
    boolean doublePointer;

    public PhotoScrollView(Context context) {
        super(context);
    }

    public PhotoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                doublePointer = false;
                onTouchEvent(ev); //在2.3的机器上的时候必须有这句，， 4.0不需要， 怀疑在4.0的OnTouch中对双手的点击也做了处理 而2.3没有 只能再出发一下
                break;
            case MotionEvent.ACTION_POINTER_DOWN :
                doublePointer = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doublePointer = false;
                break;
        }
        if (!doublePointer) {
            onTouchEvent(ev);
        } else {
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
