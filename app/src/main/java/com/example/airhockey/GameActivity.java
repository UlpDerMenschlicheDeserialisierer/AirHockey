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
            pitch = new Pitch(this, difficulty);
        else
            pitch = new Pitch(this);
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


    /**
     * Check if the screen is touched
     *
     * @param event a MotionEvent with a touch action
     * @return true if the screen has been touched
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown1 = event.getX();
                yDown1 = event.getY();
                if (xDown1 > pitch.getPlayer().x - 64 && xDown1 < pitch.getPlayer().x + pitch.getPlayer().radius + 64) {
                    if (yDown1 > pitch.getPlayer().y - 64 && yDown1 < pitch.getPlayer().y + pitch.getPlayer().radius + 64) {
                        pitch.getPlayer().setX(xDown1);
                        pitch.getPlayer().setY(Math.max(yDown1, deviceHeight / 2 + pitch.getPlayer().radius));
                    }
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                xDown2 = event.getX(1);
                yDown2 = event.getY(1);
                if (xDown2 > pitch.getPlayer().x - 64 && xDown2 < pitch.getPlayer().x + pitch.getPlayer().radius + 64) {
                    if (yDown2 > pitch.getPlayer().y - 64 && yDown2 < pitch.getPlayer().y + pitch.getPlayer().radius + 64) {
                        pitch.getPlayer().setX(xDown2);
                        pitch.getPlayer().setY(Math.max(yDown2, deviceHeight / 2 + pitch.getPlayer().radius));
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    for (int i = 0; i < 2; i++) {
                        float xMove = event.getX(i);
                        float yMove = event.getY(i);
                        if (xMove > pitch.getPlayer().x - 64 && xMove < pitch.getPlayer().x + pitch.getPlayer().radius + 64) {
                            if (yMove > pitch.getPlayer().y - 64 && yMove < pitch.getPlayer().y + pitch.getPlayer().radius + 64) {
                                pitch.getPlayer().setX(xMove);
                                pitch.getPlayer().setY(Math.max(yMove, deviceHeight / 2 + pitch.getPlayer().radius));
                            }
                        }
                    }
                } else {
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (xMove > pitch.getPlayer().x - 64 && xMove < pitch.getPlayer().x + pitch.getPlayer().radius + 64) {
                        if (yMove > pitch.getPlayer().y - 64 && yMove < pitch.getPlayer().y + pitch.getPlayer().radius + 64) {
                            pitch.getPlayer().setX(xMove);
                            pitch.getPlayer().setY(Math.max(yMove, deviceHeight / 2 + pitch.getPlayer().radius));
                        }
                    }
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}