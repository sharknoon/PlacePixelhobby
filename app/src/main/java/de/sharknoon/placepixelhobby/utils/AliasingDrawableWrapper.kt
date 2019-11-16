package de.sharknoon.placepixelhobby.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper

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