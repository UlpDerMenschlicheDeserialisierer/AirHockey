package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

public class Puck extends RoundEntity implements Runnable{

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private float dx;

    /**
     * The puck's velocity on the Y-axis
     */
    private float dy;

    /**
     * A HockeyTable to get the Paddles
     */
    private Pitch pitch;

    /**
     * The puck's previous coordinates
     */
    private float[] prevCoords;

    /**
     * The previous coordinates of the red paddle
     */
    private float[] rpPrevCoords;

    /**
     * The previous coordinates of the blue paddle
     */
    private float[] bpPrevCoords;

    /**
     * If the game is in goal mode
     */
    private boolean goal;
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
        Thread thread = new Thread(this, "PuckThread");
        rpPrevCoords = new float[2];
        bpPrevCoords = new float[2];
        prevCoords = new float[2];
        goal = false;
        this.pitch = pitch;
        dx = 0;
        dy = 0;
        thread.start();
    }


    @Override
    public void run() {
        while (!pitch.getPlayer().isWinner() && !pitch.getPlayer().isWinner()) {
            if (!goal) {
                if (x <= 0 || x >= deviceWidth - radius * 2)
                    dx = -dx;
                if (x < 0)
                    x = 0;
                if (x > deviceWidth - radius * 2)
                    x = deviceWidth - radius * 2;
                if (y <= 0 || y >= deviceHeight - radius * 2)
                    dy = -dy;
                Player player = pitch.getPlayer();
                if (player != null) {
                    if (distanceFrom(player) <= radius + player.radius) {
                        while (distanceFrom(player) <= radius + player.radius) {
                            x -= dx;
                            centerPointX = x + radius;
                            y -= dy;
                            centerPointY = y + radius;
                        }
                        player.delay = 2;
                        float m = dy / dx;
                        float[] rpCoords = {player.getCenterPointX(), player.getCenterPointY()};
                        // y - rpCoords[1] = m(x-rpCoords[0])
                        // y = mx - m*rpCoords[0] + rpCoords[1]
                        if (centerPointY < m * centerPointX - m * rpCoords[0] + rpCoords[1]) {
                            if (centerPointY > player.centerPointY) {
                                dy = dy;
                                dx = -dx;
                            } else {
                                dy = -dy;
                                dx = dx;
                            }
                        } else if (centerPointY > m * centerPointX - m * rpCoords[0] + rpCoords[1]) {
                            if (centerPointY > player.centerPointY) {
                                dx = dx;
                                dy = -dy;
                            } else {
                                dx = -dx;
                                dy = dy;
                            }
                        } else {
                            dx = -dx;
                            dy = -dy;
                        }
                    }
                }
                if (dx == 0 && dy == 0) {
                    centerPointX = deviceWidth / 2;
                    x = centerPointX - radius;
                    centerPointY = deviceHeight / 2;
                    y = centerPointY - radius;
                    dy = 2;
                    dx = 2;
                }
                if (player != null) {
                    rpPrevCoords[0] = player.getX();
                    rpPrevCoords[1] = player.getY();
                }
                x += dx;
                centerPointX += dx;
                y += dy;
                centerPointY += dy;

            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
