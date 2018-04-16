package uk.ac.reading.student.akostarevas.asteroids;

/**
 * Object class.
 * Data structure with x, y and size.
 */
class Object {

    float size;
    float x;
    float y;

    /**
     * Object constructor without size.
     * @param x X location.
     * @param y Y location.
     */
    Object(float x, float y) {
        this(x, y, 0);
    }

    /**
     * Object constructor.
     * @param x X location.
     * @param y Y location.
     * @param size Size.
     */
    Object(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

}
