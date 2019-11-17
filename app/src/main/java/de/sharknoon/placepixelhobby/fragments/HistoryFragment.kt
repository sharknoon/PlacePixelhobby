package de.sharknoon.placepixelhobby.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.github.chrisbanes.photoview.PhotoView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper
import de.sharknoon.placepixelhobby.utils.setImageDrawableKeepZoom

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onResume() {
        super.onResume()
        initPhotoView()
        initSeekBar()
    }

    private var photoView: PhotoView? = null

    private fun initPhotoView() {
        val a = requireActivity()
        val photoView = a.findViewById<PhotoView>(R.id.photo_view_fragment_history)
        val drawable = a.resources.getDrawable(R.drawable.ic_place_00, a.theme)
        val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
        photoView.setImageDrawable(aliasingDrawableWrapper)
        photoView.maximumScale = 50F
        this.photoView = photoView

    }

    private fun initSeekBar() {
        val a = requireActivity()
        val seekBar = a.findViewById<SeekBar>(R.id.seek_bar_fragment_history)
        seekBar.progress = 0
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
                updatePhotoView(progress)

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun updatePhotoView(progress: Int) {
        val a = requireActivity()

        val safeNewId = progress.coerceIn(0..99)
        val stringId = if (safeNewId < 10) "0$safeNewId" else safeNewId.toString()
        val imageName = "ic_place_$stringId"

        val id = a.resources
            .getIdentifier(imageName, "drawable", a.packageName)
        val drawable = a.getDrawable(id) ?: return

        val aliasingDrawableWrapper = AliasingDrawableWrapper(drawable)
        val pv = photoView ?: return
        pv.setImageDrawableKeepZoom(aliasingDrawableWrapper)
    }

    companion object {
        private val self = HistoryFragment()
        fun getInstance() = self
    }


}
