package uk.ac.reading.student.akostarevas.asteroids;

class MovableObject extends StaticObject {

    protected int canvasWidth;
    protected int canvasHeight;

    /* Angle in degrees */
    private float angle;
    private float velocity;

    MovableObject(float x, float y, int canvasWidth, int canvasHeight) {
        this(x, y, canvasWidth, canvasHeight, 0,0);
    }

    MovableObject(float x, float y, int canvasWidth, int canvasHeight, float angle, float velocity) {
        super(x, y);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.angle = angle;
        this.velocity = velocity;
    }

    @SuppressWarnings("unused")
    MovableObject(float x, float y) {
        this(x, y, 0, 0);
    }

    @SuppressWarnings("unused")
    void move(float secondsElapsed) {
        double angleRadians = (angle / 180.0 * Math.PI);

        double xSpeed = velocity * Math.sin(angleRadians);
        double ySpeed = velocity * Math.cos(angleRadians);

        x = (float) ((x + secondsElapsed * xSpeed) % canvasWidth);
        y = (float) ((y + secondsElapsed * ySpeed) % canvasHeight);
    }

}
