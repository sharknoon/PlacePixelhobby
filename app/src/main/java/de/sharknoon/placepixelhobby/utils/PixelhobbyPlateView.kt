package de.sharknoon.placepixelhobby.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import de.sharknoon.placepixelhobby.model.PixelColor


class PixelhobbyPlateView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paintFill = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 6.dp
        style = Paint.Style.FILL
    }
    private val paintStroke = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 6.dp
        style = Paint.Style.STROKE
    }
    private val paintSmall = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 3.dp
        style = Paint.Style.STROKE
    }
    private val paintText = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 48F
        textAlign = Paint.Align.LEFT
    }
    private val offset = paintFill.strokeWidth / 2
    private var color: PixelColor? = null

    fun setColor(color: PixelColor) {
        val c = context.getColor(color.placeRGB)
        paintFill.color = c
        paintStroke.color = c
        paintSmall.color = c
        this.color = color
    }

    override fun onDraw(canvas: Canvas) {
        val plateSize = (300.dp - (2 * offset)).px.toInt()
        val amountColRow = 6
        val colRowSize = plateSize / amountColRow //50
        //In case of bright colors, draw dark text
        if (color == PixelColor.WHITE || color == PixelColor.LIGHT_GRAY) {
            paintText.color = Color.BLACK
        }
        //Rectangle
        canvas.drawRect(0F.offs, 0F.offs, plateSize.dp.offs, plateSize.dp.offs, paintStroke)
        //Vertical lines
        for (x in colRowSize..(plateSize - colRowSize) step colRowSize) {
            canvas.drawLine(x.dp.offs, 0F.offs, x.dp.offs, plateSize.dp.offs, paintFill)
        }
        //Horizontal lines
        for (y in colRowSize..(plateSize - colRowSize) step colRowSize) {
            canvas.drawLine(0F.offs, y.dp.offs, plateSize.dp.offs, y.dp.offs, paintFill)
        }
        //circle in the middle
        canvas.drawCircle(
            (plateSize / 2F).dp.offs,
            (plateSize / 2F).dp.offs,
            (colRowSize * 0.6).dp,
            paintFill
        )

        //draw the pixels itself
        val pixelSize = (colRowSize / 4F).dp
        val margin = (colRowSize.dp - (2F * pixelSize)) / 4F
        for (x in 0..(plateSize - colRowSize) step colRowSize) {
            for (y in 0..(plateSize - colRowSize) step colRowSize) {
                canvas.drawRect(
                    x.dp.offs + margin,
                    y.dp.offs + margin,
                    x.dp.offs + margin + pixelSize,
                    y.dp.offs + margin + pixelSize,
                    paintFill
                )
                canvas.drawLine(
                    x.dp.offs,
                    y.dp.offs + margin + (0.5F * pixelSize),
                    x.dp.offs + margin + (0.5F * pixelSize),
                    y.dp.offs + margin + (0.5F * pixelSize),
                    paintSmall
                )
                canvas.drawRect(
                    x.dp.offs + (colRowSize / 2).dp + margin,
                    y.dp.offs + margin,
                    x.dp.offs + (colRowSize / 2).dp + margin + pixelSize,
                    y.dp.offs + margin + pixelSize,
                    paintFill
                )
                canvas.drawLine(
                    x.dp.offs,
                    y.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    x.dp.offs + margin + (0.5F * pixelSize),
                    y.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    paintSmall
                )
                canvas.drawRect(
                    x.dp.offs + margin,
                    y.dp.offs + (colRowSize / 2).dp + margin,
                    x.dp.offs + margin + pixelSize,
                    y.dp.offs + (colRowSize / 2).dp + margin + pixelSize,
                    paintFill
                )
                canvas.drawLine(
                    x.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    y.dp.offs + margin + (0.5F * pixelSize),
                    x.dp.offs + colRowSize.dp,
                    y.dp.offs + margin + (0.5F * pixelSize),
                    paintSmall
                )
                canvas.drawRect(
                    x.dp.offs + (colRowSize / 2).dp + margin,
                    y.dp.offs + (colRowSize / 2).dp + margin,
                    x.dp.offs + (colRowSize / 2).dp + margin + pixelSize,
                    y.dp.offs + (colRowSize / 2).dp + margin + pixelSize,
                    paintFill
                )
                canvas.drawLine(
                    x.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    y.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    x.dp.offs + colRowSize.dp,
                    y.dp.offs + (3 * margin) + (1.5F * pixelSize),
                    paintSmall
                )
            }
        }
        //text in the middle
        canvas.getClipBounds(clipBoundsHolder)
        val cHeight = clipBoundsHolder.height()
        val cWidth = clipBoundsHolder.width()
        val text = color?.pixelhobbyCode?.toString() ?: "---"
        paintText.getTextBounds(text, 0, text.length, clipBoundsHolder)
        val x = cWidth / 2f - clipBoundsHolder.width() / 2f - clipBoundsHolder.left
        val y = cHeight / 2f + clipBoundsHolder.height() / 2f - clipBoundsHolder.bottom
        canvas.drawText(text, x, y, paintText)
    }

    companion object {
        private val clipBoundsHolder = Rect()
    }

    private var Number.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        )
        set(_) {}

    private var Number.px: Float
        get() = this.toFloat() / context.resources.displayMetrics.density
        set(_) {}

    private var Number.offs: Float
        get() = this.toFloat() + offset
        set(_) {}

}