package com.mtr.steganography;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 * Linear and scrambled encoding / decoding tests
 */
@RunWith(AndroidJUnit4.class)
public class SteganographyTests {

    private final String data = "dfguwykbnawd awiyud!!ekfb87392 xx šššš";

    @Test
    public void linear() {
        Bitmap bitmap = createTestBitmap();
        bitmap = Steganography.Companion.encode(bitmap, data, EncodeTypes.LINEAR);
        String result = Steganography.Companion.decode(bitmap, DecodeTypes.LINEAR);
        assertEquals(data, result);
    }

    @Test
    public void scrambled(){
        Bitmap bitmap = createTestBitmap();
        bitmap = Steganography.Companion.encode(bitmap, data, EncodeTypes.SCRAMBLED);
        String result = Steganography.Companion.decode(bitmap, DecodeTypes.SCRAMBLED);
        assertEquals(data, result);
    }

    private Bitmap createTestBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        canvas.drawColor(Color.RED);
        canvas.drawRect(500f, 600f, 800f, 800f, paint);
        return bitmap;
    }


}
