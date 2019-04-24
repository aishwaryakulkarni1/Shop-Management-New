package com.inevitablesol.www.shopmanagement.analysis.date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import com.inevitablesol.www.shopmanagement.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Pritam on 21-12-2017.
 */

public class ImageBuilders extends AppCompatActivity
{

    public   byte[] getBitmapArray(int id)
    {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        return bitmapdata;

    }
}
