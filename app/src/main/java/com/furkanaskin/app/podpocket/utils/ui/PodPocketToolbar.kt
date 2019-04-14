package com.furkanaskin.app.podpocket.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.furkanaskin.app.podpocket.R


class PodPocketToolbar : Toolbar {
    private var isBackButtonVisible = true

    constructor(context: Context) : this(context, null) {
        initView(context, null, R.attr.toolbarStyle)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.style.ToolBarStyle_Base) {
        initView(context, attrs, R.attr.toolbarStyle)

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        setBackgroundColor(Color.TRANSPARENT)
        setTitleTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        background = ContextCompat.getDrawable(context, R.drawable.bg_header)
        centerChilds()
    }

    fun setBackbuttonEnabled(isEnabled: Boolean) {
        isBackButtonVisible = isEnabled
        centerChilds()
    }

    private fun centerChilds() {
        val title = title
        val outViews = ArrayList<View>(1)
        findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT)
        if (outViews.isNotEmpty()) {
            val titleView = outViews[0] as TextView
            titleView.gravity = Gravity.CENTER_HORIZONTAL
            titleView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            val layoutParams = titleView.layoutParams as Toolbar.LayoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.setMargins(0, 0, if (isBackButtonVisible) 140 else 60, 0)
            requestLayout()
        }
    }

}