package io.square1.tools.http.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by roberto on 13/11/14.
 */
public class BitmapDataReader implements DataReader<Bitmap> {

    @Override
    public Bitmap readData(InputStream in) throws Exception {
         return BitmapFactory.decodeStream(in);
    }
}
