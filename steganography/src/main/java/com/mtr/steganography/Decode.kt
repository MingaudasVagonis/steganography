package com.mtr.steganography

import android.graphics.Bitmap
import com.mtr.steganography.BitmapUtils.Companion.brightestBlock
import com.mtr.steganography.BitmapUtils.Companion.get

import com.mtr.steganography.Steganography.Companion.bits
import com.mtr.steganography.Steganography.Companion.largeBits
import com.mtr.steganography.Steganography.Companion.readPixels
import com.mtr.steganography.Steganography.Companion.scrambledStartBlockSize

internal class Decode {
    companion object{
        /**
         * Decodes sequential bits of data
         * @param bitmap Source bitmap
         * @return Decoded data
         */
        fun linear(bitmap: Bitmap): String{

            var string = ""

            val pixels = get(bitmap)

            val length = readPixels(pixels.sliceArray(0 until largeBits))

            for (x in largeBits..length*bits step bits)
                string+= readPixels(pixels.sliceArray(x until x+bits)).toChar()

            return string
        }
        /**
         * Decodes scrambled bits of data
         * @param bitmap Source bitmap
         * @return Decoded data
         */
        fun scrambled(bitmap: Bitmap) : String{
            val pixels = get(bitmap)
            /* Using brightest block of the bitmap as a starting point */
            val start = brightestBlock(pixels, bitmap.width, scrambledStartBlockSize)

            var string = ""

            /**
             *  Reads character + next position at given position
             *  @param pos Start position
             */
            fun readScrambled(pos: Int){
                string+= readPixels(pixels.sliceArray(pos until pos + bits)).toChar()
                /* Position of the next character */
                val next = readPixels(pixels.sliceArray(pos + bits until pos + bits + largeBits))
                if(next != 0)
                    readScrambled(next)
            }

            readScrambled(start)

            return string
        }
    }
}