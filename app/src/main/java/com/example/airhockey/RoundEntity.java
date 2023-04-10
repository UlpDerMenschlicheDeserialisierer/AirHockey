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

    /**
     * The entity's mass
     */
    float mass;

    /**
     * Initialize a new round entity
     *
     * @param x   The entity's x-coordinate
     * @param y   The entity's x-coordinate
     * @param bmp A Bitmap
     */
    RoundEntity(float x, float y, float mass, Bitmap bmp) {
        super(x - (float) bmp.getWidth() / 2, y - (float) bmp.getHeight() / 2,
                bmp);
        this.mass = mass;
        radius = bmp.getWidth() / 2;
        centerPointX = x + radius;
        x -= radius;
        centerPointY = y + radius;
        y -= radius;
    }

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

    /**
     * Get the entity's mass
     *
     * @return the entity's mass
     */
    public float getMass() {
        return mass;
    }


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

    /**
     * Get the distance between 2 round entities
     *
     * @param other another RoundEntity
     * @return the distance between them
     */
    float distanceFrom(RoundEntity other) {
        float dx = centerPointX - other.centerPointX;
        float dy = centerPointY - other.centerPointY;
        int distance = (int) Math.sqrt(dx*dx + dy*dy);
        return distance;
    }

    public Vector2D checkCircleCollisionWithDirection(RoundEntity other) {
        float dx, dy;
        if(other.centerPointX > centerPointX){
            dx = other.centerPointX - centerPointX;
        }else{
            dx = centerPointX - other.centerPointX;
        }

        if(other.centerPointY > centerPointY){
            dy = other.centerPointY - centerPointY;
        }else{
            dy = centerPointY - other.centerPointY;
        }

        int distance = (int) Math.sqrt(dx * dx + dy * dy);

        if (distance > radius + other.radius) {
            // Die Kreise berühren sich nicht
            return null;
        }

        // Die Kreise berühren sich
        Vector2D direction = new Vector2D(dx, dy);
        direction.normalize();
        return direction;
    }

    public void handlePuckCollisionWithDirection(Puck puck, Vector2D collisionDirection) {
        // Berechne die Winkel zwischen der aktuellen Bewegungsrichtung des Pucks und dem Kollisionsvektor
        double angleOfIncidence = Math.atan2(puck.getVelocity().getY(), puck.getVelocity().getX());
        double angleOfCollision = Math.atan2(collisionDirection.getY(), collisionDirection.getX());
        double angleOfReflection = 2 * angleOfCollision - angleOfIncidence;

        // Berechne die neue Bewegungsrichtung des Pucks anhand des reflektierten Winkels
        double speed = puck.getVelocity().getMagnitude();
        double dx = speed * Math.cos(angleOfReflection);
        double dy = speed * Math.sin(angleOfReflection);
        Vector2D newVelocity = new Vector2D(dx, -dy);

        // Setze die neue Bewegungsrichtung des Pucks
        puck.setVelocity(newVelocity);
    }

}
