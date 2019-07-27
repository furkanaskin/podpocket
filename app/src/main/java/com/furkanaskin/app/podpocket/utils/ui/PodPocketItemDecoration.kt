package com.furkanaskin.app.podpocket.utils.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PodPocketItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager is RecyclerView.LayoutManager) {
            val position = parent.getChildAdapterPosition(view) // item position
            val spanCount = 1
            val column = position % spanCount // item column

            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        }
    }
}