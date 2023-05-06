package com.example.airhockey;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class Pitch extends View {

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    Player player;


    Bot bot;

    /**
     * A puck
     */
    Puck p;

    /**
     * The game background
     */
    Bitmap bg;


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

    /*
    /**
     * Create a HockeyTable with a difficulty
     *
     * @param context    required for a View
     * @param difficulty The difficulty level


    public Pitch(Context context, @NonNull String difficulty) {
        super(context);
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

        bot = new Bot(deviceWidth / 2, deviceHeight / 8, this);

        p = new Puck(deviceWidth / 2, deviceHeight / 2, this);
    }

    /**
     * Create a HockeyTable without a difficulty (will be Medium)
     *
     * @param context required for a View
     */
    public Pitch(Context context, Button goalPlayer, Button goalBot) {
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
                new Goal(goalPlayer), this);

        bot = new Bot(deviceWidth / 2, deviceHeight / 8, this, new Goal(goalBot));

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

    public Bot getBot(){return bot;}


    /**
     * Draw everything
     *
     * @param c required for onDraw
     */
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawBitmap(bg, 0, 0, null);
        player.draw(c);
        bot.draw(c);
        p.draw(c);
        paint.setTextSize(144);
        paint.setColor(Color.WHITE);

        invalidate();
    }
}
