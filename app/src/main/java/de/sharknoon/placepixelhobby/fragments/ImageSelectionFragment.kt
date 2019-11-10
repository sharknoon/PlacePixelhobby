package de.sharknoon.placepixelhobby.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.SubimageSelectableAdapter
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.model.SubimageExtensions.selected
import de.sharknoon.placepixelhobby.utils.getAllSubimages

class ImageSelectionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_selection, container, false)
    }


    override fun onResume() {
        super.onResume()
        initImagesRecyclerView()
    }

    private var imagesRVAdapter: SubimageSelectableAdapter? = null

    private fun initImagesRecyclerView() {
        val view = view ?: return

        val subImages = getAllSubimages(view.context, false)

        // set up the RecyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recycler_view_fragment_image_selection)
        recyclerView?.layoutManager = GridLayoutManager(view.context, 4)
        val adapter = SubimageSelectableAdapter(view.context, subImages)
        adapter.setSubimageClickListener { _, subimage ->
            toggleImageSelection(subimage)
        }

        recyclerView?.adapter = adapter

        imagesRVAdapter = adapter
    }

    private fun toggleImageSelection(item: Subimage) {

        //Checking for turning off or on the favorite
        //Turning on
        if (item.selected) {
            imagesRVAdapter?.setUnselected(item)
            //Turning off
        } else {
            imagesRVAdapter?.setSelected(item)
        }
    }


    companion object {
        private val self = ImageSelectionFragment()
        fun getInstance() = self
    }

}
