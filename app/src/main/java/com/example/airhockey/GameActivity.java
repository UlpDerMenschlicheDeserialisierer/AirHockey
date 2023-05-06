package com.example.airhockey;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.os.Bundle;
import android.widget.Button;

/**
 * The activity the game runs in
 *
 * @author David Kaluta
 * @version 21
 * @since 1
 */
public class GameActivity extends AppCompatActivity {

    /**
     * The width of the device
     */
    public static final float deviceWidth = Resources.getSystem()
            .getDisplayMetrics().widthPixels;

    /**
     * The height of the device
     */
    public static final float deviceHeight = Resources.getSystem()
            .getDisplayMetrics().heightPixels;

    /**
     * A HockeyTable for the game
     */
    Pitch pitch;

    /**
     * X-coordinate for the first touch
     */
    float xDown1;

    /**
     * Y-coordinate for the second touch
     */
    float yDown1;

    /**
     * X-coordinate for the second touch
     */
    float xDown2;

    /**
     * Y-coordinate for the second touch
     */
    float yDown2;

    String difficulty;

    public Button goal1 = findViewById(R.id.button); //Tor oben
    public Button goal2 = findViewById(R.id.button2); //Tor unten

    /**
     * Prepare for opening game
     *
     * @param savedInstanceState required for onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("DIFFICULTY");
        if (difficulty != null)
            pitch = new Pitch(this, goal2, goal1);
        else
            pitch = new Pitch(this, goal2, goal1);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(pitch);
    }

    /**
     * Make the app full screen
     *
     * @param hasFocus is the app in focus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getActionMasked();
        float playerRadius = pitch.getPlayer().radius;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown1 = event.getX();
                yDown1 = event.getY();
                if(xDown1 > playerRadius && xDown1< pitch.getWidth() - playerRadius){
                    if(yDown1 > pitch.getHeight()/2-playerRadius/2 && yDown1< pitch.getHeight()-playerRadius*2){
                        pitch.getPlayer().setX(xDown1);
                        pitch.getPlayer().setY(yDown1);
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                xDown1 = event.getX();
                yDown1 = event.getY();

                pitch.getPlayer().getFingerTracker().addFingerPosition(new Vector2D(xDown1, yDown1));

                //pitch.getPlayer().setVelocity();
                float newX = xDown1;
                float newY = yDown1;

                 playerRadius = pitch.getPlayer().radius;

                if(xDown1 < playerRadius){
                    newX = playerRadius;
                }else if(xDown1 > pitch.getWidth() - playerRadius) {
                    newX = pitch.getWidth() - playerRadius;
                }

                if(yDown1 < pitch.getHeight()/2-playerRadius/2) {
                    newY = pitch.getHeight()/2-playerRadius/2;
                }else if(yDown1 > pitch.getHeight()-playerRadius*2){
                    newY = pitch.getHeight()-playerRadius*2;
                }

                pitch.getPlayer().setX(newX);
                pitch.getPlayer().setY(newY);

                return true;
            default:
                return super.onTouchEvent(event);
        }


    }


}