package uk.ac.reading.student.akostarevas.asteroids;

public class MovableObject extends StaticObject {

    /* Speed (pixel/second) of the object in direction X and Y */
    //TODO: turn into velocity and angle?
    private float xSpeed;
    private float ySpeed;

    MovableObject(float x, float y, float xSpeed, float ySpeed) {
        super(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @SuppressWarnings("unused")
    MovableObject(float x, float y) {
        this(x, y, 0, 0);
    }

    @SuppressWarnings("unused")
    void move(float secondsElapsed) {
        x += secondsElapsed * xSpeed;
        y += secondsElapsed * ySpeed;
    }

}
