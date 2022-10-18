package com.shpp.p2p.cs.akartel.assignment4;

/**
 * com.shpp.p2p.cs.akartel.assignment4.Sounds read some wav files for breakout game
 * and overrating method run to create Threads
 */
public class Sounds implements Runnable {
    private final StdAudio audio = new StdAudio();
    private double[] clip;

    /**
     * primary constructor just read file via com.shpp.p2p.cs.akartel.assignment4.StdAudio object
     * @param file link to file
     */
    public Sounds(String file) {
        clip = StdAudio.read(file);
    }

    /**
     * constructor read file via com.shpp.p2p.cs.akartel.assignment4.StdAudio object
     * @param file link to file
     * @param speedSlower slows wav sample speed
     */
    public Sounds(String file, int speedSlower) {
        clip = StdAudio.read(file);
        double[] result = new double[clip.length * speedSlower];
        for (int i = 0; i < result.length; i++) {
            /* Each new sample is formed by going back to the original array and
             * looking up at half the current position. This, due to rounding
             * down with int division, doubles each entry.
             */
            result[i] = clip[i / speedSlower];
        }
        clip = result;
    }
    @Override
    public void run() {
        audio.play(clip);
    }
}



