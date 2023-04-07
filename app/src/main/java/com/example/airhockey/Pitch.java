package com.example.airhockey;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;


/**
 * The View for the game
 *
 * @author David Kaluta
 * @version 24
 * @since 1
 */
public class Pitch extends View {

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    /**
     * A red paddle (PC)
     */
    Player player;

    /**
     * A blue paddle (NPC)
     */
    //BluePaddle bp;

    /**
     * A puck
     */
    Puck p;

    /**
     * The game background
     */
    Bitmap bg;

    /**
     * A separator line
     */
    Bitmap line;

    /**
     * A Paint to draw text with
     */
    Paint paint;

    /**
     * A timer for pausing
     */
    Timer timer;

    /**
     * The time required to pause
     */
    int pauseTime;

    /**
     * The game's difficulty
     */
    String difficulty;

    /**
     * Create a HockeyTable with a difficulty
     *
     * @param context    required for a View
     * @param difficulty The difficulty level
     */
    public Pitch(Context context, @NonNull String difficulty) {
        super(context);
        this.difficulty = difficulty;
        paint = new Paint();
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        pauseTime = -4;
        bg = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.field)
                , deviceWidth, deviceHeight, true);
        player = new Player(deviceWidth / 2, 7 * deviceHeight / 8,
                new Goal(deviceWidth / 4, 0, this), this);

        p = new Puck(deviceWidth / 2, deviceHeight / 2, this);
    }

    /**
     * Create a HockeyTable without a difficulty (will be Medium)
     *
     * @param context required for a View
     */
    public Pitch(Context context) {
        super(context);
        paint = new Paint();
        int deviceWidth = Resources.getSystem().getDisplayMetrics()
                .widthPixels;
        int deviceHeight = Resources.getSystem().getDisplayMetrics()
                .heightPixels;
        bg = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.field)
                , deviceWidth, deviceHeight, true);
        player = new Player(deviceWidth / 2, 7 * deviceHeight / 8,
                new Goal(deviceWidth / 4, 0, this), this);

        p = new Puck(deviceWidth / 2, deviceHeight / 2, this);
    }

    /**
     * Get the red paddle
     *
     * @return the red paddle
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Draw everything
     *
     * @param c required for onDraw
     */
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawBitmap(bg, 0, 0, null);
        player.draw(c);
        player.getGoal().draw(c);
        p.draw(c);
        paint.setTextSize(144);
        paint.setColor(Color.WHITE);

        invalidate();
    }
}
