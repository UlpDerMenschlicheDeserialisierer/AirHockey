package com.example.airhockey;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Timer;

public class Pitch extends View {

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Player player;
    private Bot bot;
    private Puck p;
    private Bitmap bg;
    private Paint paint;
    private int scorePlayer = 0;
    private int scoreBot = 0;
    private Canvas c;
    private boolean startGoalAnimation = false;

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
        //goalAnimation();

        invalidate();
    }

    public void drawScore() {
        Paint paint = new Paint();
        paint.setTextSize(144);
        paint.setColor(Color.WHITE);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.montserrat);
        paint.setTypeface(typeface);
        paint.setShadowLayer(30f, 10f, 10f, Color.parseColor("#00DDFF"));

        c.save();
        c.rotate(90);
        String text = scoreBot + " : " + scorePlayer;
        c.drawText(text, deviceHeight / 2 - deviceHeight / 14, -deviceWidth + deviceWidth / 6, paint);
        c.restore();
        if (startGoalAnimation) {
            paint.setTextSize(200);
            c.drawText("GOAL", deviceWidth / 2 - deviceWidth / 4, deviceHeight / 2 + deviceHeight / 28, paint);
            invalidate();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    c.drawText("GOAL", deviceWidth + 10, deviceHeight + 10, paint);
                    startGoalAnimation = false;
                }
            }, 2000); // Delay for 1 second (1000 milliseconds)
            //startGoalAnimation = false;
        }

        invalidate();
    }

    public void goalAnimation(){
        if (startGoalAnimation) {
            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setColor(Color.WHITE);
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.montserrat);
            paint.setTypeface(typeface);
            paint.setShadowLayer(30f, 10f, 10f, Color.parseColor("#00DDFF"));

            c.drawText("GOAL", deviceWidth / 2 - deviceWidth / 4, deviceHeight / 2 + deviceHeight / 28, paint);
            // Invalidate the view to force a redraw
            invalidate();
            startGoalAnimation = false;
        }
    }

    public void setScorePlayer(int scorePlayer) {
        this.scorePlayer = scorePlayer;
    }

    public void setScoreBot(int scoreBot) {
        this.scoreBot = scoreBot;
    }
    public void setStartGoalAnimation(boolean startGoalAnimation) {
        this.startGoalAnimation = startGoalAnimation;
    }
}
