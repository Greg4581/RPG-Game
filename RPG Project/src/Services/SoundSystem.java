/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import static RPG.Main.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.*;

/**
 *
 * @author Gregory Salazar
 */
public final class SoundSystem implements Runnable {

    private static Thread thread;
    private static Clip currentTrack = null;
    private static int currentTrackNum;
    private static final List<String> playlist = new ArrayList<>();
    private static final ConcurrentHashMap<Clip, Boolean> activeSounds = new ConcurrentHashMap<>();

    private static boolean soundsPaused, musicPaused;

    public static void playSound(String soundName) {
        playSound(soundName, false);    //sounds not looped by default
    }

    public static void playSound(String soundName, boolean looped) {
        playSound(Resource.loadSound(soundName), looped);
    }

    public static void playSound(Clip clip) {
        playSound(clip, false);    //sounds not looped by default
    }

    public static synchronized void playSound(Clip clip, boolean looped) {
        if (!isValid(clip)) {
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

    public static synchronized void pauseSounds() {
        soundsPaused = true;
        activeSounds.keySet().stream().forEach((sound) -> {
            if (!soundDone(sound)) {
                sound.stop();
            } else {
                activeSounds.remove(sound);
            }
        });
    }

    public static synchronized void unPauseSounds() {
        activeSounds.keySet().stream().forEach((sound) -> {
            if (!soundDone(sound)) {
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

    public static synchronized void stopSounds() {
        activeSounds.keySet().stream().forEach((sound) -> {
            if (!soundDone(sound)) {
                sound.drain();
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

    public static synchronized int numActiveSounds() {
        //returns the number of active sounds (paused sounds are still counted as active)
        int num = 0;
        num = activeSounds.keySet().stream().filter((sound) -> (!soundDone(sound))).map((_item) -> 1).reduce(num, Integer::sum);
        return num;
    }

    public static synchronized void playMusic(String trackName) {
        Clip track = addToPlaylist(trackName);
        if (isValid(track)) {
            stopMusic();
            currentTrack = track;
            currentTrackNum = playlist.indexOf(trackName);
            currentTrack.start();
            musicPaused = false;
        }
    }

    public static synchronized void playNextTrack() {
        //plays the next track in the playlist
        playMusic(playlist.get((currentTrackNum + 1) % playlist.size()));
    }

    public static synchronized void pauseMusic() {
        musicPaused = true;
        if (!musicDone()) {
            currentTrack.stop();
        }
    }

    public static synchronized void stopMusic() {
        if (!musicDone()) {
            currentTrack.drain();
            currentTrack.close();
        }
        currentTrack = null;
        musicPaused = false;
    }

    public static synchronized void unPauseMusic() {
        if (!musicDone()) {
            currentTrack.start();
        }
        musicPaused = false;
    }

    public static synchronized void resetTrack() {
        //resets music track to starting position
        if (!musicDone()) {
            currentTrack.setFramePosition(0);
        }
    }

    public static boolean musicPaused() {
        //returns true if music is paused, else returns false
        return musicPaused;
    }

    public static synchronized Clip addToPlaylist(String trackName) {
        //adds the track to the playlist if valid and if it doesn't already exist in playlist
        //returns the clip object if valid
        Clip track = Resource.loadMusic(trackName);
        if (isValid(track) && !playlist.contains(trackName)) {
            playlist.add(trackName);
        }
        return track;
    }

    public static synchronized void setPlaylist(String[] sPlaylist) {
        playlist.clear();
        playlist.stream().forEach((track) -> {
            addToPlaylist(track);
        });
    }

    public static List<String> getPlaylist() {
        //returns the playlist
        return playlist;
    }

    public static String getCurrentTrack() {
        //returns the current track
        return playlist.get(currentTrackNum);
    }

    public static int getCurrentTrackNum() {
        //returns the current track number
        return currentTrackNum;
    }

    public static void printSupportedFileTypes() {
        //prints all the audio file types this system supports
        System.out.println("This sound system supports the following audio types:");
        for (Type t : AudioSystem.getAudioFileTypes()) {
            System.out.println("   ." + t.getExtension());
        }
    }

    public static void printPlaylist() {
        //prints the playlist
        playlist.stream().forEach((track) -> {
            System.out.println(track);
        });
    }

    private static boolean soundDone(Clip sound) {
        //returns false if the sound is done playing, else returns true
        return (sound == null || !sound.isOpen() || (!sound.isRunning() && !soundsPaused));
    }

    private static boolean musicDone() {
        //returns false if the music track is done playing, else returns true
        return (currentTrack == null || !currentTrack.isOpen() || (!currentTrack.isRunning() && !musicPaused));
    }

    private static boolean isValid(Clip clip) {
        if (clip == null || !clip.isOpen()) {
            System.err.println(SoundSystem.class.getSimpleName() + " ERROR: Clip does not exist or is not open.");
            return false;
        } else {
            return true;
        }
    }

    public static void initialize(String[] playlist) {
        //starts the sound system
        if (thread != null) {
            return;
        }
        thread = new Thread(new SoundSystem(), "SoundSystem");
        thread.setPriority(1);
        for (String track : playlist) {
            addToPlaylist(track);
        }
        currentTrack = Resource.loadMusic(getCurrentTrack());
        currentTrack.start();
        try {
            thread.start();
        } catch (IllegalThreadStateException e) {
            System.err.println("There was an error initializing the sound system.");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SoundSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized (this) {
                if (musicDone() && !musicPaused) {
                    if (!playlist.isEmpty()) {
                        playNextTrack();
                    }
                }
            }
        }
    }
}
