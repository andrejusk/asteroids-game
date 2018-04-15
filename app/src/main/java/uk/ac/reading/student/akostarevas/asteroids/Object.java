package uk.ac.reading.student.akostarevas.asteroids;

/**
 * Abstract Object.
 * Data structure with x, y, size.
 */
abstract class Object {

    float size;

    float x;
    float y;

    Object(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

}
