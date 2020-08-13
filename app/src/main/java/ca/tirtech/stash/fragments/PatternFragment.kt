package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ca.tirtech.stash.R
import ca.tirtech.stash.forms.Form
import ca.tirtech.stash.forms.FormControlRegistry
import ca.tirtech.stash.forms.Subform
import com.google.android.material.snackbar.Snackbar

class PatternFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_patterns, container, false) as ConstraintLayout
        val form = Form("Test Form", requireContext(), onCancel = {Snackbar.make(root,"Cancelled",Snackbar.LENGTH_SHORT).show()}, onSubmitComplete = {Snackbar.make(root, "Submit with $it", Snackbar.LENGTH_SHORT).show()})
            .addField("Test A", FormControlRegistry.Control.STRING, default={"Boo"})
            .addForm(Subform("Nested Form", requireContext()).addField(
                "Nested Field",
                FormControlRegistry.Control.SINGLE_CHOICE,
                mapOf("options" to arrayListOf("A", "B", "C")),
                default = {"C"}))
        form.build()
        form.mount(root)
        return root
    }
}
