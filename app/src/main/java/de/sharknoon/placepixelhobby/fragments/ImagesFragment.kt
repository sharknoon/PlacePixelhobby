package de.sharknoon.placepixelhobby.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.activities.ImageActivity
import de.sharknoon.placepixelhobby.adapters.SubimageFavoriteAdapter
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.model.SubimageExtensions.favorite
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

    override fun onResume() {
        super.onResume()
        initSharedPreferences()
        initFavoriteImagesRecyclerView()
        initAllImagesRecyclerView()
    }

    private var favoriteImagePrefs: SharedPreferences? = null
    private var favoriteImagesRVAdapter: SubimageFavoriteAdapter? = null
    private var allImagesRVAdapter: SubimageFavoriteAdapter? = null

    private fun initSharedPreferences() {
        favoriteImagePrefs = this.activity?.getSharedPreferences(
            "subimage_favorites",
            Context.MODE_PRIVATE
        )
    }

    private fun initFavoriteImagesRecyclerView() {
        val view = view ?: return

        val subImages = getFavoriteImages()

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recycler_view_fragment_images_favorite_subimages)
        val adapter =
            SubimageFavoriteAdapter(view.context, subImages, R.layout.view_holder_image)
        adapter.setSubimageClickListener(this@ImagesFragment::openImageActivity)

        adapter.setFavoriteClickListener(this@ImagesFragment::toggleImageFavorite)

        recyclerView?.adapter = adapter

        favoriteImagesRVAdapter = adapter

        updateTextViewFavorites()
    }

    private fun initAllImagesRecyclerView() {
        val view = view ?: return

        val subImages = getAllSubimages(view.context, false)

        // set up the RecyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.recycler_view_fragment_images_all_subimages)
        val adapter =
            SubimageFavoriteAdapter(view.context, subImages, R.layout.view_holder_image)
        adapter.setSubimageClickListener(this@ImagesFragment::openImageActivity)

        adapter.setFavoriteClickListener(this@ImagesFragment::toggleImageFavorite)
        recyclerView?.adapter = adapter

        allImagesRVAdapter = adapter
    }

    private fun openImageActivity(subimage: Subimage) {
        val intent = Intent(view?.context, ImageActivity::class.java)
        addTitleInformations(subimage, intent)
        view?.context?.startActivity(intent)
    }

    private fun addTitleInformations(subimage: Subimage, intent: Intent) {
        intent.putExtra("originalImageName", subimage.name)
        intent.putExtra("imageName", "${subimage.name}_detailed")
        intent.putExtra("imageNo", subimage.number)
        intent.putExtra("imageX", subimage.x)
        intent.putExtra("imageY", subimage.y)
    }

    private fun toggleImageFavorite(item: Subimage) {
        val prefs = favoriteImagePrefs ?: return

        //Checking for turning off or on the favorite
        //Turning on
        if (prefs.contains(item.id.toString())) {
            //Remove it from the shared prefs
            prefs.edit().remove(item.id.toString()).apply()
            //Update the Recyclerview
            favoriteImagesRVAdapter?.removeItem(
                Subimage.fromID(requireContext(), item.id)
            )
            allImagesRVAdapter?.removeFavorite(item)
            //Turning off
        } else {
            //Add it to the shared prefs
            prefs.edit().putString(item.id.toString(), item.name).apply()
            //Update the Recyclerview
            favoriteImagesRVAdapter?.addItem(
                Subimage.fromID(requireContext(), item.id)
            )
            favoriteImagesRVAdapter?.setFavorite(item)
            allImagesRVAdapter?.setFavorite(item)
        }

        updateTextViewFavorites()
    }

    private fun updateTextViewFavorites() {
        val prefs = favoriteImagePrefs ?: return

        //If the favorites-list is empty, show a text field
        val textView =
            requireActivity().findViewById<TextView>(R.id.text_view_fragment_images_no_favourites)
        textView.visibility = if (prefs.all.isEmpty()) VISIBLE else GONE
    }

    private fun getFavoriteImages(): MutableList<Subimage> {
        val prefs = favoriteImagePrefs ?: return mutableListOf()

        return prefs
            .all
            .keys
            .stream()
            .map { it.toIntOrNull() }
            .filter { it != null }
            .map { Subimage.fromID(requireContext(), it ?: -1) }
            .peek { it.favorite = true }
            .sorted { s1, s2 -> s1.number.compareTo(s2.number) }
            .collect(Collectors.toList())
    }

    companion object {
        private val self = ImagesFragment()
        fun getInstance() = self
    }

}

