package uk.ac.reading.student.akostarevas.asteroids;

class Vector {

    float xDisplace;
    float yDisplace;

    double velocity;
    double angleDegrees;

    Vector() {
        this(0, 0);
    }

    Vector(float xDisplace, float yDisplace) {
        this.xDisplace = xDisplace;
        this.yDisplace = yDisplace;
        updateVector();
    }

    Vector(double velocity, double angleDegrees) {
        this.velocity = velocity;
        this.angleDegrees = angleDegrees;
        updateDisplacement();
    }

    Vector(Object source, Object target) {
        this(target.x - source.x, target.y - source.y);
    }

    /**
     * Updates Vector with Displacement.
     */
    void updateVector() {
        /* Sum of squares */
        velocity = Math.sqrt(Math.pow(xDisplace, 2) + Math.pow(yDisplace, 2));

        if (xDisplace == 0 && yDisplace == 0) {
            angleDegrees = 0;
            return;
        }

        /* Quick maths */
        if (yDisplace == 0) {
            angleDegrees = 90;
        } else {
            double angleRadians = Math.atan(xDisplace / yDisplace);
            angleDegrees = angleRadians * 180.0 / Math.PI;
        }

        if (yDisplace < 0) {
            angleDegrees += 180.0;
        }
    }

    /**
     * Update Displacement with Vector.
     */
    void updateDisplacement() {
        double angleRadians = (angleDegrees / 180.0 * Math.PI);
        xDisplace = (float) (velocity * Math.sin(angleRadians));
        yDisplace = (float) (velocity * Math.cos(angleRadians));
    }

    /**
     * Updates and returns velocity.
     * @return Velocity.
     */
    double getVelocity() {
        updateVector();
        return velocity;
    }

    /**
     * Takes velocity and updates displacement.
     * @param velocity Change in velocity.
     */
    void updateVelocity(double velocity) {
        setVelocity(this.velocity + velocity);
        updateDisplacement();
    }

    void setVelocity(double velocity) {
        this.velocity = velocity;
        updateDisplacement();
    }

    /**
     * Updates and returns xDisplace.
     * @return xDisplace.
     */
    float getX() {
        updateDisplacement();
        return xDisplace;
    }

    /**
     * Updates and returns yDisplace.
     * @return yDisplace.
     */
    float getY() {
        updateDisplacement();
        return yDisplace;
    }

    /**
     * Cross-product multiplies two Vectors.
     * @param vector1 Vector1.
     * @param vector2 Vector2.
     * @return Cross-product result.
     */
    static Vector multiply(Vector vector1, Vector vector2) {
        double vector1AngleRadians = (vector1.angleDegrees / 180.0 * Math.PI);
        double xVector1 = (vector1.velocity) * Math.sin(vector1AngleRadians);
        double yVector1 = (vector1.velocity) * Math.cos(vector1AngleRadians);

        double vector2AngleRadians = (vector2.angleDegrees / 180.0 * Math.PI);
        double xVector2 = (vector2.velocity) * Math.sin(vector2AngleRadians);
        double yVector2 = (vector2.velocity) * Math.cos(vector2AngleRadians);

        float xFinal = (float) (xVector1 + xVector2);
        float yFinal = (float) (yVector1 + yVector2);

        return new Vector(xFinal, yFinal);
    }

}
