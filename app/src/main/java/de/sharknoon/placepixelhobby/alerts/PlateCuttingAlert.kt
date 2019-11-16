package de.sharknoon.placepixelhobby.alerts

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import de.sharknoon.placepixelhobby.R
import de.sharknoon.placepixelhobby.model.PixelColor
import de.sharknoon.placepixelhobby.utils.PixelhobbyPlateView


fun showPlateCuttingAlert(activity: Activity, amountPixels: Int, color: PixelColor) {
    val darkTheme = color == PixelColor.WHITE || color == PixelColor.LIGHT_GRAY
    val theme = if (darkTheme) R.style.DarkDialogTheme else R.style.LightDialogTheme
    val icon = if (darkTheme) R.drawable.ic_info_dark else R.drawable.ic_info_light

    val alertDialog = AlertDialog.Builder(activity, theme)
        .setTitle(R.string.cut_information)
        .setView(R.layout.alert_cutinformation)
        .setIcon(icon)
        .show()

    val textViewAmountPlates = alertDialog
        .findViewById<TextView>(R.id.text_view_alert_cutinformation_amount_plates)
    val pixelhobbyPlateView = alertDialog
        .findViewById<PixelhobbyPlateView>(R.id.pixelhobby_plate_view_alert_cutinformation)

    val amountPlates = amountPixels / 140

    textViewAmountPlates.text = activity.getString(R.string.x_full_plates_plus, amountPlates)
    pixelhobbyPlateView.setColor(color)

}

