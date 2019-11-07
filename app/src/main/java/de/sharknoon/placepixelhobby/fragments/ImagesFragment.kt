package de.sharknoon.placepixelhobby.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.SubimageAdapter
import de.sharknoon.placepixelhobby.activities.ImageActivity
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.utils.getAllSubimages
import java.util.stream.Collectors


class ImagesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onStart() {
        super.onStart()
        initSharedPreferences()
        initFavouriteImagesRecyclerView()
        initAllImagesRecyclerView()
    }

    private var favouriteImagePrefs: SharedPreferences? = null

    private fun initSharedPreferences() {
        favouriteImagePrefs = this.activity?.getSharedPreferences(
            "subimage_favourites",
            Context.MODE_PRIVATE
        )
    }

    private fun initFavouriteImagesRecyclerView() {
        val view = view ?: return
        val favouriteImagePrefs = favouriteImagePrefs ?: return

        val subImages = getFavouriteImages()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFavouriteSubimages)
        recyclerView?.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = SubimageAdapter(view.context, subImages)
        adapter.setSubimageClickListener { _, subimage ->
            openImageActivity("${subimage.name}_detailed")
        }

        adapter.setFavouriteClickListener { _, subimage ->
            toggleImageFavourite(subimage)
        }

        recyclerView?.adapter = adapter

        //Don't question it, I need to store a instance of the listener
        val listener = { sharedPreferences: SharedPreferences, key: String ->
            when {
                //added or changed
                sharedPreferences.contains(key) -> {
                    adapter.addItem(
                        Subimage.fromID(requireContext(), key.toIntOrNull() ?: -1)
                    )
                }
                //removed
                !sharedPreferences.contains(key) -> {
                    adapter.remoteItem(
                        Subimage.fromID(requireContext(), key.toIntOrNull() ?: -1)
                    )
                }
            }
        }
        favouriteImagePrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun initAllImagesRecyclerView() {
        val view = view ?: return

        val subImages = getAllSubimages(view.context, false)

        // set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvAllSubimages)
        recyclerView?.layoutManager = GridLayoutManager(view.context, 4)
        val adapter = SubimageAdapter(view.context, subImages)
        adapter.setSubimageClickListener { _, subimage ->
            openImageActivity("${subimage.name}_detailed")
        }

        adapter.setFavouriteClickListener { _, subimage ->
            toggleImageFavourite(subimage)
        }
        recyclerView?.adapter = adapter
    }

    private fun openImageActivity(imageName: String) {
        val myIntent = Intent(view?.context, ImageActivity::class.java)
        myIntent.putExtra("imageName", imageName)
        view?.context?.startActivity(myIntent)
    }

    private fun toggleImageFavourite(item: Subimage) {
        val prefs = favouriteImagePrefs ?: return

        if (prefs.contains(item.id.toString())) {
            prefs.edit().remove(item.id.toString()).apply()
        } else {
            prefs.edit().putString(item.id.toString(), item.name).apply()
        }
    }

    private fun getFavouriteImages(): MutableList<Subimage> {
        val prefs = favouriteImagePrefs ?: return mutableListOf()

        return prefs
            .all
            .keys
            .stream()
            .map { Subimage.fromID(requireContext(), it.toIntOrNull() ?: -1) }
            .sorted { s1, s2 -> s1.number.compareTo(s2.number) }
            .collect(Collectors.toList())
    }

    companion object {
        private val self = ImagesFragment()
        fun getInstance() = self
    }

}

