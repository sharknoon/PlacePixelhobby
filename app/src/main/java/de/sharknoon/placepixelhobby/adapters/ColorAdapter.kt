package de.sharknoon.placepixelhobby.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor

open class ColorAdapter(context: Context, private val data: List<Pair<PixelColor, Int>>) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {


    private val inflater = LayoutInflater.from(context)
    private var colorInfoClickListener: (view: View, color: PixelColor) -> Unit = { _, _ -> }
    private var buttonCutClickListener: (view: View, color: PixelColor, amountPixels: Int) -> Unit =
        { _, _, _ -> }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorViewHolder {
        val view = inflater.inflate(R.layout.view_holder_color, parent, false)
        return ColorViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val (color, amount) = data[position]
        val c = inflater.context
        val amountColorsquares = amount.toDouble() / 140.0

        holder.imageViewColorDisplay.setBackgroundColor(
            c.resources.getColor(
                color.placeRGB,
                null
            )
        )
        holder.textViewColorNameAndNumber.text = c.getString(
            R.string.color_subtitle,
            c.getString(color.displayName),
            color.pixelhobbyCode
        )
        holder.textViewAmountPixels.text = c.getString(
            R.string.amount_pixels,
            amount,
            amountColorsquares
        )
    }


    /**
     * Returns the total amount of items
     */
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val imageViewColorDisplay =
            itemView.findViewById<View>(R.id.view_view_holder_color_display)
        internal val imageViewColorInfo =
            itemView.findViewById<ImageView>(R.id.image_view_view_holder_color_info)
        internal val textViewColorNameAndNumber =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_color_name_and_number)
        internal val textViewAmountPixels =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_color_amount_pixels)
        internal val buttonCut =
            itemView.findViewById<ImageButton>(R.id.button_view_holder_color_cut)

        init {
            imageViewColorInfo.setOnClickListener {
                colorInfoClickListener(it, getItem(adapterPosition).first)
            }
            buttonCut.setOnClickListener {
                val (pixelColor, amount) = getItem(adapterPosition)
                buttonCutClickListener(it, pixelColor, amount)
            }
        }

    }

    private fun getItem(position: Int): Pair<PixelColor, Int> {
        return data[position]
    }

    // allows clicks events to be caught
    fun setColorInfoClickListener(colorInfoClickListener: (view: View, color: PixelColor) -> Unit) {
        this.colorInfoClickListener = colorInfoClickListener
    }

    fun setButtonCutClickListener(buttonCutClickListener: (view: View, color: PixelColor, amount: Int) -> Unit) {
        this.buttonCutClickListener = buttonCutClickListener
    }

}