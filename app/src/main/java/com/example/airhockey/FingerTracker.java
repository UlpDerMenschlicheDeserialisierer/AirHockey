package com.example.airhockey;

import java.util.ArrayList;
import java.util.List;

public class FingerTracker {
    private List<Vector2D> fingerPositions;
    private long lastUpdateTime;
    private Vector2D fingerVelocity;

    private int maxVelocity = 10;

    public FingerTracker() {
        fingerPositions = new ArrayList<>();
        lastUpdateTime = 0;
        fingerVelocity = new Vector2D(0, 0);
    }

    public void addFingerPosition(Vector2D position) {
        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == 0 || currentTime - lastUpdateTime >= 50) { // Update every 50 milliseconds
            fingerPositions.add(position);
            if (fingerPositions.size() > 10) { // Keep only the last 10 positions
                fingerPositions.remove(0);
            }

            if (fingerPositions.size() > 1) { // Calculate finger velocity
                Vector2D lastPosition = fingerPositions.get(fingerPositions.size() - 2);
                fingerVelocity = Vector2D.subtract(position, lastPosition).multiply(0.1); // Multiply by 20 to convert to pixels per second

                //Set max velocity
                if(fingerVelocity.getX() > maxVelocity){fingerVelocity.setX(maxVelocity);} else if (fingerVelocity.getX()<maxVelocity*-1) {fingerVelocity.setX(maxVelocity*-1);}
                if(fingerVelocity.getY() > maxVelocity){fingerVelocity.setY(maxVelocity);} else if (fingerVelocity.getY()<maxVelocity*-1) {fingerVelocity.setY(maxVelocity*-1);}
            }

            lastUpdateTime = currentTime;
        }
    }

    public Vector2D getFingerVelocity() {
        return fingerVelocity;
    }

    public void clear() {
        fingerPositions.clear();
        fingerVelocity = new Vector2D(0, 0);
        lastUpdateTime = 0;
    }
}
