package de.sharknoon.placepixelhobby.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor

open class ColorAdapter(context: Context, private val data: List<Pair<PixelColor, Int>>) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {


    private val inflater = LayoutInflater.from(context)
    private var colorInfoClickListener: (color: PixelColor) -> Unit = { _ -> }
    private var buttonCutClickListener: (color: PixelColor, amountPixels: Int) -> Unit =
        { _, _ -> }

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
        val r = inflater.context.resources
        val amountColorsquares = amount / 140
        val amountRestPixels = amount % 140

        holder.getImageViewColorDisplay().setBackgroundColor(
            r.getColor(
                color.placeRGB,
                null
            )
        )
        holder.getTextViewColorNameAndNumber().text = r.getString(
            R.string.color_subtitle,
            r.getString(color.displayName),
            color.pixelhobbyCode
        )
        holder.getTextViewAmountPixels().text =
            if (amountColorsquares > 0 && amountRestPixels > 0) {
                r.getString(
                    R.string.new_line,
                    r.getQuantityString(R.plurals.x_pixels, amount, amount),
                    r.getString(
                        R.string.x_plus_y,
                        r.getQuantityString(
                            R.plurals.x_squares,
                            amountColorsquares,
                            amountColorsquares
                        ),
                        r.getQuantityString(R.plurals.x_pixels, amountRestPixels, amountRestPixels)
                    )
                )
            } else if (amountColorsquares < 1) {
                r.getQuantityString(R.plurals.x_pixels, amount, amount)
            } else if (amountColorsquares > 0) {
                r.getString(
                    R.string.new_line,
                    r.getQuantityString(R.plurals.x_pixels, amount, amount),
                    r.getQuantityString(R.plurals.x_squares, amountColorsquares, amountColorsquares)
                )
            } else {
                "ERROR SHOULDN'T HAPPEN :("
            }
    }


    /**
     * Returns the total amount of items
     */
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageViewColorDisplay =
            itemView.findViewById<View>(R.id.view_view_holder_color_display)
        private val imageViewColorInfo =
            itemView.findViewById<ImageView>(R.id.image_view_view_holder_color_info)
        private val textViewColorNameAndNumber =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_color_name_and_number)
        private val textViewAmountPixels =
            itemView.findViewById<TextView>(R.id.text_view_view_holder_color_amount_pixels)
        private val buttonCut =
            itemView.findViewById<ImageButton>(R.id.button_view_holder_color_cut)

        init {
            imageViewColorInfo.setOnClickListener {
                colorInfoClickListener(getItem(adapterPosition).first)
            }
            buttonCut.setOnClickListener {
                val (pixelColor, amount) = getItem(adapterPosition)
                buttonCutClickListener(pixelColor, amount)
            }
        }

        fun getImageViewColorDisplay(): View = imageViewColorDisplay
        fun getTextViewColorNameAndNumber(): TextView = textViewColorNameAndNumber
        fun getTextViewAmountPixels(): TextView = textViewAmountPixels
    }

    private fun getItem(position: Int): Pair<PixelColor, Int> {
        return data[position]
    }

    // allows clicks events to be caught
    fun setColorInfoClickListener(colorInfoClickListener: (color: PixelColor) -> Unit) {
        this.colorInfoClickListener = colorInfoClickListener
    }

    fun setButtonCutClickListener(buttonCutClickListener: (color: PixelColor, amount: Int) -> Unit) {
        this.buttonCutClickListener = buttonCutClickListener
    }

}