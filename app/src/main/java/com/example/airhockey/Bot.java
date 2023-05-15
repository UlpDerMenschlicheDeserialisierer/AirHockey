package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;

public class Bot extends RoundEntity implements Runnable {
    private boolean isWinner;

    private Pitch pitch;
    private Puck puck;  // Referenz auf den Puck

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    // Konstruktor erhält zusätzlich Referenz auf den Puck
    public Bot(float x, float y, Pitch pitch, Puck puck) {
        super(x, y, Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        pitch.getResources(), R.drawable.skinred),
                (int) ((deviceWidth) * 128.0 * 2 / 1080.0), (int) ((deviceWidth) * 128.0 * 2 / 1080.0), true));
        this.pitch = pitch;
        this.puck = puck;  // Referenz auf den Puck speichern
        this.isWinner = false;

        velocity = new Vector2D(0, 0);

        fingerTracker = new FingerTracker();
        fingerTracker.addFingerPosition(new Vector2D(x, y));

        Thread thread = new Thread(this, "BotThread");
        thread.start();
    }


    @Override
    public void run() {
        Player player = pitch.getPlayer();

        while (!player.isWinner() && !this.isWinner) {
            // Richtungsvektor vom Bot zum Puck berechnen
            Vector2D directionToPuck = Vector2D.subtract(new Vector2D(puck.getCenterPointX(), puck.getY()), new Vector2D(getCenterPointX(), getCenterPointY()));
            directionToPuck.normalize();
            // Geschwindigkeit auf den Betrag des Richtungsvektors setzen
            setVelocity(directionToPuck.multiply(5));

            // Bot bewegt sich nun aktiv zum Puck
            long millis = System.currentTimeMillis();
            while (System.currentTimeMillis() < millis + 1000) {
                float newX = (float) (getX() + getVelocity().getX() + radius);
                float newY = (float) (getY() + getVelocity().getY() + radius);

                // Abprallen am Spielfeldrand
                if (newX > deviceWidth - radius) {
                    newX = deviceWidth - radius;
                    getVelocity().setX(-getVelocity().getX());
                } else if (newX < radius) {
                    newX = radius;
                    getVelocity().setX(-getVelocity().getX());
                }

                if (newY > deviceHeight / 2 - radius) {
                    newY = deviceHeight / 2 - radius;
                    getVelocity().setY(-getVelocity().getY());
                } else if (newY < radius) {
                    newY = radius;
                    getVelocity().setY(-getVelocity().getY());
                }

                // Setzen der neuen Position
                setX(newX);
                setY(newY);



                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void win(){isWinner=true;}
}
