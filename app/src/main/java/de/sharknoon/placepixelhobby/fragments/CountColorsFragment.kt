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

class CountColorsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflating the layout
        return inflater.inflate(R.layout.fragment_counter, container, false)
    }

    override fun onResume() {
        super.onResume()

        //Inserting the image selection fragment
        val imageSelectionFragment = ImageSelectionFragment.getInstance()
        childFragmentManager.beginTransaction().apply {
            replace(R.id.view_fragment_counter_container, imageSelectionFragment)
            commit()
        }

        //Listening to the next button
        val button = requireActivity().findViewById<Button>(R.id.button_fragment_counter_next)
        button?.setOnClickListener(this::onNextButtonClicked)

        //Listening to selection changes
        SubimageExtensions.onSelectionChanged(this::onSelectionChanged)
    }

    private fun onNextButtonClicked(view: View) {

    }

    private fun onSelectionChanged(amountSelected: Int) {
        val textView = requireActivity()
            .findViewById<TextView>(R.id.text_view_fragment_counter_amount_selected_images)
        textView.text = resources.getString(
            if (amountSelected == 1) {
                R.string.x_images_selected_singular
            } else {
                R.string.x_images_selected_plural
            }, amountSelected
        )
    }

    companion object {
        private val self = CountColorsFragment()
        fun getInstance() = self
    }


}
