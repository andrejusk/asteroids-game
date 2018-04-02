package uk.ac.reading.student.akostarevas.asteroids;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends GameThread {

    private MovableObject debugObject;

    private Controller joystick;
    private Controller thrust, shoot;

    //TODO: figure out better way of doing this
    private final static float joyX = (float) (1.0 / 3.0);
    private final static float joyY = (float) (2.0 / 3.0);

    private final static float thrustX = (float) (2.5 / 3.0);
    private final static float thrustY = (float) (2.0 / 3.0);

    private final static float shootX = (float) (0.70);
    private final static float shootY = (float) (0.75);

    private Player player;

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
        createJoystick(canvasWidth * joyX, canvasHeight * joyY);

        thrust = new Controller( canvasWidth * thrustX, canvasHeight * thrustY, Controller.TYPE.BUTTON);
        shoot = new Controller(canvasWidth * shootX, canvasHeight * shootY, Controller.TYPE.BUTTON);

        debugObject = new MovableObject(50, 50, canvasWidth, canvasHeight, 45, 10);
        player = new Player(canvasWidth, canvasHeight);
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
        debugObject.draw(canvas);
    }

    private void createJoystick(float x, float y) {
        joystick = new Controller(x, y, Controller.TYPE.JOYSTICK);
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
                    StaticObject target = new StaticObject(x, y);
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
                    //TODO: shoot
                    shoot.active = true;
                    shoot.pointerId = pointerId;
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
        debugObject.move(secondsElapsed);
        player.move(secondsElapsed);
    }

}