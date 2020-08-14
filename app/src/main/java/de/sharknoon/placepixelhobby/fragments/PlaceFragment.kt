package de.sharknoon.placepixelhobby.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
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
        initImageOptions()
    }

    enum class ImageType {
        ORIGINAL, CLEAN, ORIGINAL_GRID, CLEAN_GRID;

        fun withGrid(): ImageType = when {
            this == CLEAN -> CLEAN_GRID
            this == ORIGINAL -> ORIGINAL_GRID
            else -> this
        }

        fun withoutGrid(): ImageType = when {
            this == CLEAN_GRID -> CLEAN
            this == ORIGINAL_GRID -> ORIGINAL
            else -> this
        }

        fun toClean(): ImageType = when {
            this == ORIGINAL_GRID -> CLEAN_GRID
            this == ORIGINAL -> CLEAN
            else -> this
        }

        fun toOriginal(): ImageType = when {
            this == CLEAN_GRID -> ORIGINAL_GRID
            this == CLEAN -> ORIGINAL
            else -> this
        }
    }

    private val selectedImageType = MutableLiveData<ImageType>()

    private fun initImage() {
        val a = requireActivity()
        val photoViewPlace = a.findViewById<PhotoView>(R.id.photo_view_fragment_place_image)
        val observer = { imageId: Int ->
            val drawable = a.resources.getDrawable(imageId, a.theme)
            val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
            photoViewPlace.setImageDrawableKeepZoom(aliasingDrawableWrapper)
            photoViewPlace.maximumScale = 50F
        }
        val imageTypeObserver = { imageType: ImageType? ->
            val imageId = when (imageType ?: ImageType.ORIGINAL) {
                ImageType.ORIGINAL -> R.drawable.ic_place_99
                ImageType.ORIGINAL_GRID -> R.drawable.ic_place_original_grid
                ImageType.CLEAN -> R.drawable.ic_final_clean
                ImageType.CLEAN_GRID -> R.drawable.ic_place_cleaned_grid
            }
            observer(imageId)
        }
        selectedImageType.observeForever(imageTypeObserver)

        selectedImageType.value = ImageType.ORIGINAL
    }

    private fun initImageOptions() {
        val a = requireActivity()

        val radioGroup = a.findViewById<RadioGroup>(R.id.radio_group_fragment_place)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            for (j in 0 until radioGroup.childCount) {
                val view = radioGroup.getChildAt(j) as ToggleButton
                view.isChecked = view.id == checkedId
            }
            if (checkedId == R.id.toggle_button_fragment_place_original) {
                selectedImageType.value = selectedImageType.value?.toOriginal()
            } else {
                selectedImageType.value = selectedImageType.value?.toClean()
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

        // Switch settings
        val switchGrid = a.findViewById<Switch>(R.id.switch_fragment_place_grid)
        switchGrid.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedImageType.value = selectedImageType.value?.withGrid()
            } else {
                selectedImageType.value = selectedImageType.value?.withoutGrid()
            }
        }

        switchGrid.isChecked = false


    }

    companion object {
        private val self = PlaceFragment()
        fun getInstance() = self
    }
}
