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

                        // Berechne die Positionen des Puckmittelpunkts und des Playermittelpunkts
                        float puckCenterX = puck.getX() + puck.getWidth() / 2;
                        float puckCenterY = puck.getY() + puck.getHeight() / 2;
                        float playerCenterX = player1.getX() + player1.getWidth() / 2;
                        float playerCenterY = player1.getY() + player1.getHeight() / 2;

                        // Berechne die Distanz zwischen den Mittelpunkten
                        float distance = (float) Math.sqrt(Math.pow(puckCenterX - playerCenterX, 2) + Math.pow(puckCenterY - playerCenterY, 2));

                        // Berechne die Summe der Radien
                        float sumOfRadii = puck.getWidth() / 2 + player1.getWidth() / 2;

                        // Überprüfe, ob der Puck den Player berührt hat
                        if (distance < sumOfRadii) {
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
