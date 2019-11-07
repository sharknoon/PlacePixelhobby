package de.sharknoon.placepixelhobby.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.Subimage

class AliasingDrawableWrapper(wrapped: Drawable) : DrawableWrapper(wrapped) {

    override fun draw(canvas: Canvas) {
        val oldDrawFilter = canvas.drawFilter
        canvas.drawFilter = DRAW_FILTER
        super.draw(canvas)
        canvas.drawFilter = oldDrawFilter
    }

    companion object {
        private val DRAW_FILTER = PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0)
    }
}

private object Subimages {
    var originalSubimages: MutableList<Subimage>? = null
    var detailedSubimages: MutableList<Subimage>? = null
}

fun getAllSubimages(context: Context, detailed: Boolean): MutableList<Subimage> {
    if (detailed) {
        Subimages.detailedSubimages?.also { return it }
    } else {
        Subimages.originalSubimages?.also { return it }
    }
    val arrayId = if (detailed) R.array.subimages_detailed else R.array.subimages_original
    return runCatching {
        val arrayList = context.resources.obtainTypedArray(arrayId)
        val list = mutableListOf<Subimage>()
        for (i in 0 until arrayList.length()) {
            val subimage = Subimage.fromID(context, arrayList.getResourceId(i, -1))
            list.add(subimage)
        }
        arrayList.recycle()
        if (detailed) {
            Subimages.detailedSubimages = list
        } else {
            Subimages.originalSubimages = list
        }
        list
    }.getOrDefault(mutableListOf())
}