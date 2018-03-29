package uk.ac.reading.student.akostarevas.asteroids;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class TheGame extends GameThread{

    private Ball ball;
    private Ball paddle;

    private Objective smileyBall;
    private Obstacle[] sadBalls;

    Bitmap mBall, mPaddle, mSmileyBall, mSadBall;

    private float mMinDistanceBetweenBallAndPaddle = 0;

    //This is run before anything else, so we can prepare things here
    public TheGame(GameView gameView) {
        /* House keeping */
        super(gameView);

        //Prepare the image so we can draw it on the screen (using a canvas)
        mBall = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.small_red_ball);

        //Prepare the image of the paddle so we can draw it on the screen (using a canvas)
        mPaddle = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.yellow_ball);

        //Prepare the image of the SmileyBall so we can draw it on the screen (using a canvas)
        mSmileyBall =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.smiley_ball);

        //Prepare the image of the SadBall(s) so we can draw it on the screen (using a canvas)
        mSadBall =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.sad_ball);

        reset();
    }

    void reset() {
        ball = new Ball(mBall,
                mCanvasWidth / 2, mCanvasHeight / 2,
                mCanvasWidth / 3, mCanvasHeight / 3);

        paddle = new Ball(mPaddle, mCanvasWidth / 2, mCanvasHeight);

        smileyBall = new Objective(mSmileyBall, mCanvasWidth / 2, mSmileyBall.getHeight() / 2);

        sadBalls = new Obstacle[3];
        sadBalls[0] = new Obstacle(mSadBall, mCanvasWidth / 3, mCanvasHeight / 3);
        sadBalls[1] = new Obstacle(mSadBall, mCanvasWidth - mCanvasWidth / 3, mCanvasHeight / 3);
        sadBalls[2] = new Obstacle(mSadBall, mCanvasWidth / 2, mCanvasHeight / 5);

        //Get the minimum distance between a small ball and a bigball
        //We leave out the square root to limit the calculations of the program
        //Remember to do that when testing the distance as well
        mMinDistanceBetweenBallAndPaddle = (mPaddle.getWidth() / 2 + mBall.getWidth() / 2) * (mPaddle.getWidth() / 2 + mBall.getWidth() / 2);
    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {
        reset();
    }

    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to do nothing
        //It is ok not understanding what is happening here
        if (canvas == null) return;

        //House keeping
        super.doDraw(canvas);

        //canvas.drawBitmap(bitmap, x, y, paint) uses top/left corner of bitmap as 0,0
        //we use 0,0 in the middle of the bitmap, so negate half of the width and height of the ball to draw the ball as expected
        //A paint of null means that we will use the image without any extra features (called Paint)

        //draw the image of the ball using the X and Y of the ball
        ball.draw(canvas);
        paddle.draw(canvas);
        smileyBall.draw(canvas);
        for (Obstacle obstacle : sadBalls) {
            obstacle.draw(canvas);
        }

    }


    //This is run whenever the phone is touched by the user
    @Override
    protected void actionOnTouch(float x, float y) {
        //Move the ball to the x position of the touch
        paddle.x = x;
    }


    //This is run whenever the phone moves around its axises
    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        //Change the paddle speed
        paddle.xSpeed = paddle.xSpeed + 70f * xDirection;

        //If paddle is outside the screen and moving further away
        //Move it into the screen and set its speed to 0
        if(paddle.x <= 0 && paddle.xSpeed < 0) {
            paddle.xSpeed = 0;
            paddle.x = 0;
        }
        if(paddle.x >= mCanvasWidth && paddle.xSpeed > 0) {
            paddle.xSpeed = 0;
            paddle.x = mCanvasWidth;
        }

    }


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {
        //If the ball moves down on the screen
        if(ball.ySpeed > 0) {
            //Check for a paddle collision
            updateBallCollision(paddle.x, mCanvasHeight);
        }

        ball.move(secondsElapsed);
        paddle.move(secondsElapsed);

        //Check if the ball hits either the left side or the right side of the screen
        //But only do something if the ball is moving towards that side of the screen
        //If it does that => change the direction of the ball in the X direction
        if((ball.x <= ball.bitmap.getWidth() / 2 && ball.xSpeed < 0) || (ball.x >= mCanvasWidth - ball.bitmap.getWidth() / 2 && ball.xSpeed > 0) ) {
            ball.xSpeed = -ball.xSpeed;
        }

        //Check for SmileyBall collision
        if(updateBallCollision(smileyBall.x, smileyBall.y)) {
            //Increase score
            updateScore(1);
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
        if(ball.y >= mCanvasHeight) {
            setState(GameThread.STATE_LOSE);
        }

    }

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
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
