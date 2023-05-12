package com.example.airhockey;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class Pitch extends View {

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Player player;
    private Bot bot;
    private Puck p;
    private Bitmap bg;
    private Paint paint;

    private Timer timer;

    private int pauseTime;

    private int scorePlayer = 0;

    public void incrementScorePlayer() {
        this.scorePlayer +=1;
    }

    public void incrementScoreBot() {
        this.scoreBot +=1;
    }

    private int scoreBot = 0;

    private Canvas c;

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
    public Pitch(Context context) {
        super(context);
        paint = new Paint();
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), deviceWidth, deviceHeight, true);
        player = new Player(deviceWidth / 2, 7 * deviceHeight / 8, this);
        bot = new Bot(deviceWidth / 2, deviceHeight / 8, this);
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
        this.c = c;
        super.onDraw(c);
        c.drawBitmap(bg, 0, 0, null);

        player.draw(c);
        bot.draw(c);
        p.draw(c);
        drawScore();
        invalidate();
    }

    public void drawScore(){
        Paint paint = new Paint();
        paint.setTextSize(144);
        paint.setColor(Color.WHITE);
        c.save();
        c.rotate(90);
        String text = scoreBot + " : " + scorePlayer;
        c.drawText(text,  deviceHeight/2 - deviceWidth/8 , -deviceWidth+deviceWidth/5, paint);
        c.restore();

        invalidate();
    }
}
