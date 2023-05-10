package com.example.airhockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;

public class Bot extends RoundEntity implements Runnable{
    private boolean isWinner;

    private Pitch pitch;

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Bot(float x, float y, Pitch pitch) {
        super(x, y, Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        pitch.getResources(), R.drawable.skinred),
                (int) ((deviceWidth)*128.0*2/1080.0), (int) ((deviceWidth)*128.0*2/1080.0), true));
        this.pitch = pitch;
        this.isWinner = false;

        velocity = new Vector2D(0,0);

        fingerTracker = new FingerTracker();
        fingerTracker.addFingerPosition(new Vector2D(x,y));

        Thread thread = new Thread(this, "BotThread");
        thread.start();
    }


    @Override
    public void run() {
        Player player = pitch.getPlayer();

        while(!player.isWinner() && !this.isWinner){
            setVelocity(new Vector2D((Math.random()*5)-2, (Math.random()*5)-2));
            long millis = System.currentTimeMillis();
            while (System.currentTimeMillis()< millis + 1000){
                float newX = (float) (getX() + getVelocity().getX() + radius);
                float newY = (float) (getY() + getVelocity().getY() + radius);

                if(newX>deviceWidth-radius){
                    newX = deviceWidth-radius;
                    getVelocity().setX(-getVelocity().getX());
                }else if(newX<radius) {
                    newX = radius;
                    getVelocity().setX(-getVelocity().getX());
                }

                if(newY>deviceHeight/2-radius){
                    newY=deviceHeight/2-radius;
                    getVelocity().setY(-getVelocity().getY());
                }else if(newY<radius){
                    newY=radius;
                    getVelocity().setY(-getVelocity().getY());
                }

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


    public boolean isWinner() {return isWinner;}
}
