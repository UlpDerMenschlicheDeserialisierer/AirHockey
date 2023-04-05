package com.example.airhockey;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.graphics.Rect;

public class Pitch extends AppCompatActivity {
    private ImageView player1;
    private ImageView puck;
    private float xCoOrdinate, yCoOrdinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch);

        player1 = findViewById(R.id.imageViewPlayer1);
        puck = findViewById(R.id.imageViewPuck);

        player1.setOnTouchListener(new View.OnTouchListener() {
            ConstraintLayout layout = findViewById(R.id.pitchlayout);
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xCoOrdinate = view.getX() - event.getRawX();
                        yCoOrdinate = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + xCoOrdinate;
                        float newY = event.getRawY() + yCoOrdinate;

                        // Überprüfe, ob die ImageView das Layout überschreitet
                        if (newX < 0) {
                            newX = 0;
                        } else if (newX > layout.getBackground().getBounds().width() - view.getWidth()) {
                            newX = layout.getBackground().getBounds().width() - view.getWidth();
                        }
                        if (newY < layout.getBackground().getBounds().height()/2 - (player1.getHeight()/2)) { // nur untere Hälfte des Hintergrunds erlaubt
                            newY = layout.getBackground().getBounds().height()/2 - (player1.getHeight()/2);
                        } else if (newY > this.layout.getBackground().getBounds().height() - view.getHeight()) {
                            newY = layout.getBackground().getBounds().height() - view.getHeight();
                        }

                        view.animate().x(newX).y(newY).setDuration(0).start();

                        // Überprüfe, ob sich player1 und puck berühren
                        Rect rectPlayer1 = new Rect((int)player1.getX(), (int)player1.getY(), (int)(player1.getX() + player1.getWidth()), (int)(player1.getY() + player1.getHeight()));
                        Rect rectPuck = new Rect((int)puck.getX(), (int)puck.getY(), (int)(puck.getX() + puck.getWidth()), (int)(puck.getY() + puck.getHeight()));
                        if (rectPlayer1.intersects(rectPuck.left, rectPuck.top, rectPuck.right, rectPuck.bottom)) {
                            // Bestimme die Berührungsposition auf der Spieler-ImageView
                            float touchX = event.getRawX() - player1.getX();
                            float touchY = event.getRawY() - player1.getY();

                            // Bestimme die Breite der Spieler-ImageView
                            float playerWidth = player1.getWidth();

                            // Bestimme von welcher Seite der Puck getroffen wurde
                            if (touchX < playerWidth / 2) { // linker Bereich
                                // Bewege den Puck nach rechts
                                puck.animate().x(puck.getX() - 10).setDuration(0).start();
                            } else { // rechter Bereich
                                // Bewege den Puck nach links
                                puck.animate().x(puck.getX() + 10).setDuration(0).start();
                            }
                        }

                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
