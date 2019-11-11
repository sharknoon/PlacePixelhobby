package de.sharknoon.placepixelhobby.model

import de.sharknoon.placepixelhobby.R

enum class PixelColor(
    val displayName: Int,
    val pixelhobbyCode: Int,
    val pixelhobbyRGB: Int,
    val placeRGB: Int
) {
    WHITE(R.string.white, 100, R.color.pixelhobbyWhite, R.color.placeWhite),
    LIGHT_GRAY(R.string.light_gray, 411, R.color.pixelhobbyLightGray, R.color.placeLightGray),
    GRAY(R.string.gray, 172, R.color.pixelhobbyGray, R.color.placeGray),
    BLACK(R.string.black, 441, R.color.pixelhobbyBlack, R.color.placeBlack),
    PINK(R.string.pink, 103, R.color.pixelhobbyPink, R.color.placePink),
    RED(R.string.red, 155, R.color.pixelhobbyRed, R.color.placeRed),
    ORANGE(R.string.orange, 266, R.color.pixelhobbyOrange, R.color.placeOrange),
    BROWN(R.string.brown, 461, R.color.pixelhobbyBrown, R.color.placeBrown),
    YELLOW(R.string.yellow, 133, R.color.pixelhobbyYellow, R.color.placeYellow),
    LIGHT_GREEN(R.string.light_green, 246, R.color.pixelhobbyLightGreen, R.color.placeLightGreen),
    GREEN(R.string.green, 245, R.color.pixelhobbyGreen, R.color.placeGreen),
    AQUA_BLUE(R.string.aqua_blue, 469, R.color.pixelhobbyAquaBlue, R.color.placeAquaBlue),
    GREEN_BLUE(R.string.green_blue, 370, R.color.pixelhobbyGreenBlue, R.color.placeGreenBlue),
    BLUE(R.string.blue, 293, R.color.pixelhobbyBlue, R.color.placeBlue),
    VIOLET(R.string.violet, 442, R.color.pixelhobbyViolet, R.color.placeViolet),
    PURPLE(R.string.purple, 351, R.color.pixelhobbyPurple, R.color.placePurple);

    companion object {
        fun fromPlaceRGB(pixel: Int) = when (pixel) {
            -1 -> WHITE
            -1776412 -> LIGHT_GRAY
            -7829368 -> GRAY
            -14540254 -> BLACK
            -22575 -> PINK
            -1769472 -> RED
            -1731328 -> ORANGE
            -6264254 -> BROWN
            -1713920 -> YELLOW
            -7020476 -> LIGHT_GREEN
            -16597503 -> GREEN
            -16743481 -> AQUA_BLUE
            -16722979 -> GREEN_BLUE
            -16776982 -> BLUE
            -3182876 -> VIOLET
            -8257408 -> PURPLE
            else -> null
        }
    }
}