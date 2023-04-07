package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Puck extends RoundEntity{

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Pitch pitch;
    /**
     * Initialize a new round entity
     *
     * @param x    The entity's x-coordinate
     * @param y    The entity's x-coordinate
     */
    Puck(float x, float y, Pitch pitch) {
        super(x, y, 0.5f,
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(pitch.getResources(),
                                R.drawable.pukgelb),
                        (int) ((deviceWidth)*64.0*2/1080.0), (int) ((deviceWidth)*64.0*2/1080.0), true));
    }
}
