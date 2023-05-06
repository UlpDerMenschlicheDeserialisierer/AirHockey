package com.example.airhockey;

import android.widget.Button;

public class Goal {
    private float x1, x2, y;

    public Goal(Button goal){
        x1 = goal.getX();
        x2 = x1 + goal.getWidth();
        y = goal.getY();
    }

    public float getX1() {
        return x1;
    }

    public float getX2() {
        return x2;
    }

    public float getY() {
        return y;
    }
}
