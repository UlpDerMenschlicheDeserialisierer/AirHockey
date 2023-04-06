package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A player-controlled paddle
 */
class Player extends RoundEntity {

    /**
     * a Goal for the paddle
     */
    private Goal goal;

    /**
     * A variable showing if the player won
     */
    private boolean isWinner;

    public int delay;

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    /**
     * Create a new Red Paddle
     *
     * @param x    the paddle's x-coordinate
     * @param y    the paddle's y-coordinate
     * @param goal a Goal for the paddle
     * @param pitch   a HockeyTable for resource getting
     */
    Player(int x, int y, Goal goal, Pitch pitch) {
        super(x, y, 2.5f,
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(
                                pitch.getResources(), R.drawable.skinblue),
                        (int) ((deviceWidth)*128.0/1080.0), (int) ((deviceWidth)*128.0/1080.0), true));
        isWinner = false;
        this.goal = goal;
        delay = 0;
    }

    /**
     * Get the paddle's goal
     *
     * @return the goal
     */
    Goal getGoal() {
        return goal;
    }

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
