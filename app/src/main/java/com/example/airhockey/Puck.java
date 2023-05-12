package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

public class Puck extends RoundEntity implements Runnable{

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private float newX;

    private Vector2D velocity;

    /**
     * The puck's velocity on the Y-axis
     */
    private float newY;

    /**
     * A HockeyTable to get the Paddles
     */
    private Pitch pitch;

    /**
     * If the game is in goal mode
     */
    private boolean goal;

    private int scorePlayer=0;
    private int scoreBot=0;
    /**
     * Initialize a new round entity
     *
     * @param x    The entity's x-coordinate
     * @param y    The entity's x-coordinate
     */
    Puck(float x, float y, Pitch pitch) {
        super(x, y,
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(pitch.getResources(),
                                R.drawable.pukgelb),
                        (int) ((deviceWidth)*64.0*2/1080.0), (int) ((deviceWidth)*64.0*2/1080.0), true));
        velocity = new Vector2D(x,y);
        setVelocity(new Vector2D(0,0));
        Thread thread = new Thread(this, "PuckThread");
        goal = false;
        this.pitch = pitch;
        newX = 0;
        newY = 0;
        thread.start();
    }

    public Vector2D getVelocity(){return velocity;}

    public void setVelocity(Vector2D velocity){this.velocity = velocity;}

    public void run() {
        Player p1 = pitch.getPlayer();
        Bot bot = pitch.getBot();
        while (!p1.isWinner() && !bot.isWinner()) {
            //System.out.println("Puck X: " + centerPointX);
            //System.out.println("Puck Y: " + centerPointY);
            if (!goal) {
                //Tor überprüfung
                //System.out.println(y-radius);

                Vector2D direction;
                newX = x + radius;
                newY = y + radius;

                //pitch.drawScore((int) newX, (int) newY);

                //GOAL Player
                if (newY < radius) {
                    System.out.println("Y Passt");
                    if (x > (deviceWidth / 2) - (deviceWidth / 6) && newX < (deviceWidth / 2) + (deviceWidth / 6)) {
                        System.out.println("X Passt");
                        scorePlayer+=1;
                        goal = true;
                        pitch.setScorePlayer(scorePlayer);
                        break;
                    }
                }
                //GOAL Bot
                else if (newY > deviceHeight - radius) {
                    if (x > (deviceWidth / 2) - (deviceWidth / 6) && newX < (deviceWidth / 2) + (deviceWidth / 6)) {
                        System.out.println("X Passt");
                        scoreBot+=1;
                        goal = true;
                        pitch.setScoreBot(scoreBot);
                        break;
                    }
                }


                if ((direction= checkCircleCollisionWithDirection(p1))!=null) {
                    setVelocity(new Vector2D(2,2));
                    handlePuckCollisionWithDirection(this, direction, p1);
                }

                if ((direction= checkCircleCollisionWithDirection(bot))!=null) {
                    setVelocity(new Vector2D(2,2));
                    handlePuckCollisionWithDirection(this, direction, bot);
                }

                if (x <= 0 || x >= deviceWidth - radius * 2) {
                    velocity.setX(velocity.getX()*-1);
                }
                if (x < 0) {
                    setX(0);
                }
                if (x > deviceWidth - radius * 2) {
                    setX(deviceWidth - radius * 2);
                }
                if (y <= 0 || y >= deviceHeight - radius * 2){
                    //System.out.println("Tor");
                    velocity.setY(velocity.getY()*-1);
                }

                newX += getVelocity().getX();
                newY += getVelocity().getY();

                //System.out.println(getVelocity().getX());
                //System.out.println(getVelocity().getY());

                setX(newX);
                setY(newY);


            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
