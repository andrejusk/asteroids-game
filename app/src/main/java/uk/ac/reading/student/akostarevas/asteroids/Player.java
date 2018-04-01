package uk.ac.reading.student.akostarevas.asteroids;

@SuppressWarnings("unused")
public class Player extends MovableObject {
    public Player(int canvasWidth, int canvasHeight) {
        super(canvasWidth / 2, canvasHeight / 2, 0, 0, canvasWidth, canvasHeight);
    }
}
