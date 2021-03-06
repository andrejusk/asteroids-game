package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Game class.
 */
public class Game extends GameThread {

    /* Store MotionObjects in Game */
    private ArrayList<MotionObject> objects;

    /* Player object */
    private Player player;

    /* Player controls */
    private PlayerInput joystick;
    private PlayerInput thrust, shoot;

    /* Game Bitmaps */
    private final Bitmap playerNormal, playerThrust, asteroid, bullet;

    /* Player control locations */
    private final static float joyX = (float) (1.0 / 3.0);
    private final static float joyY = (float) (2.0 / 3.0);
    private final static float thrustX = (float) (0.70);
    private final static float thrustY = (float) (0.75);
    private final static float shootX = (float) (2.6 / 3.0);
    private final static float shootY = (float) (2.0 / 3.0);

    /* Score values */
    private final static long asteroidPoints = 100;

    /* Sound thread */
    private SoundThread soundThread;

    /**
     * Set up game.
     *
     * @param gameView GameView
     */
    Game(GameView gameView) {
        super(gameView);

        playerNormal = BitmapFactory.decodeResource(
                gameView.getContext().getResources(),
                R.drawable.spaceship);
        playerThrust = BitmapFactory.decodeResource(
                gameView.getContext().getResources(),
                R.drawable.spaceship_thrust);

        asteroid = BitmapFactory.decodeResource(
                gameView.getContext().getResources(),
                R.drawable.asteroid);
        bullet = BitmapFactory.decodeResource(
                gameView.getContext().getResources(),
                R.drawable.asteroid);

        soundThread = new SoundThread(context);
        soundThread.start();
    }

    /**
     * This is run before a new game (also after an old game).
     */
    @Override
    public void setupBeginning() {
        /* Create controls */
        createJoystick(canvasWidth * joyX, canvasHeight * joyY);
        thrust = new PlayerInput(canvasWidth * thrustX, canvasHeight * thrustY,
                canvasWidth, canvasHeight, PlayerInput.TYPE.THRUST);
        shoot = new PlayerInput(canvasWidth * shootX, canvasHeight * shootY,
                canvasWidth, canvasHeight, PlayerInput.TYPE.SHOOT);

        /* Create Player and MotionObjects */
        player = new Player(canvasWidth, canvasHeight, playerNormal, playerThrust);
        objects = new ArrayList<>();
    }

    /**
     * Draws Game.
     * @param canvas Canvas to draw to.
     */
    @Override
    protected void draw(Canvas canvas) {
        /* No canvas */
        if (canvas == null) {
            return;
        }

        /* Draw background */
        super.draw(canvas);

        /* Don't draw if waiting */
        if (super.gameState == STATE.MENU) {
            return;
        }

        /* Draw Player */
        player.draw(canvas);

        /* Draw MotionObjects */
        for (MotionObject object : objects) {
            object.draw(canvas);
        }

        if (super.gameState != STATE.DEAD) {
            /* Overlay controllers */
            joystick.draw(canvas);
            thrust.draw(canvas);
            shoot.draw(canvas);
        }

    }

    /**
     * Creates Joystick at given location.
     * @param x X location.
     * @param y Y location.
     */
    private void createJoystick(float x, float y) {
        joystick = new PlayerInput(x, y, canvasWidth, canvasHeight, PlayerInput.TYPE.JOYSTICK);
    }


    /**
     * Handles touch events.
     * @param e MotionEvent to handle.
     */
    @Override
    protected boolean actionOnTouch(MotionEvent e) {

        if (super.gameState != STATE.RUNNING) {
            return false;
        }

        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        int action = e.getActionMasked();

        /* Touch information */
        float x = e.getX(pointerIndex);
        float y = e.getY(pointerIndex);

        /* Finger lifted up */
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            /* If thrust finger */
            if (thrust.isPointer(pointerId)) {
                player.thrusting = false;
                thrust.active = false;
                soundThread.stopEngine = true;
            }
            /* If thrust finger */
            else if (shoot.isPointer(pointerId)) {
                shoot.active = false;
            }
            /* If joystick finger */
            else if (joystick.isPointer(pointerId)) {
                joystick.active = false;
            }
        }

