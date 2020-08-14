package de.sharknoon.placepixelhobby.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.Subimage
import de.sharknoon.placepixelhobby.model.SubimageExtensions.selected

open class SubimageSelectableAdapter(context: Context, private val data: MutableList<Subimage>) :
    RecyclerView.Adapter<SubimageSelectableAdapter.SubimageSelectableViewHolder>() {


    private val inflater = LayoutInflater.from(context)
    private var subimageClickListener: (view: View, item: Subimage) -> Unit = { _, _ -> }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubimageSelectableViewHolder {
        val view = inflater.inflate(R.layout.view_holder_image_selection, parent, false)
        return SubimageSelectableViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: SubimageSelectableViewHolder, position: Int) {
        val subimage = data[position]
        val subtext = "${subimage.number} (${subimage.x}|${subimage.y})"

        holder.textViewSubimageName.text = subtext
        holder.imageViewSubimage.setImageDrawable(subimage.image)

        if (subimage.selected) {
            holder.imageViewSelected.setImageResource(R.drawable.ic_check)
            holder.imageViewSubimage.alpha = 0.5F
        } else {
            holder.imageViewSelected.setImageResource(R.color.colorTransparent)
            holder.imageViewSubimage.alpha = 1F
        }
    }


    /**
     * Returns the total amount of items
     */
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class SubimageSelectableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewSubimageName =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_image_selection_name)
        internal var imageViewSubimage =
            itemView.findViewById<ImageView>(R.id.image_view_view_holder_image_selection_image)
        internal val imageViewSelected =
            itemView.findViewById<ImageView>(R.id.image_view_view_holder_image_selection_selected)

        init {
            itemView.setOnClickListener {
                subimageClickListener(it, getItem(adapterPosition))
            }
        }

    }

    private fun getItem(position: Int): Subimage {
        return data[position]
    }

    fun setSelected(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data[index].selected = true
        notifyItemChanged(index)
    }

    fun setUnselected(subimage: Subimage) {
        val index = data.indexOf(subimage)
        data[index].selected = false
        notifyItemChanged(index)
    }

    // allows clicks events to be caught
    fun setSubimageClickListener(subimageClickListener: (view: View, item: Subimage) -> Unit) {
        this.subimageClickListener = subimageClickListener
    }

}