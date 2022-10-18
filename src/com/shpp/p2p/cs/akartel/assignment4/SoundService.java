package com.shpp.p2p.cs.akartel.assignment4;

/**
 * class create objects from com.shpp.p2p.cs.akartel.assignment4.Sounds with link to our wav file
 * also slow speed of wav sample
 */
public class SoundService {

    Sounds fight = new Sounds("src/resource/sounds/fight.wav", 2);
    Sounds holyshit = new Sounds("src/resource/sounds/holyshit.wav", 2);
    Sounds paddleHit = new Sounds("src/resource/sounds/paddleHit.wav");
    Sounds unstoppable = new Sounds("src/resource/sounds/unstoppable.wav");
    Sounds you_lose = new Sounds("src/resource/sounds/you_lose.wav", 2);
    Sounds you_win = new Sounds("src/resource/sounds/you_win.wav", 2);

    /**
     * play brick sound we create new value every time
     * to hear it while several bricks hit in a row
     */
    public void playBrickSound() {
        Sounds brickHit = new Sounds("src/resource/sounds/brickHit.wav");
        new Thread(brickHit).start();
    }

    /**
     * play wav file via thread until it ends
     * same for methods below
     */
    public void playFightSound() {
        new Thread(fight).start();
    }

    public void playHolyshitSound() {
        new Thread(holyshit).start();
    }

    public void playPaddleHitSound() {
        new Thread(paddleHit).start();
    }

    public void playUnstoppableSound() {
        new Thread(unstoppable).start();
    }


    public void playLoseSound() {
        new Thread(you_lose).start();
    }

    public void playWinSound() {
        new Thread(you_win).start();
    }
}
