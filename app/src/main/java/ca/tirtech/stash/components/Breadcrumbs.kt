package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ca.tirtech.stash.R
import ca.tirtech.stash.util.setVisibility

class Breadcrumbs(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private val crumbs: ArrayList<Crumb> = ArrayList()

    init {
        orientation = HORIZONTAL
    }

    fun setCrumbs(texts: List<String>) {
        removeAllViews()
        crumbs.clear()
        texts.forEach { text ->
            addCrumb(text)
        }
    }

    fun addCrumb(text: String) {
        val crumb = Crumb(context, this, text)
        crumbs.lastOrNull()?.setLast(false)
        crumb.setLast()
        crumbs.add(crumb)
        addView(crumb.view)
    }

    fun removeLastCrumb() = crumbs.removeLastOrNull()?.run{
        removeView(view)
        crumbs.lastOrNull()?.setLast()
    }

    private class Crumb(context: Context, root: ViewGroup, text: String){

        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_crumb, null)
        val txtText: TextView = view.findViewById(R.id.crumb_text)
        val imgArrow: ImageView = view.findViewById(R.id.crumb_image)

        init {
            txtText.text = text
        }

        fun setLast(isLast: Boolean = true) {
            imgArrow.setVisibility(!isLast)
        }

    }
}
