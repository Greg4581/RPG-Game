/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import static RPG.Main.Resource;
import java.util.concurrent.ConcurrentHashMap;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.*;

/**
 *
 * @author Gregory Salazar
 */
public final class SoundSystem {

    private static Clip activeMusic = null;
    private static final ConcurrentHashMap<Clip, Boolean> activeSounds = new ConcurrentHashMap<>();

    private static boolean soundsPaused, musicPaused, musicLooped;

    public static void playSound(String soundName) {
        playSound(soundName, false);    //sounds not looped by default
    }

    public static void playSound(String soundName, boolean looped) {
        playSound(Resource.loadSound(soundName), looped);
    }

    public static void playSound(Clip clip) {
        playSound(clip, false);    //sounds not looped by default
    }

    public static void playSound(Clip clip, boolean looped) {
        if (clip == null || !clip.isOpen()) {
            System.err.println(SoundSystem.class.getSimpleName() + " ERROR: Clip does not exist or is not open.");
            return;
        }
        if (soundsPaused) {
            System.err.println(SoundSystem.class.getSimpleName() + " ERROR: Make sure sounds are unpaused before playing a new one.");
            return;
        }
        activeSounds.put(clip, looped);  //adds each sound to the map
        if (looped) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        clip.start();   //play the sound
    }

    public static void pauseSounds() {
        activeSounds.keySet().stream().forEach((sound) -> {
            if (soundOpen(sound)) {
                sound.stop();
            } else {
                activeSounds.remove(sound);
            }
        });
        soundsPaused = true;
    }

    public static void unpauseSounds() {
        activeSounds.keySet().stream().forEach((sound) -> {
            if (soundOpen(sound)) {
                if (activeSounds.get(sound)) {
                    //redefine looping after unpausing
                    sound.loop(Clip.LOOP_CONTINUOUSLY);
                }
                sound.start();
            } else {
                activeSounds.remove(sound);
            }
        });
        soundsPaused = false;
    }

    public static void stopSounds() {
        activeSounds.keySet().stream().forEach((sound) -> {
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
        num = activeSounds.keySet().stream().filter((sound) -> (soundOpen(sound))).map((_item) -> 1).reduce(num, Integer::sum);
        return num;
    }

    public static void playMusic(String musicName) {
        playMusic(musicName, true);    //music looped by default
    }

    public static void playMusic(String musicName, boolean looped) {
        playMusic(Resource.loadMusic(musicName), looped);
    }

    public static void playMusic(Clip clip) {
        playMusic(clip, true);    //music looped by default
    }

    public static void playMusic(Clip clip, boolean looped) {
        if (clip == null || !clip.isOpen()) {
            System.err.println(SoundSystem.class.getSimpleName() + " ERROR: Clip does not exist or is not open.");
            return;
        }
        stopMusic();    //stop any other music currently playing
        activeMusic = clip;
        musicLooped = looped;
        if (looped) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        clip.start();   //play the music
        musicPaused = false;
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
            if (musicLooped) {
                //redefine looping after unpausing
                activeMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
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
