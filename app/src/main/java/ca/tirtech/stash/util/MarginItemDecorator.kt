package ca.tirtech.stash.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class MarginItemDecorator(private val spacing: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) = with(outRect) {
        if (parent.getChildAdapterPosition(view) == 0) {
            top = spacing
        }
        left += spacing
        right += spacing
        bottom += spacing
    }
}
