package com.furkanaskin.app.podpocket.utils.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class PodPocketSpinner : AppCompatSpinner {

    constructor(context: Context) : this(context, null) {
        setDefaultFont()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        setDefaultFont()

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setDefaultFont()
    }

    private fun setDefaultFont() {

    }


}