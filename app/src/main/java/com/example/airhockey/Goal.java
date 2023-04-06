package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A Goal
 *
 * @author David Kaluta
 * @version 21
 * @since 5
 */
class Goal extends Entity {
    /**
     * The goal's score
     */
    private int score;

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    /**
     * Create a new goal
     *
     * @param x  the goal's x-coordinate
     * @param y  The goal's y-coordinate
     * @param pitch A HockeyTable for resources
     */
    Goal(float x, float y, Pitch pitch) {
        super(x, y, Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(pitch.getResources()
                        , R.drawable.black_pixel),
                (int) deviceWidth / 2, 10, true));
        score = 0;
    }

    /**
     * Get the goal's score
     *
     * @return the goal's score
     */
    int getScore() {
        return score;
    }

    /**
     * Increase the score by 1
     */
    void incScore() {
        score++;
    }

}
