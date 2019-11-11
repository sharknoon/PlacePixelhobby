package de.sharknoon.placepixelhobby.alerts

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor

fun showColorInformationAlert(activity: Activity, color: PixelColor) {
    val alertDialog = AlertDialog.Builder(activity)
        .setTitle(R.string.color_information)
        .setView(R.layout.alert_colorinformation)
        .setIcon(R.drawable.ic_info)
        .show()

    val imageView = alertDialog
        .findViewById<View>(R.id.image_view_alert_colorinformation_color_display)
    imageView.setBackgroundColor(activity.getColor(color.placeRGB))
}
