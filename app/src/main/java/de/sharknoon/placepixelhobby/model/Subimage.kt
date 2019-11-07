package de.sharknoon.placepixelhobby.model

import android.content.Context
import android.graphics.drawable.Drawable
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper

data class Subimage(
    val image: Drawable,
    val id: Int,
    val name: String,
    val number: Int,
    val x: Int,
    val y: Int
) {

    companion object {
        private val IMAGE_CACHE = mutableMapOf<Int, Subimage>()

        fun fromID(context: Context, id: Int): Subimage {
            //Checking the cache
            IMAGE_CACHE[id]?.also { return it }
            //Getting the drawable, name and properties of the subimage
            val drawable = context.getDrawable(id)
                ?: throw IllegalArgumentException("Drawable ID $id not found!")
            val name = context.resources.getResourceEntryName(id)
            val subNames = name.split('_')
            val subimage = Subimage(
                AliasingDrawableWrapper(drawable),
                id,
                name,
                subNames[1].toInt(),
                subNames[2].toInt(),
                subNames[3].toInt()
            )
            IMAGE_CACHE[id] = subimage
            return subimage
        }

    }
}