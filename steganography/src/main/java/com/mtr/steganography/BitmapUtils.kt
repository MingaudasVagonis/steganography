package com.mtr.steganography

import android.graphics.Bitmap
import android.graphics.Color

import com.mtr.steganography.Steganography.Companion.alterBit
import java.nio.IntBuffer

class BitmapUtils {
    companion object{

        /**
         * @param pixels Pixel array
         * @param params 0-> start pixel, 1-> block size, 2-> stride
         * @return A block of pixels as 1-dim array
         */
        private fun getBlock(pixels: IntArray, params: Array<Int>)=
            ArrayList<Int>().apply {
                (params[0] until params[0] + params[1]).takeWhile { it < pixels.size}.forEach{ x->
                    (0 until params[1]).takeWhile { x+ it * params[2] < pixels.size }.forEach {
                        this+=pixels[x+ it * params[2]]
                    }
                }
            }


        /**
         * Divides array of pixels into 900 pieces and returns the brightest one
         * @param pixels Array of pixels
         * @param stride Stride of the image
         * @param size Block size
         * @return position of the pixel which is the start of the darkest block
         */
        fun brightestBlock(pixels: IntArray, stride: Int, size: Int) : Int{

            fun blockLuminance(pos: Int) : Double {
                var luminance = 0.0
                getBlock(pixels, arrayOf(pos, size, stride)).forEach{
                    /* Setting the last bit to 0 so that result wouldn't change
                       after encoding */
                    luminance+=this.luminance(alterBit(it, 0))
                }
                return luminance / Math.pow(size.toDouble(), 2.0)
            }

            return ArrayList<Pair<Int,Double>>().apply {
                /* Dividing pixels into 1000 pieces */
                for (i in 0 until 1000) {
                    val pixel = (i * stride*stride * 0.001).toInt()
                    this += pixel to blockLuminance(pixel)
                }
                this.sortBy { (_, luminance) -> luminance }
                /* Removing those that wouldn't fit a character */
            }.filter { it.first < pixels.size - Steganography.scrambledLength }
                /* last() for brightest, first() for darkest */
             .last().first
        }

        fun get(bitmap: Bitmap) = IntArray(bitmap.width * bitmap.height).apply {
            bitmap.copyPixelsToBuffer(IntBuffer.wrap(this))
        }

        fun set(bitmap: Bitmap, pixels: IntArray) = bitmap.apply {
            copyPixelsFromBuffer(IntBuffer.wrap(pixels))
        }

        private fun luminance(pixel: Int) = 0.2126* Color.red(pixel) + 0.7152* Color.green(pixel) + 0.0722* Color.blue(pixel)


    }
}