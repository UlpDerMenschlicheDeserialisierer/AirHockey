package com.example.airhockey;
import android.graphics.Bitmap;

/**
 * A round entity subclass
 */
public abstract class RoundEntity extends Entity {

    /**
     * The center point's x-coordinate
     */
    float centerPointX;

    /**
     * The center point's y-coordinate
     */
    float centerPointY;

    /**
     * The round entity's radius
     */
    float radius;

    FingerTracker fingerTracker;

    Vector2D velocity;

    /**
     * Initialize a new round entity
     *
     * @param x   The entity's x-coordinate
     * @param y   The entity's x-coordinate
     * @param bmp A Bitmap
     */
    RoundEntity(float x, float y, Bitmap bmp) {
        super(x - (float) bmp.getWidth() / 2, y - (float) bmp.getHeight() / 2,
                bmp);
        radius = bmp.getWidth() / 2;
        centerPointX = x + radius;
        x -= radius;
        centerPointY = y + radius;
        y -= radius;
    }

    public Vector2D getVelocity(){return velocity;}

    public void setVelocity(Vector2D velocity){this.velocity = velocity;}

    /**
     * Get the center point x-coordinate
     *
     * @return the center point x-coordinate
     */
    float getCenterPointX() {
        return centerPointX;
    }


    /**
     * Get the center point y-coordinate
     *
     * @return the center point y-coordinate
     */
    public float getCenterPointY() {
        return centerPointY;
    }


    /**
     * Get the entity radius
     *
     * @return the entity's radius
     */
    public float getRadius() {
        return radius;
    }

    public FingerTracker getFingerTracker(){return fingerTracker;}


    /**
     * Set the x-coordinate with changing the center point
     *
     * @param x the new x-coordinate
     */
    @Override
    public void setX(float x) {
        super.setX(x - radius);
        centerPointX = x + radius;
    }

    /**
     * Set the y-coordinate with changing the center point
     *
     * @param y the new y-coordinate
     */
    @Override
    public void setY(float y) {
        super.setY(y - radius);
        centerPointY = y + radius;
    }

    public Vector2D checkCircleCollisionWithDirection(RoundEntity player) {
        float dx, dy;

        dx = player.centerPointX - centerPointX;
        dy = player.centerPointY - centerPointY;

        int distance = (int) Math.sqrt(dx * dx + dy * dy);

        if (distance > radius + player.radius) {
            // Die Kreise berühren sich nicht
            return null;
        }

        // Die Kreise berühren sich
        Vector2D direction = new Vector2D(dx, dy); // Swap dx and dy here
        direction.normalize();
        return direction;
    }

    public void handlePuckCollisionWithDirection(Puck puck, Vector2D collisionDirection, RoundEntity player) {
        // Berechne den Winkel zwischen der aktuellen Bewegungsrichtung des Pucks und dem Kollisionsvektor
        double angleOfIncidence = Math.atan2(puck.getVelocity().getY(), puck.getVelocity().getX());
        double angleOfCollision = Math.atan2(collisionDirection.getY(), collisionDirection.getX());

        // Berechne den Winkel, um den der Puck reflektiert werden soll, indem der Winkel zwischen der Kollisionsrichtung und der x-Achse des Koordinatensystems um 180 Grad gedreht wird.
        double angleOfOppositeDirection = angleOfCollision + Math.PI;

        // Berechne die x- und y-Komponenten der neuen Bewegungsrichtung des Pucks basierend auf dem reflektierten Winkel und der aktuellen Geschwindigkeit des Pucks.
        double speed = (player.getFingerTracker().getFingerVelocity().getMagnitude() + puck.getVelocity().getMagnitude());
        double dx = speed * Math.cos(angleOfOppositeDirection);
        double dy = speed * Math.sin(angleOfOppositeDirection);

        // Setze die neue Bewegungsrichtung des Pucks
        puck.setVelocity(new Vector2D(dx, dy));
    }

}
