package com.furkanaskin.app.podpocket.utils.ui

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.furkanaskin.app.podpocket.utils.extensions.hide
import com.furkanaskin.app.podpocket.utils.extensions.show

class PodPocketRecyclerView : RecyclerView {
    private var emptyView: View? = null
    private var loadingView: View? = null
    private var countTextView: TextView? = null
    private var isCountTextAdd: Boolean = false
    private var isLoadingFinished: Boolean = false

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private fun checkIfEmpty() {
        if (context is Activity)
        (context as Activity).runOnUiThread {
            val count: Int
            if (adapter != null && isLoadingFinished) {
                if (loadingView != null)
                    loadingView!!.hide()
                count = adapter!!.itemCount
                if (this.countTextView != null) {
                    if (isCountTextAdd) {
                        countTextView!!.text = String.format("%s Adet", count)
                    } else {
                        countTextView!!.text = count.toString()
                    }
                }
                val emptyViewVisible = count < 1
                emptyView?.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
                visibility = if (!emptyViewVisible) {
                    View.VISIBLE
                } else
                    View.GONE
            } else if (loadingView != null) {
                loadingView!!.show()
                emptyView?.hide()
                visibility = View.GONE
            }
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(emptyObserver)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        checkIfEmpty()
        emptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View?) {
        if (emptyView == null)
            return
        this.emptyView = emptyView
        checkIfEmpty()
    }

    fun setLoadingView(loadingView: View?) {
        if (loadingView == null)
            return
        this.loadingView = loadingView
        checkIfEmpty()
    }

    fun disableLoadingView() {
        isLoadingFinished = true
        checkIfEmpty()
    }

    fun setCountTextView(countTextView: TextView, isCountTextAdd: Boolean) {
        this.isCountTextAdd = isCountTextAdd
        this.countTextView = countTextView
        checkIfEmpty()
    }
}