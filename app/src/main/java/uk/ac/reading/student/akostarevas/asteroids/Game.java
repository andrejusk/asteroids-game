package uk.ac.reading.student.akostarevas.asteroids;

//Other parts of the android libraries that we use
import android.graphics.Canvas;

public class Game extends GameThread {

    private MovableObject debugObject;

    private Player player;

    /**
     * Set up game.
     * @param gameView GameView
     */
    Game(GameView gameView) {
        super(gameView);
        initialise();
    }

    private void initialise() {
        debugObject = new MovableObject(50, 50, canvasWidth, canvasHeight, 45, 1000);
        //player = new Player()
        /*
        ball = new Ball(mBall,
                canvasWidth / 2, canvasHeight / 2,
                canvasWidth / 3, canvasHeight / 3);

        paddle = new Ball(mPaddle, canvasWidth / 2, canvasHeight);

        smileyBall = new Objective(mSmileyBall, canvasWidth / 2, mSmileyBall.getHeight() / 2);

        sadBalls = new Obstacle[3];
        sadBalls[0] = new Obstacle(mSadBall, canvasWidth / 3, canvasHeight / 3);
        sadBalls[1] = new Obstacle(mSadBall, canvasWidth - canvasWidth / 3, canvasHeight / 3);
        sadBalls[2] = new Obstacle(mSadBall, canvasWidth / 2, canvasHeight / 5);

        //Get the minimum distance between a small ball and a bigball
        //We leave out the square root to limit the calculations of the program
        //Remember to do that when testing the distance as well
        mMinDistanceBetweenBallAndPaddle = (mPaddle.getWidth() / 2 + mBall.getWidth() / 2) * (mPaddle.getWidth() / 2 + mBall.getWidth() / 2);
        */
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
        debugObject.draw(canvas);
    }


    /**
     * Runs on screen touch
     */
    @Override
    protected void actionOnTouch(float x, float y) {
        //Move the ball to the x position of the touch
        //paddle.x = x;
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