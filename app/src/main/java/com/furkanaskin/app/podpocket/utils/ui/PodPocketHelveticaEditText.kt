package com.furkanaskin.app.podpocket.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.furkanaskin.app.podpocket.R


class PodPocketHelveticaEditText : AppCompatEditText {

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
        if (!isInEditMode) {
            val typeface = ResourcesCompat.getFont(context, R.font.helvetica_regular)
            setTypeface(typeface)
        }
    }

}