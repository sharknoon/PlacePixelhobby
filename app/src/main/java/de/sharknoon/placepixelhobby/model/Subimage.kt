package de.sharknoon.placepixelhobby.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import de.sharknoon.placepixelhobby.utils.AliasingDrawableWrapper


class Subimage private constructor(
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
            //Getting the drawable, displayName and properties of the subimage
            val drawable = ResourcesCompat.getDrawable(context.resources, id, context.theme)
                ?: throw IllegalArgumentException("Drawable ID $id not found!")
            val name = context.resources.getResourceEntryName(id)
            val subNames = name.split('_')
            val subimage = Subimage(
                AliasingDrawableWrapper(drawable),
                id,
                name,
                subNames[1].toIntOrNull() ?: -1,
                subNames[2].toIntOrNull() ?: -1,
                subNames[3].toIntOrNull() ?: -1
            )
            IMAGE_CACHE[id] = subimage
            return subimage
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subimage

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}