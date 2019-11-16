package de.sharknoon.placepixelhobby.alerts

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor
import java.util.*

fun showColorInformationAlert(activity: Activity, color: PixelColor) {
    val alertDialog = AlertDialog.Builder(activity)
        .setTitle(R.string.color_information)
        .setView(R.layout.alert_colorinformation)
        .setIcon(R.drawable.ic_info_light)
        .show()

    val textViewColor = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_colorinformation_color_value)
    val textViewName = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_colorinformation_color_name_value)
    val textViewPixelhobbyCode = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_colorinformation_pixelhobby_code_value)
    val textViewPixelhobbyRGB = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_colorinformation_pixelhobby_rgb_value)
    val textViewPlaceRGB = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_colorinformation_place_rgb_value)

    textViewColor.setBackgroundColor(activity.getColor(color.placeRGB))
    textViewName.text = activity.getString(color.displayName)
    textViewPixelhobbyCode.text = color.pixelhobbyCode.toString()
    textViewPixelhobbyRGB.text = ColorInt.toHexString(activity.getColor(color.pixelhobbyRGB))
    textViewPlaceRGB.text = ColorInt.toHexString(activity.getColor(color.placeRGB))
}

private object ColorInt {
    fun toHexString(color: Int): String {
        var hexString = Integer.toHexString(color)
        if (hexString.length > 6) hexString = hexString.substring(2)
        return "#${hexString.toUpperCase(Locale.getDefault())}"
    }
}