package de.sharknoon.placepixelhobby.logic

object Cutting {

    fun cut(amountPixels: Int): Array<BooleanArray> {
        val n = 12
        val set = Array(n) { BooleanArray(n) }
        val amountRestPixels = amountPixels % 140
        var pixelCounter = amountRestPixels

        for (x in 0 until n) {
            for (y in 0 until n) {
                //Abort when all pixels are set
                if (pixelCounter < 1) return set
                //Circle in the middle
                if (x in 5..6 && y in 5..6) {
                    set[x][y] = true
                    //continue without reducing the counter
                    continue
                }
                //If one pixel is left and the amount of pixels is even
                if (pixelCounter == 1 && amountRestPixels % 2 == 0) {
                    set[x][n - 1] = true
                } else {
                    set[x][y] = true
                }
                pixelCounter--
            }
        }

        return set
    }

}