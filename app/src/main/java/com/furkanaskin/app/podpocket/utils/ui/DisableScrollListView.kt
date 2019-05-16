package com.furkanaskin.app.podpocket.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView


class DisableScrollListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.action == MotionEvent.ACTION_MOVE) true else super.dispatchTouchEvent(ev)
    }
}