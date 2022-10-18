package com.shpp.p2p.cs.akartel.assignment4;

import acm.graphics.*;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Breakout class
 */
public class Breakout extends WindowProgram {
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 30;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;
    private static final double INCREASE_X_COEFFICIENT = 1.2;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;
    GArc paddle;

    GOval ball;
    double vx, vy; // velocity x and y
    GLabel gameSpeed, points, gameEnd;
    int speed, game, brickCount = 0;
    Color someColor, brickColor = new Color(83, 130, 161);
    SoundService service = new SoundService();


    /**
     * paddle x position depend on mouse movement
     *
     * @param mouseEvent the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        paddle.setLocation(mouseEvent.getX(), getHeight() - PADDLE_Y_OFFSET);
        if (paddle.getX() > getWidth() - paddle.getWidth()) paddle.setLocation(
                getWidth() - paddle.getWidth(), getHeight() - PADDLE_Y_OFFSET);
    }

    /**
     * run breakout game
     */
    public void run() {

        service.playFightSound();

        labelsInvoke();
        ballInvoke();
        bricksInvoke();

        for (game = 0; game < NTURNS; game++) {
            objectsVariablesStartPosition();
            addMouseListeners();
            waitForClick();
            animation();
            remove(paddle); // removing paddle to create new one next round with previous size
        }
        loseStatement();

    }

    /**
     * shows Winner label + sound
     */
    private void winStatement() {
        gameEnd.setLabel("WINNER !!!");
        gameEnd.setLocation(getWidth() / 2d - gameEnd.getWidth() / 2, getHeight() / 2d);

        service.playWinSound();
    }

    /**
     * shows Lose label + sound
     */
    private void loseStatement() {
        if (brickCount < 100) {
            gameEnd.setLabel("LOOSER !!!");
            gameEnd.setLocation(getWidth() / 2d - gameEnd.getWidth() / 2, getHeight() / 2d);

            service.playLoseSound();
        }
    }

    /**
     * create labels
     */
    private void labelsInvoke() {
        gameSpeed = new GLabel(speed + " m/s");
        labelSettings(gameSpeed);
        gameSpeed.setLocation(0, gameSpeed.getHeight());


        points = new GLabel(brickCount + " points");
        labelSettings(points);
        points.setLocation(getWidth() - points.getWidth(), points.getHeight());

        gameEnd = new GLabel("");
        labelSettings(gameEnd);
    }

    /**
     * method to set some params for labels
     *
     * @param label add label with Font and Color parameters
     */
    private void labelSettings(GLabel label) {
        label.setFont("-30");
        label.setColor(someColor);
        add(label);
    }

    /**
     * revert ball and paddle - size and location to previous positions
     * revert speed and labels to previous positions
     */
    private void objectsVariablesStartPosition() {
        speed = 0;
        gameSpeed.setLabel(speed + " m/s");

        paddleInvoke();

        ball.setLocation(getWidth() / 2d - ball.getWidth() / 2, getHeight() / 2d - ball.getHeight() / 2);
        ball.setSize(BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setColor(Color.BLACK);

        vx = RandomGenerator.getInstance().nextDouble(1, 3);
        if (RandomGenerator.getInstance().nextBoolean()) vx = -vx;
        vy = 3;
    }

    /**
     * Build bricks matrix with various colors every 2nd row
     */
    private void bricksInvoke() {
        for (int i = 0; i < NBRICK_ROWS; i++) {
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
                GRect rect = new GRect(BRICK_SEP / 2d + (BRICK_WIDTH + BRICK_SEP) * j,
                        BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * i, BRICK_WIDTH, BRICK_HEIGHT);
                rect.setFilled(true);
                rect.setColor(changeBrickColor(i));
                add(rect);
            }
        }
    }

    private Color changeBrickColor(int i) {
        switch (i % 10) {
            case 0 -> brickColor = Color.RED;
            case 2 -> brickColor = Color.ORANGE;
            case 4 -> brickColor = Color.YELLOW;
            case 6 -> brickColor = Color.GREEN;
            case 8 -> brickColor = Color.CYAN;
        }
        return brickColor;
    }

    /**
     * animation lasts while you lose ball or win
     * ball moving and bouncing from walls and bricks
     * while ball destroy bricks some labels shows user his progress
     */
    private void animation() {
        speed = 100;
        gameSpeed.setLabel(speed + " m/s");
        while (ball.getY() <= getHeight()) {
            wallHit();
            paddleHit();
            brickHitSomeColor();
            brickHit();
            pointsChange();

            ball.move(vx, vy);
            pause(9);
            if (brickCount == NBRICK_ROWS * NBRICK_ROWS) {
                winStatement();
                break;
            }
            ballLostSound();
        }
    }

    private void ballLostSound() {
        if (ball.getY() > getHeight() && game < 2) {
            service.playHolyshitSound();
        }
    }

    /**
     * label shows user his score
     */
    private void pointsChange() {
        points.setLabel(brickCount + " points");
        points.setLocation(getWidth() - points.getWidth(), points.getHeight());
    }

