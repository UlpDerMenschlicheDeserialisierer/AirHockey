package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

public class Puck extends RoundEntity implements Runnable{

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private float dx;

    private Vector2D velocity;

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

        velocity = new Vector2D(x,y);
        setVelocity(new Vector2D(0,0));
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

    public Vector2D getVelocity(){return velocity;}

    public void setVelocity(Vector2D velocity){this.velocity = velocity;}

    public void run() {
        while (!pitch.getPlayer().isWinner() && !pitch.getPlayer().isWinner()) {
            if (!goal) {
                Player p1 = pitch.getPlayer();
                Vector2D direction;
                if ((direction= checkCircleCollisionWithDirection(p1))!=null) {
                    System.out.println("Kollision");
                    setVelocity(new Vector2D(1,1));
                    handlePuckCollisionWithDirection(this, direction);
                }

                if (x <= 0 || x >= deviceWidth - radius * 2) {
                    velocity.setX(velocity.getX()*-1);
                    System.out.println("1");
                }
                if (x < 0) {
                    x = 0;
                    System.out.println("2");
                }
                if (x > deviceWidth - radius * 2) {
                    x = deviceWidth - radius * 2;
                    System.out.println("3");
                }
                if (y <= 0 || y >= deviceHeight - radius * 2){
                    velocity.setY(velocity.getY()*-1);
                    System.out.println("4");
                }

                x += getVelocity().getX();
                y += getVelocity().getY();
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
