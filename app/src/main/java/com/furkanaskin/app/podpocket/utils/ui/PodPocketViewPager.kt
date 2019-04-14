package com.furkanaskin.app.podpocket.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class PodPocketViewPager : ViewPager {

    var swipeEnabled = false

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.swipeEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.swipeEnabled && super.onInterceptTouchEvent(event)
    }


}