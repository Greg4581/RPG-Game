/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gregory Salazar
 */
public class Animation {

    private int frameCount = 0;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame = 0;               // animations current frame
    private final int animationDirection = 1;         // animation direction (i.e counting forward or backward)
    private final int totalFrames;                // total amount of frames for your animation

    private boolean stopped = true;                // has animations stopped

    private final List<Frame> frames = new ArrayList<>();    // Arraylist of frames 

    public Animation(BufferedImage[] frames, int frameDelay) {

        this.frameDelay = frameDelay;

        for (BufferedImage frame : frames) {
            addFrame(frame, frameDelay);
        }

        this.frameDelay = frameDelay;
        this.totalFrames = this.frames.size();

    }

    public void start() {
        if (!stopped || frames.isEmpty()) {
            return;
        }
        stopped = false;
    }

    public void stop() {
        if (frames.isEmpty()) {
            return;
        }
        stopped = true;
    }

    public void restart() {
        if (frames.isEmpty()) {
            return;
        }
        stopped = false;
        currentFrame = 0;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
        if (!stopped) {
            frameCount++;

            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;

                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                } else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                }
            }
        }
    }
}
