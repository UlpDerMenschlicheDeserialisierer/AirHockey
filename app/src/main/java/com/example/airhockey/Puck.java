package com.example.airhockey;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup;
import android.widget.Button;

public class Puck extends RoundEntity implements Runnable{

    private static float deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static float deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private float newX;
    private Vector2D velocity;
    private float newY;
    private Pitch pitch;
    private boolean goal;
    private int scorePlayer=0;
    private int scoreBot=0;
    private Bot bot;
    private Context context;
    private Thread thread;
    /**
     * Initialize a new round entity
     *
     * @param x    The entity's x-coordinate
     * @param y    The entity's x-coordinate
     */
    Puck(float x, float y, Pitch pitch, Context context) {
        super(x, y, Bitmap.createScaledBitmap(BitmapFactory.decodeResource(pitch.getResources(), R.drawable.pukgelb), (int) ((deviceWidth)*64.0*2/1080.0), (int) ((deviceWidth)*64.0*2/1080.0), true));
        velocity = new Vector2D(x,y);
        setVelocity(new Vector2D(0,0));
        thread = new Thread(this, "PuckThread");
        goal = false;
        this.pitch = pitch;
        this.context = context;
        newX = 0;
        newY = 0;
        bot = new Bot(deviceWidth / 2, deviceHeight / 8, pitch, this);
        thread.start();
    }

    public Bot getBot(){return this.bot;}

    public Vector2D getVelocity(){return velocity;}

    public void setVelocity(Vector2D velocity){this.velocity = velocity;}

    public void setGoal(boolean goal){this.goal=goal;}

    public void run() {
        Player p1 = pitch.getPlayer();
        Looper.prepare();
        while (!p1.isWinner() && !bot.isWinner()) {
            if(scorePlayer==1){
                p1.win();
                showDialog("Player");
                break;
            }
            if(scoreBot==5){
                bot.win();
                showDialog("Bot");
                break;
            }

            if (!goal) {
                //Tor überprüfung
                Vector2D direction;
                newX = x + radius;
                newY = y + radius;

                //pitch.drawScore((int) newX, (int) newY);

                //GOAL Player
                if (newY < radius) {
                    if (x > (deviceWidth / 2) - (deviceWidth / 6) && newX < (deviceWidth / 2) + (deviceWidth / 6)) {
                        scorePlayer+=1;
                        goal = true;
                        pitch.setStartGoalAnimation(true);
                        pitch.setGoalPlayer(true);
                        pitch.setScorePlayer(scorePlayer);
                        continue;
                    }
                }
                //GOAL Bot
                else if (newY > deviceHeight - radius) {
                    if (x > (deviceWidth / 2) - (deviceWidth / 6) && newX < (deviceWidth / 2) + (deviceWidth / 6)) {
                        scoreBot+=1;
                        goal = true;
                        pitch.setStartGoalAnimation(true);
                        pitch.setGoalPlayer(false);
                        pitch.setScoreBot(scoreBot);
                        continue;
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
                    velocity.setY(velocity.getY()*-1);
                }

                newX += getVelocity().getX();
                newY += getVelocity().getY();

                setX(newX);
                setY(newY);
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Looper.loop();
    }
    private void showDialog(String name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Titel
        SpannableString title = new SpannableString("Game Finished!");
        title.setSpan(new RelativeSizeSpan(1.2f), 0, title.length(), 0);
        builder.setTitle(title);

        // Nachricht
        SpannableString message = new SpannableString("The " + name + " won!\n\n");
        message.setSpan(new RelativeSizeSpan(1.1f), 0, message.length(), 0);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent resultIntent = new Intent();
                ((Activity) context).setResult(RESULT_OK, resultIntent);
                ((Activity) context).finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                ViewGroup.LayoutParams params = positiveButton.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundResource(R.drawable.button_background);
            }
        });
        dialog.show();
    }
}