    /**
     * remove brick and add points case brick color == sameColor
     */
    private void brickHitSomeColor() {
        if (getCollidingObject() != null && getCollidingObject().getWidth() == BRICK_WIDTH) {
            if (getCollidingObject().getColor().equals(someColor)) {
                remove(getCollidingObject());
                brickCount++;

                service.playBrickSound();
                if (ball.getColor().equals(Color.BLACK)) {
                    vx = -vx;
                    vy = -vy;
                }
            }
        }
    }

    /**
     * remove bricks via hitting with ball and change direction of ball
     * every brick invoke some changes after hit:
     * CYAN - increasing speed of ball by 5% each time
     * GREEN - increasing paddle size (x2 limitation included)
     * YELLOW - increase ball size (x1.5 limitation)
     * ORANGE - change brick color (you should hit it again to remove)
     * RED - ball become on fire it will not bounce anymore instead just move through bricks and burn
     */
    private void brickHit() {
        if (getCollidingObject() != null && getCollidingObject().getWidth() == BRICK_WIDTH) {

            if (getCollidingObject().getColor().equals(Color.CYAN)) {
                speed += 5;
                gameSpeed.setLabel(speed + " m/s");
                vy = vy + vy * 0.05;
            }

            if (getCollidingObject().getColor().equals(Color.GREEN)) {
                if (paddle.getWidth() < PADDLE_WIDTH * 2) paddle.scale(1.1);
            }

            if (getCollidingObject().getColor().equals(Color.YELLOW)) {
                if (ball.getWidth() < BALL_RADIUS * 3 &&
                        ball.getX() > BALL_RADIUS / 2d &&
                        (ball.getX() + ball.getWidth() - BALL_RADIUS / 2d) < getWidth()) ball.scale(1.07);
            }

            if (getCollidingObject() != null && getCollidingObject().getColor().equals(Color.ORANGE)) {
                getCollidingObject().setColor(someColor);

                service.playBrickSound();
            }

            if (getCollidingObject() != null && getCollidingObject().getColor().equals(Color.RED)) {
                if (ball.getColor().equals(Color.BLACK)) {
                    service.playUnstoppableSound();
                }

                ball.setColor(Color.RED);
            }
//bounce from brick only in case ball is black
            if (ball.getColor().equals(Color.BLACK)) {
                vx = -vx;
                vy = -vy;
            }
            // removing all bricks ball encounter , exception (Orange and someColor) bricks
            if (getCollidingObject() != null && !(getCollidingObject().getColor().equals(Color.ORANGE) ||
                    getCollidingObject().getColor().equals(someColor))) {
                remove(getCollidingObject());
                brickCount++;

                service.playBrickSound();
            }
        }
    }

    /**
     * ball hit paddle and bouncing to various sides relatively to paddle surface it hits
     */

    private void paddleHit() {
        /* this If checks : ball hits paddle
         ball moving down to exclude sticking with paddle
         and case ball already passed paddle location
         */
        if (getCollidingObject() == paddle && vy > 0 && ball.getY() < paddle.getY()) {
            /*
            this if checks : the side ball hit paddle , case ball hits left side of paddle
            and - ball coming from the left we change vx direction and value vx
                - ball coming from the right vx increasing
             */
            if (ball.getX() < (paddle.getX() + paddle.getWidth() / 3 - ball.getWidth()))
                if (vx > 0) vx = RandomGenerator.getInstance().nextDouble(-1, -3);
                else vx = vx * INCREASE_X_COEFFICIENT;
            /*
            this if - vise versa from previous if statement
             */
            if (ball.getX() > (paddle.getX() + paddle.getWidth() * 2 / 3))
                if (vx < 0) vx = RandomGenerator.getInstance().nextDouble(1, 3);
                else vx = vx * INCREASE_X_COEFFICIENT;

            vy = -vy;
            service.playPaddleHitSound();
        }
    }

    /**
     * finding object via 4 points around ball
     *
     * @return object that ball found
     */

    private GObject getCollidingObject() {
        GObject collider = getElementAt(ball.getX(), ball.getY() + ball.getHeight());
        if (collider != null) return collider;
        else if ((collider = getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight())) != null)
            return collider;
        else if ((collider = getElementAt(ball.getX() + ball.getWidth(), ball.getY())) != null) return collider;
        else if ((collider = getElementAt(ball.getX(), ball.getY())) != null) return collider;

        return null;
    }

    /**
     * bounce from walls and floor
     * and exclude cases - ball sticking to the wall
     */
    private void wallHit() {
        if (ball.getX() < 0 && vx < 0) vx = -vx;//if ball hit left wall and vx direction is left ball change direction
        if (ball.getX() + ball.getWidth() > getWidth() && vx > 0) vx = -vx;//vise versa with right side wall
        if (ball.getY() < 0) vy = -vy; //change y direction case floor hit
    }

    /**
     * create ball
     */
    private void ballInvoke() {
        ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setFilled(true);
        add(ball);
    }

    /**
     * create paddle
     */
    private void paddleInvoke() {
        paddle = new GArc(PADDLE_WIDTH, PADDLE_HEIGHT * 2, 0, 180);
        paddle.setLocation(getWidth() / 2d - paddle.getWidth() / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }
}