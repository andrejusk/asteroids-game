package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends GameThread {

    private MovableObject debugObject;

    private StaticObject touchReference;
    private Player player;

    long lastKey;

    /**
     * Set up game.
     *
     * @param gameView GameView
     */
    Game(GameView gameView) {
        super(gameView);
        initialise();
    }

    private void initialise() {
        touchReference = new StaticObject(canvasWidth / 3, canvasHeight / 2 * 3);
        debugObject = new MovableObject(50, 50, canvasWidth, canvasHeight, 45, 10);
        player = new Player(canvasWidth, canvasHeight);
        lastKey = System.currentTimeMillis();
    }

    /**
     * This is run before a new game (also after an old game).
     */
    @Override
    public void setupBeginning() {
        initialise();
    }

    @Override
    protected void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        super.draw(canvas);

        touchReference.draw(canvas);
        debugObject.draw(canvas);
        player.draw(canvas);
    }


    /**
     * Runs on screen touch
     */
    @Override
    protected void actionOnTouch(MotionEvent e) {

        /* Left side of screen - movement */
        if (e.getRawX() < canvasWidth / 2) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                touchReference = new StaticObject(e.getRawX(), e.getRawY());
                player.thrusting = true;
            }
            if (e.getAction() == MotionEvent.ACTION_MOVE) {
                StaticObject target = new StaticObject(e.getRawX(), e.getRawY());
                player.updateAngle(touchReference, target);
            }
            if (e.getAction() == MotionEvent.ACTION_UP) {
                player.thrusting = false;
            }
        } else {
            player.thrusting = false;
        }

    }

    @Override
    protected void actionOnKey(KeyEvent event) {
        /* Angle */
        if (event.getKeyCode() == KeyEvent.KEYCODE_A) {
            player.turningLeft = (event.getAction() != KeyEvent.ACTION_UP);
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_D) {
            player.turningRight = (event.getAction() != KeyEvent.ACTION_UP);
        }

        /* Velocity */
        if (event.getKeyCode() == KeyEvent.KEYCODE_W) {
            player.thrusting = (event.getAction() != KeyEvent.ACTION_UP);
        }
    }


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {
        /*
        //If the ball moves down on the screen
        if(ball.ySpeed > 0) {
            //Check for a paddle collision
            updateBallCollision(paddle.x, canvasHeight);
        }

        ball.move(secondsElapsed);
        paddle.move(secondsElapsed);

        //Check if the ball hits either the left side or the right side of the screen
        //But only do something if the ball is moving towards that side of the screen
        //If it does that => change the direction of the ball in the X direction
        if((ball.x <= ball.bitmap.getWidth() / 2 && ball.xSpeed < 0) || (ball.x >= canvasWidth - ball.bitmap.getWidth() / 2 && ball.xSpeed > 0) ) {
            ball.xSpeed = -ball.xSpeed;
        }

        //Check for SmileyBall collision
        if(updateBallCollision(smileyBall.x, smileyBall.y)) {
            //Increase score
            increaseScore(1);
        }

        //Loop through all SadBalls
        for (Obstacle obstacle : sadBalls) {
            //Perform collisions (if necessary) between SadBall in position i and the red ball
            updateBallCollision(obstacle.x, obstacle.y);
        }

        //If the ball goes out of the top of the screen and moves towards the top of the screen =>
        //change the direction of the ball in the Y direction
        if(ball.y <= ball.bitmap.getWidth() / 2 && ball.ySpeed < 0) {
            ball.ySpeed = -ball.ySpeed;
        }

        //If the ball goes out of the bottom of the screen => lose the game
        if(ball.y >= canvasHeight) {
            setState(GameThread.STATE_LOSE);
        }
        */
        debugObject.move(secondsElapsed);
        player.move(secondsElapsed);
    }

    /*
    //Collision control between mBall and another big ball
    private boolean updateBallCollision(float x, float y) {
        //Get actual distance (without square root - remember?) between the mBall and the ball being checked
        float distanceBetweenBallAndPaddle = (x - ball.x) * (x - ball.x) + (y - ball.y) *(y - ball.y);

        //Check if the actual distance is lower than the allowed => collision
        if(mMinDistanceBetweenBallAndPaddle >= distanceBetweenBallAndPaddle) {
            //Get the present speed (this should also be the speed going away after the collision)
            float speedOfBall = (float) Math.sqrt(ball.xSpeed * ball.xSpeed + ball.ySpeed * ball.ySpeed);

            //Change the direction of the ball
            ball.xSpeed = ball.x - x;
            ball.ySpeed = ball.y - y;

            //Get the speed after the collision
            float newSpeedOfBall = (float) Math.sqrt(ball.xSpeed * ball.xSpeed + ball.ySpeed * ball.ySpeed);

            //using the fraction between the original speed and present speed to calculate the needed
            //velocities in X and Y to get the original speed but with the new angle.
            ball.xSpeed = ball.xSpeed * speedOfBall / newSpeedOfBall;
            ball.ySpeed = ball.ySpeed * speedOfBall / newSpeedOfBall;

            return true;
        }
        return false;

    }
    */
}