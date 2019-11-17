package de.sharknoon.placepixelhobby.fragments

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.ToggleButton
import com.github.chrisbanes.photoview.PhotoView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper
import de.sharknoon.placepixelhobby.utils.setImageDrawableKeepZoom
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onResume() {
        super.onResume()
        initImage()
        initRadioGroup()
    }

    enum class ImageType {
        ORIGINAL, CLEAN
    }

    private var selectedImageType = MutableLiveData<ImageType>()

    private fun initImage() {
        selectedImageType.observeForever {
            val imageType = it ?: return@observeForever
            val a = requireActivity()
            val photoView = a.findViewById<PhotoView>(R.id.photo_view_fragment_final_clean)
            val imageId = when (imageType) {
                ImageType.ORIGINAL -> R.drawable.ic_place_99
                ImageType.CLEAN -> R.drawable.ic_final_clean
            }
            val drawable = a.resources.getDrawable(imageId, a.theme)
            val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
            photoView.setImageDrawableKeepZoom(aliasingDrawableWrapper)
            photoView.maximumScale = 50F
        }

        selectedImageType.value = ImageType.ORIGINAL
    }

    private fun initRadioGroup() {
        val a = requireActivity()

        val radioGroup = a.findViewById<RadioGroup>(R.id.radio_group_fragment_place)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            for (j in 0 until radioGroup.childCount) {
                val view = radioGroup.getChildAt(j) as ToggleButton
                view.isChecked = view.id == checkedId
            }
            when (checkedId) {
                R.id.toggle_button_fragment_place_original ->
                    selectedImageType.value = ImageType.ORIGINAL
                R.id.toggle_button_fragment_place_clean ->
                    selectedImageType.value = ImageType.CLEAN
            }
        }

        val toggleButtonOriginal =
            a.findViewById<ToggleButton>(R.id.toggle_button_fragment_place_original)
        val toggleButtonClean =
            a.findViewById<ToggleButton>(R.id.toggle_button_fragment_place_clean)
        val listener = View.OnClickListener {
            radioGroup.check(it.id)
        }
        toggleButtonOriginal.setOnClickListener(listener)
        toggleButtonClean.setOnClickListener(listener)

        toggle_button_fragment_place_original.performClick()
    }

    companion object {
        private val self = PlaceFragment()
        fun getInstance() = self
    }
}
