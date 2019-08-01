package com.mtr.steganography

import android.graphics.Bitmap
import com.mtr.steganography.BitmapUtils.Companion.brightestBlock
import com.mtr.steganography.BitmapUtils.Companion.get
import com.mtr.steganography.BitmapUtils.Companion.set
import com.mtr.steganography.Steganography.Companion.alterBit
import com.mtr.steganography.Steganography.Companion.binary
import com.mtr.steganography.Steganography.Companion.bits
import com.mtr.steganography.Steganography.Companion.formLinearSeq
import com.mtr.steganography.Steganography.Companion.largeBits
import com.mtr.steganography.Steganography.Companion.limit
import com.mtr.steganography.Steganography.Companion.scrambledStartBlockSize
import kotlin.IllegalArgumentException

internal class Encode {
    companion object{

        /**
         * Encodes data in random locations in bitmap
         * @param bitmap Source bitmap
         * @param text Data to be encoded
         */
        fun scrambled(bitmap: Bitmap, text: String) :Bitmap{
            val pixels = get(bitmap)

            /* TODO just use positions that fits into 31bits */
            if(pixels.size > limit) throw IllegalArgumentException("Bitmap is too big")

            /* Dividing pixels into blocks of 47 bits to fit a character and
               next character's position, removing the first one since 0 position
               is used for end character */
            val availablePixels = MutableList(pixels.size / (bits + largeBits)){
                IntRange(it * (bits + largeBits), (it+1) * (bits + largeBits))
            }.also { it.removeAt(0) }

            if(availablePixels.size - 1 < text.length) throw IllegalArgumentException("Bitmap is too small")

            var pivot = brightestBlock(pixels, bitmap.width, scrambledStartBlockSize).also { start->
                availablePixels.removeIf{it.contains(start)}
            }

            text.forEachIndexed { charIndex, char ->
                /* Using 0 as end of line position */
                val next = if(charIndex == text.length-1) 0 else availablePixel(availablePixels).first
                (binary(char, bits) + binary(next, largeBits)).forEachIndexed { index, c ->
                    pixels[index + pivot] = alterBit(pixels[index + pivot], c.toInt())
                }
                pivot = next
            }

            return set(bitmap, pixels)
        }


        /**
         * Encodes data in linear fashion
         * @param bitmap Source bitmap
         * @param text Data to be encoded
         */
        fun linear(bitmap: Bitmap, text: String) : Bitmap{

            if(text.length > limit) throw IllegalArgumentException("Message is too long")

            val pixels = get(bitmap)

            val linearSeq = formLinearSeq(text)

            if(pixels.size < linearSeq.length) throw IllegalArgumentException("Bitmap is too small")

            linearSeq.forEachIndexed { index, c ->
                pixels[index] = alterBit(pixels[index], c.toInt())
            }

            return set(bitmap, pixels)
        }

        private fun availablePixel(availablePixels : MutableList<IntRange>) =
                availablePixels.random().also { availablePixels.remove(it) }

    }
}