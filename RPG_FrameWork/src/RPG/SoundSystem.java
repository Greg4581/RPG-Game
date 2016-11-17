/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;

/**
 *
 * @author Gregory Salazar
 */
public final class SoundSystem {

    private static Clip activeMusic = null;
    private static final ArrayList<Clip> activeSounds = new ArrayList();

    private static boolean soundsPaused, musicPaused;

    public static void playSound(String soundName) {
        playSound(soundName, false);    //sounds not looped by default
    }

    public static void playSound(String soundName, boolean looped) {
        if (soundsPaused) {
            System.err.println("ERROR: Make sure sounds are unpaused before playing a new one.");
            return;
        }
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.Resource.loadSound(soundName)); //open an audio input stream and load sound file from resources
            Clip clip = AudioSystem.getClip();  //get a new sound clip resource
            clip.open(audioIn); //open audio clip
            if (looped) {   //loop if specified
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            activeSounds.add(0, clip);  //adds each sound to the list, ordered from newest to oldest
            clip.start();   //play the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public static void pauseSounds() {
        activeSounds.stream().forEach((sound) -> {
            if (soundOpen(sound)) {
                sound.stop();
            } else {
                activeSounds.remove(sound);
            }
        });
        soundsPaused = true;
    }

    public static void unpauseSounds() {
        activeSounds.stream().forEach((sound) -> {
            if (soundOpen(sound)) {
                sound.start();
            } else {
                activeSounds.remove(sound);
            }
        });
        soundsPaused = false;
    }

    public static void stopSounds() {
        activeSounds.stream().forEach((sound) -> {
            if (soundOpen(sound)) {
                sound.close();
            }
        });
        activeSounds.clear();
        soundsPaused = false;
    }

    public static boolean soundsPaused() {
        //returns true if sounds are paused, else returns false
        return soundsPaused;
    }

    public static int numActiveSounds() {
        //returns the number of active sounds (paused sounds are still counted as active)
        int num = 0;
        num = activeSounds.stream().filter((sound) -> (soundOpen(sound))).map((_item) -> 1).reduce(num, Integer::sum);
        return num;
    }

    public static void playMusic(String musicName) {
        playMusic(musicName, true);    //music looped by default
    }

    public static void playMusic(String musicName, boolean looped) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.Resource.loadMusic(musicName)); //open an audio input stream and load music file from resources
            Clip clip = AudioSystem.getClip();  //get a new sound clip resource
            clip.open(audioIn); //open audio clip
            if (looped) {   //loop if specified
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            musicCheck();
            if (activeMusic != null) {
                //stop any other music currently playing
                stopMusic();
            }
            activeMusic = clip;
            clip.start();   //play the music
            musicPaused = false;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public static void pauseMusic() {
        musicCheck();
        if (activeMusic != null) {
            activeMusic.stop();
        }
        musicPaused = true;
    }

    public static void unpauseMusic() {
        musicCheck();
        if (activeMusic != null) {
            activeMusic.start();
        }
        musicPaused = false;
    }

    public static void stopMusic() {
        musicCheck();
        if (activeMusic != null) {
            activeMusic.close();
            activeMusic = null;
        }
        musicPaused = false;
    }

    public static boolean musicPaused() {
        //returns true if music is paused, else returns false
        return musicPaused;
    }

    public static void printSupportedFileTypes() {
        //prints all the audio file types this system supports
        System.out.println("This sound system supports the following audio types:");
        for (Type t : AudioSystem.getAudioFileTypes()) {
            System.out.println("   ." + t.getExtension());
        }
    }

    private static boolean soundOpen(Clip sound) {
        //returns false if sound is null, or exists but is closed
        //else returns true
        return !(sound == null || !sound.isOpen() || (!sound.isRunning() && !soundsPaused));
    }

    private static void musicCheck() {
        //automatically defreferences activeMusic clip if it exists but is closed
        if (activeMusic == null || !activeMusic.isOpen() || (!activeMusic.isRunning() && !musicPaused)) {
            activeMusic = null;
        }
    }
}
