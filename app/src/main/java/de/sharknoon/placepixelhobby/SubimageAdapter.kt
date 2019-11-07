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

class SubimageAdapter(context: Context, private val data: MutableList<Subimage>) :
    RecyclerView.Adapter<SubimageAdapter.SubimageViewHolder>() {


    private val inflater = LayoutInflater.from(context)
    private var subimageClickListener: (view: View, item: Subimage) -> Unit = { _, _ -> }
    private var favoriteClickListener: (view: View, item: Subimage) -> Unit = { _, _ -> }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubimageViewHolder {
        val view = inflater.inflate(R.layout.image_recyclerview, parent, false)
        return SubimageViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: SubimageViewHolder, position: Int) {
        val subimage = data[position]
        val subtext = "${subimage.number} (${subimage.x}|${subimage.y})"
        holder.textViewSubimageName.text = subtext
        holder.imageViewSubimage.setImageDrawable(subimage.image)
    }

    /**
     * Returns the total amount of items
     */
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class SubimageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewSubimageName =
            itemView.findViewById<TextView>(R.id.recyclerViewItemTextView)
        internal var imageViewSubimage =
            itemView.findViewById<ImageView>(R.id.recyclerViewItemImageView)
        internal val buttonFavorite =
            itemView.findViewById<Button>(R.id.recyclerViewItemButton)

        init {
            itemView.setOnClickListener {
                subimageClickListener(it, getItem(adapterPosition))
            }

            buttonFavorite?.setOnClickListener {
                favoriteClickListener(it, getItem(adapterPosition))
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

    fun remoteItem(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    // allows clicks events to be caught
    fun setSubimageClickListener(subimageClickListener: (view: View, item: Subimage) -> Unit) {
        this.subimageClickListener = subimageClickListener
    }

    fun setFavoriteClickListener(favoriteClickListener: (view: View, item: Subimage) -> Unit) {
        this.favoriteClickListener = favoriteClickListener
    }

}