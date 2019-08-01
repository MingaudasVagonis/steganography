package com.mtr.steganography

import android.graphics.Bitmap
import com.mtr.steganography.Decode.Companion.linear
import com.mtr.steganography.Encode.Companion.linear
import com.mtr.steganography.Encode.Companion.scrambled
import com.mtr.steganography.Decode.Companion.scrambled
import kotlin.reflect.KFunction

class Steganography {
    companion object{

        fun encode(bitmap: Bitmap, text: String, type: EncodeTypes) : Bitmap{
            return type.func.call(Encode.Companion, bitmap, text)
        }

        fun decode(bitmap: Bitmap, type: DecodeTypes): String{
            return type.func.call(Decode.Companion, bitmap)
        }

        /**
         * Changes the last bit to either 1 or 0
         * @param num Value to be changed
         * @param to 0 or 1
         * @return Changed integer
         */
        fun alterBit(num: Int, to:Int) = (num and -2) or (to and 1)

        /**
         * Getting the last bit of the integer
         * @param num Value to be evaluated
         * @return 0 or 1
         */
        fun lastBit(num: Int) = num and 1

        /**
         * Joins values of the array into a string and parses it into integer
         * @param pixels array with 0s or 1s
         * @return Parsed integer value
         */
        fun readPixels(pixels: IntArray) :Int = pixels.map { lastBit(it) }.joinToString("").toInt(2)

        fun formLinearSeq(text: String) : String = binary(text.length+1, largeBits) + text.map { binary(it, bits) }.joinToString("")

        /**
         * Getting the binary representation of a char or int in string format
         * @param c Value to be converted
         * @param n How many bits needed
         * @return String representation of the binary value
         */
        fun binary(c: Char, n: Int) = toNbit(c.toInt().toString(2), n)

        fun binary(c: Int, n: Int) = toNbit(c.toString(2), n)

        private fun toNbit(bit_str: String, n: Int) = "0".repeat(n - bit_str.length) + bit_str

        /* Bits used in linear encoding and for character representation */
        const val bits = 16

        /* Bits used to represent position in scrambled encoding */
        const val largeBits = 31

        /* Block size used to evaluate brightness of the bitmap */
        const val scrambledStartBlockSize = 5

        const val scrambledLength = bits + largeBits + 1

        /* Limit to fit into 31bits */
        val limit = Math.pow(2.0, largeBits.toDouble()).toInt() - 1

    }

}

enum class EncodeTypes(val func: KFunction<Bitmap>) {
    LINEAR(::linear),
    SCRAMBLED(::scrambled)
}

enum class DecodeTypes(val func: KFunction<String>){
    LINEAR(::linear),
    SCRAMBLED(::scrambled)
}