        /* Finger moved */
        else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_HOVER_MOVE) {
            /* The pointerId is always zero on ACTION_MOVE, have to find it manually */
            int pointerCount = e.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                pointerIndex = i;
                pointerId = e.getPointerId(pointerIndex);
                /* Joystick finger */
                if (joystick.isPointer(pointerId)) {
                    x = e.getX(pointerIndex);
                    y = e.getY(pointerIndex);
                    Object target = new Object(x, y);
                    player.updateAngle(joystick, target);
                }
            }
        }

        /* Finger pressed down */
        else if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            /* Left side of screen */
            if (x < canvasWidth / 2) {
                createJoystick(x, y);
                joystick.active = true;
                joystick.pointerId = pointerId;
            }
            /* Right side of screen */
            else {
                /* Thrust button */
                if (thrust.isAffected(x, y)) {
                    player.thrusting = true;
                    thrust.active = true;
                    thrust.pointerId = pointerId;
                    soundThread.startEngine = true;
                }
                /* Shoot button */
                else if (shoot.isAffected(x, y)) {
                    shoot.pointerId = pointerId;
                    shoot.active = true;
                    PlayerBullet bullet = new PlayerBullet(player, this.bullet);
                    objects.add(bullet);
                    soundThread.playFire = true;
                }
            }
        }

        return true;

    }

    /**
     * Handles keyboard events.
     * @param event KeyEvent to handle.
     */
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


    /**
     * This is run just before the game "scenario" is printed on the screen.
     */
    @Override
    protected void updateGame(float secondsElapsed) {
        /* Move player */
        player.move(secondsElapsed);

        /* Move objects */
        for (MotionObject object : objects) {
            object.move(secondsElapsed);
        }

        /* Check collisions */
        for (int i = 0; i < objects.size(); i++) {
            /* Get object */
            MotionObject object = objects.get(i);

            /* If Player hits Asteroid */
            if (object instanceof Asteroid && collides(player, object)) {
                if (lives > 0) {
                    updateLives(lives - 1);
                    super.setState(STATE.DEAD);
                } else {
                    super.setState(STATE.FINISH);
                }
                soundThread.playDeath = true;
            }

            /* If object collides any other object */
            for (int j = 0; j < objects.size(); j++) {
                /* Get target */
                MotionObject target = objects.get(j);

                /* Check collision */
                if (collides(object, target)) {
                    /* PlayerBullet hits Asteroid */
                    if (object instanceof PlayerBullet && target instanceof Asteroid) {
                        /* Replace with new Asteroid objects */
                        objects.set(i, new Asteroid((Asteroid) target, object, true));
                        objects.set(j, new Asteroid((Asteroid) target, object, false));
                        /* Increment score */
                        super.increaseScore(asteroidPoints);
                        soundThread.playHit = true;
                    }
                }
            }
        }

        /* Clean up memory */
        int count = objects.size();
        int iterator = 0;
        while (iterator < count) {
            if (objects.get(iterator).exitedBounds) {
                objects.remove(iterator);
                count--;
            } else {
                iterator++;
            }
        }

        if (difficulty != DIFFICULTY.EASY) {
            /* Create objects */
            Random random = new Random(Double.doubleToLongBits(Math.random()));
            if (random.nextFloat() < 0.01) {
                objects.add(new Asteroid(canvasWidth, canvasHeight, asteroid));
            }
            if (difficulty == DIFFICULTY.HARD) {
                /* Create more objects */
                if (random.nextFloat() < 0.01) {
                    objects.add(new Asteroid(canvasWidth, canvasHeight, asteroid));
                }
            }
        }

    }

    /**
     * Cleans before changing states.
     */
    void cleanState() {
        soundThread.stopEngine = true;
    }

    /**
     * Performs collision detection.
     * @param object Object to test.
     * @param target Target to test against.
     * @return Whether objects collide.
     */
    private boolean collides(Object object, Object target) {
        /* Ignore self */
        if (object == target) {
            return false;
        }
        /* Returns if distance is less than radii */
        return
                (Math.pow((object.x + object.size / 2) - (target.x + target.size / 2), 2)
                        + Math.pow((object.y + object.size / 2) - (target.y + target.size / 2), 2))
                < Math.pow(object.size / 2 + target.size / 2, 2);
    }

}