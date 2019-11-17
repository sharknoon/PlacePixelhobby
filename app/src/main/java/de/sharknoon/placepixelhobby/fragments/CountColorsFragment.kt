package de.sharknoon.placepixelhobby.fragments


import android.arch.lifecycle.MutableLiveData
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
import de.sharknoon.placepixelhobby.utils.replaceChildFragment


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

    private var currentState = MutableLiveData<States>().apply {
        value = States.SELECTION
    }

    override fun onResume() {
        super.onResume()

        currentState.observeForever {
            it?.also { ns -> setState(ns) }
        }
        //Listening to the next button
        val button = requireActivity().findViewById<Button>(R.id.button_fragment_counter_next)
        button?.setOnClickListener {
            toggleState()
            if (currentState.value == States.SELECTION) {
                //Clearing the selection of the previous recyclerview
                SubimageExtensions.clearSelections()
            }
        }
    }

    private fun toggleState() {
        when (currentState.value) {
            States.SELECTION -> currentState.value = States.COLOR_VIEW
            States.COLOR_VIEW -> currentState.value = States.SELECTION
        }
    }

    private fun setState(newState: States) {
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
                //Changing the bottom text
                SubimageExtensions.removeSelectionChangeListener(this::onSelectionChanged)
                textView.text = getString(R.string.x_of_y_colors, amountColors.size, 16)

                //Changing the button text
                button?.text = resources.getString(R.string.finish)
                button?.isEnabled = true

                //Changing the fragment
                val fragment = ListColorsFragment.getInstance(amountColors)
                replaceChildFragment(R.id.frame_layout_fragment_count_colors_container, fragment)

            }
            States.SELECTION -> {
                SubimageExtensions.addSelectionChangeListener(this::onSelectionChanged)
                button?.text = resources.getString(R.string.next)

                //Changing the fragment
                val fragment = ImageSelectionFragment.getInstance()
                replaceChildFragment(R.id.frame_layout_fragment_count_colors_container, fragment)
            }
        }
    }

    private fun onSelectionChanged(amountSelected: Int) {
        val textView = requireActivity()
            .findViewById<TextView>(R.id.text_view_fragment_counter_amount_selected_images)
        val button = requireActivity()
            .findViewById<Button>(R.id.button_fragment_counter_next)
        textView.text = resources.getQuantityString(
            R.plurals.x_images_selected,
            amountSelected,
            amountSelected
        )
        button.isEnabled = amountSelected > 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("state", currentState.value)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.also {
            currentState.value = it.getSerializable("state") as States
        }
    }

    companion object {
        private val self = CountColorsFragment()
        fun getInstance() = self
    }


}
