package com.furkanaskin.app.podpocket.utils.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PodPocketEditText : AppCompatEditText {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    /*    val typeface = ResourcesCompat.getFont(context, R.font.montserrat)
        setTypeface(typeface)*/
    }
}