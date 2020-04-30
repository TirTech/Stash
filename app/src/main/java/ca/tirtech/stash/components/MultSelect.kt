package ca.tirtech.stash.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.databinding.CustomMultiSelectBinding
import com.google.android.material.chip.Chip

class MultSelect(context: Context, attrs: AttributeSet) : ConstraintLayout(context) {
    private var binding: CustomMultiSelectBinding
    private val chips: HashMap<Any, Chip> = HashMap()

    init {
        val atribs = context.obtainStyledAttributes(attrs, R.styleable.MultSelect)
        binding = CustomMultiSelectBinding.inflate(LayoutInflater.from(context), this, true).also {
            it.btnMultiselect.setOnClickListener { addChip() }
        }
        setChoices(atribs.getTextArray(R.styleable.MultSelect_android_entries).toList())
    }

    fun getValues(): ArrayList<Any> = arrayListOf(chips.keys)

    fun setChoices(lst: List<Any>) {
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
