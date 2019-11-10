package de.sharknoon.placepixelhobby

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.model.SubimageExtensions.favorite

open class SubimageFavoriteAdapter(context: Context, private val data: MutableList<Subimage>) :
    RecyclerView.Adapter<SubimageFavoriteAdapter.SubimageFavoriteViewHolder>() {


    private val inflater = LayoutInflater.from(context)
    private var subimageClickListener: (view: View, item: Subimage) -> Unit = { _, _ -> }
    private var favoriteClickListener: (button: Button, item: Subimage) -> Unit = { _, _ -> }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubimageFavoriteViewHolder {
        val view = inflater.inflate(R.layout.view_holder_image_favorite, parent, false)
        return SubimageFavoriteViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: SubimageFavoriteViewHolder, position: Int) {
        val subimage = data[position]
        val subtext = "${subimage.number} (${subimage.x}|${subimage.y})"
        val subfavorite = if (subimage.favorite) R.drawable.ic_star else R.drawable.ic_star_empty
        holder.textViewSubimageName.text = subtext
        holder.imageViewSubimage.setImageDrawable(subimage.image)
        holder.buttonFavorite.setBackgroundResource(subfavorite)
    }


    /**
     * Returns the total amount of items
     */
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class SubimageFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewSubimageName =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_image_favorite)
        internal var imageViewSubimage =
            itemView.findViewById<ImageView>(R.id.image_view_view_holder_image_favorite)
        internal val buttonFavorite =
            itemView.findViewById<Button>(R.id.button_view_holder_image_favorite)

        init {
            itemView.setOnClickListener {
                subimageClickListener(it, getItem(adapterPosition))
            }

            buttonFavorite?.setOnClickListener {
                favoriteClickListener(buttonFavorite, getItem(adapterPosition))
            }
        }

    }

    private fun getItem(position: Int): Subimage {
        return data[position]
    }

    fun addItem(subimage: Subimage) {
        //sorting the images
        var position = data.size
        for (i in data.indices) {
            if (data[i].number > subimage.number) {
                position = i
                break
            }
        }
        data.add(position, subimage)
        notifyItemInserted(position)
    }

    fun removeItem(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    fun setFavorite(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data[index].favorite = true
        notifyItemChanged(index)
    }

    fun removeFavorite(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data[index].favorite = false
        notifyItemChanged(index)
    }

    // allows clicks events to be caught
    fun setSubimageClickListener(subimageClickListener: (view: View, item: Subimage) -> Unit) {
        this.subimageClickListener = subimageClickListener
    }

    fun setFavoriteClickListener(favoriteClickListener: (button: Button, item: Subimage) -> Unit) {
        this.favoriteClickListener = favoriteClickListener
    }

}