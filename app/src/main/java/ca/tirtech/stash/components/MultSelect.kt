package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getTextArrayOrThrow
import androidx.databinding.BindingAdapter
import ca.tirtech.stash.R
import ca.tirtech.stash.databinding.CustomMultiSelectBinding
import com.google.android.material.chip.Chip
import java.lang.IllegalArgumentException

class MultSelect(context: Context, attrs: AttributeSet) : ConstraintLayout(context,attrs) {
    private var binding: CustomMultiSelectBinding
    private val chips: HashMap<Any, Chip> = HashMap()

    init {
        val atribs = context.obtainStyledAttributes(attrs, R.styleable.MultSelect)
        binding = CustomMultiSelectBinding.inflate(LayoutInflater.from(context), this, true).also {
            it.btnMultiselect.setOnClickListener { addChip() }
        }
        if (!isInEditMode) {
            try {
                val array = atribs.getTextArrayOrThrow(R.styleable.MultSelect_android_entries)
                setEntries(array.toList())
            } catch (e: IllegalArgumentException) {
                Log.w("MultiSelect", "Attribute 'android:entries' was not defined or is databound. Placeholder was used instead")
                setEntries(resources.getStringArray(R.array.test_strings).toList())
            }
        }else {
            setEntries(resources.getStringArray(R.array.test_strings).toList())
        }
        Log.i("MultiSelect","TEST")
    }

    fun getValues(): ArrayList<Any> = arrayListOf(chips.keys)

    fun setEntries(lst: List<Any>) {
        binding.choices = lst
        binding.invalidateAll()
    }

    private fun addChip() = Chip(context).also { chip ->
        chip.text = binding.spnMultiselect.selectedItem.toString()
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            chips.remove(chip)
            binding.chpgrpMultiselect.removeView(chip)
        }
        chips[binding.spnMultiselect.selectedItem] = chip
        binding.chpgrpMultiselect.addView(chip)
    }
}
