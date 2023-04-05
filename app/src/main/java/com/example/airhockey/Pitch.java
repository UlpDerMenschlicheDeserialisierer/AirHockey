package com.example.airhockey;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Pitch extends AppCompatActivity {
    private ImageView player1;
    private float xCoOrdinate, yCoOrdinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch);

        player1 = findViewById(R.id.imageViewPlayer1);

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
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
