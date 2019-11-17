package de.sharknoon.placepixelhobby.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor
import de.sharknoon.placepixelhobby.model.Subimage
import java.util.stream.Collectors


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

fun Fragment.replaceChildFragment(container: Int, newFragment: Fragment) {
    this.childFragmentManager.beginTransaction().apply {
        replace(container, newFragment)
        commit()
    }
}

fun FragmentActivity.replaceChildFragment(container: Int, newFragment: Fragment) {
    this.supportFragmentManager.beginTransaction().apply {
        replace(container, newFragment)
        commit()
    }
}

fun countColors(images: Collection<Subimage>): MutableMap<PixelColor, Int> =
    images.stream()
        .map(::countColors)
        .flatMap { it.entries.stream() }
        .collect(
            Collectors.groupingBy(
                Map.Entry<PixelColor, Int>::key,
                Collectors.summingInt(Map.Entry<PixelColor, Int>::value)
            )
        )


fun countColors(subimage: Subimage): Map<PixelColor, Int> {
    val tag = "Utils"
    val colorMap = mutableMapOf<Int, Int>()
    val bitmap = subimage.image.toBitmap()

    for (x in 0 until bitmap.width) {
        for (y in 0 until bitmap.height) {
            val color = bitmap.getPixel(x, y)
            val currentAmountColors = colorMap.getOrPut(color, { 0 })
            colorMap[color] = currentAmountColors + 1
        }
    }

    val pixelColorMap = mutableMapOf<PixelColor, Int>()

    for ((c, a) in colorMap) {
        val pixelColor = PixelColor.fromPlaceRGB(c)
        if (pixelColor != null) {
            pixelColorMap[pixelColor] = a
        } else {
            Log.w(tag, "Unknown color $c")
        }
    }

    return pixelColorMap
}

fun Drawable.toBitmap(): Bitmap {
    if (this is DrawableWrapper) {
        val drawable = this.drawable
        if (drawable != null && drawable is BitmapDrawable) {
            return drawable.bitmap
        }
    }

    if (this is BitmapDrawable) {
        val bitmapDrawable = this
        if (bitmapDrawable.bitmap != null) {
            return bitmapDrawable.bitmap
        }
    }

    val bitmap = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            this.intrinsicWidth,
            this.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}