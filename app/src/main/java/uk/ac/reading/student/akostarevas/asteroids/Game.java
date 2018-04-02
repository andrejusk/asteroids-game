package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

public class Game extends GameThread {

    private ArrayList<MotionObject> objects;

    private PlayerInput joystick;
    private PlayerInput thrust, shoot;

    //TODO: figure out better way of doing this
    private final static float joyX = (float) (1.0 / 3.0);
    private final static float joyY = (float) (2.0 / 3.0);

    private final static float thrustX = (float) (0.70);
    private final static float thrustY = (float) (0.75);

    private final static float shootX = (float) (2.5 / 3.0);
    private final static float shootY = (float) (2.0 / 3.0);

    private final Bitmap playerNormal, playerThrust, asteroid;

    private Player player;

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
    }

    /**
     * This is run before a new game (also after an old game).
     */
    @Override
    public void setupBeginning() {
        createJoystick(canvasWidth * joyX, canvasHeight * joyY);

        thrust = new PlayerInput(canvasWidth * thrustX, canvasHeight * thrustY, PlayerInput.TYPE.THRUST);
        shoot = new PlayerInput(canvasWidth * shootX, canvasHeight * shootY, PlayerInput.TYPE.SHOOT);

        player = new Player(canvasWidth, canvasHeight, playerNormal, playerThrust);

        objects = new ArrayList<>();
    }

    @Override
    protected void draw(Canvas canvas) {
        /* No canvas */
        if (canvas == null) {
            return;
        }

        /* Draw background */
        super.draw(canvas);

        if (super.gameState == STATE.READY) {
            return;
        }

        /* Draw controllers */
        joystick.draw(canvas);
        thrust.draw(canvas);
        shoot.draw(canvas);

        /* Draw player */
        player.draw(canvas);

        /* Draw objects */
        for (MotionObject object : objects) {
            object.draw(canvas);
        }
    }

    private void createJoystick(float x, float y) {
        joystick = new PlayerInput(x, y, PlayerInput.TYPE.JOYSTICK);
    }


    /**
     * Handles touch events.
     * @param e MotionEvent to handle.
     */
    @Override
    protected boolean actionOnTouch(MotionEvent e) {

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
            }
            /* If joystick finger */
            if (joystick.isPointer(pointerId)) {
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
                    GameObject target = new GameObject(x, y);
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
                }
                /* Shoot button */
                else if (shoot.isAffected(x, y)) {
                    PlayerBullet bullet = new PlayerBullet(player);
                    objects.add(bullet);
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
        player.move(secondsElapsed);

        Random random = new Random();
        if (random.nextFloat() < 0.02) {
            objects.add(new Asteroid(canvasWidth, canvasHeight, asteroid));
        }

        for (MotionObject object : objects) {
            object.move(secondsElapsed);
        }
    }

}