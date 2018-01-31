package cct.ansteph.moodrecorder.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by loicstephan on 2017/06/12.
 */

public  class BitmapTransfomUtils {





    public static byte [] bitmaptoByte (Bitmap b)
    {
        int bytes = b.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);

        b.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,0,blob);


        return  blob.toByteArray();
    }
}
