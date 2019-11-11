package de.sharknoon.placepixelhobby.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.SubimageExtensions
import de.sharknoon.placepixelhobby.utils.countColors
import de.sharknoon.placepixelhobby.utils.swapFragment

class CountColorsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflating the layout
        return inflater.inflate(R.layout.fragment_count_colors, container, false)
    }

    private enum class States {
        SELECTION, COLOR_VIEW
    }

    private var currentState = States.SELECTION

    override fun onResume() {
        super.onResume()

        switchState(States.SELECTION)
        //Listening to the next button
        val button = requireActivity().findViewById<Button>(R.id.button_fragment_counter_next)
        button?.setOnClickListener { toggleState() }
    }

    private fun toggleState() {
        when (currentState) {
            States.SELECTION -> switchState(States.COLOR_VIEW)
            States.COLOR_VIEW -> switchState(States.SELECTION)
        }
    }

    private fun switchState(newState: States) {
        val textView = requireActivity()
            .findViewById<TextView>(R.id.text_view_fragment_counter_amount_selected_images)
        val button = requireActivity()
            .findViewById<Button>(R.id.button_fragment_counter_next)
        when (newState) {
            States.COLOR_VIEW -> {
                //Get the selection...
                val selectedImages = SubimageExtensions.getSelectedImages()
                //...and count the colors
                val amountColors = countColors(selectedImages)
                //Changing the current state
                currentState = States.COLOR_VIEW
                //Clearing the selection of the previous recyclerview
                SubimageExtensions.clearSelections()
                //Changing the bottom text
                SubimageExtensions.removeSelectionChangeListener(this::onSelectionChanged)
                textView.text = getString(R.string.x_of_y_colors, amountColors.size, 16)

                //Changing the button text
                button?.text = resources.getString(R.string.finish)
                button?.isEnabled = true

                //Changing the fragment
                val fragment = ListColorsFragment.newInstance(amountColors)
                swapFragment(this, R.id.view_fragment_counter_container, fragment)

            }
            States.SELECTION -> {
                currentState = States.SELECTION
                SubimageExtensions.addSelectionChangeListener(this::onSelectionChanged)
                button?.text = resources.getString(R.string.next)

                //Changing the fragment
                val fragment = ImageSelectionFragment.getInstance()
                swapFragment(this, R.id.view_fragment_counter_container, fragment)
            }
        }
    }

    private fun onSelectionChanged(amountSelected: Int) {
        val textView = requireActivity()
            .findViewById<TextView>(R.id.text_view_fragment_counter_amount_selected_images)
        val button = requireActivity()
            .findViewById<Button>(R.id.button_fragment_counter_next)
        textView.text = resources.getString(
            if (amountSelected == 1) {
                R.string.x_images_selected_singular
            } else {
                R.string.x_images_selected_plural
            }, amountSelected
        )
        button.isEnabled = amountSelected > 0
    }

    companion object {
        private val self = CountColorsFragment()
        fun getInstance() = self
    }


}
