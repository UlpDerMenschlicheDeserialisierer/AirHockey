package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.VerifiedKeyEvent;

/**
 * A player-controlled paddle
 */
class Player extends RoundEntity {

    /**
     * A variable showing if the player won
     */

    private Vector2D velocity;
    private boolean isWinner;

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;


    /**
     * Create a new Red Paddle
     *
     * @param x    the paddle's x-coordinate
     * @param y    the paddle's y-coordinate
     * @param pitch   a HockeyTable for resource getting
     */
    Player(int x, int y, Pitch pitch) {
        super(x, y,
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(
                                pitch.getResources(), R.drawable.skinblue),
                        (int) ((deviceWidth)*128.0*2/1080.0), (int) ((deviceWidth)*128.0*2/1080.0), true));
        velocity = new Vector2D(0,0);
        isWinner = false;
        fingerTracker = new FingerTracker();
        fingerTracker.addFingerPosition(new Vector2D(x, y));
    }

    public void setVelocity(Vector2D velocity){this.velocity=velocity;}

    public Vector2D getVelocity(){return velocity;}

    /**
     * Check if the red paddle won
     *
     * @return if the red paddle won
     */
    boolean isWinner() {
        return isWinner;
    }

    /**
     * Win the game for the red paddle
     */
    void win() {
        isWinner = true;
    }
}
