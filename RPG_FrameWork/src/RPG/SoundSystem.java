/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RPG;

import java.io.IOException;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;

/**
 *
 * @author Gregory Salazar
 */
public final class SoundSystem {

    public static void playSound(String soundName) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.Resource.loadSound(soundName)); //open an audio input stream and load sound file from resources
            Clip clip = AudioSystem.getClip();  //get a new sound clip resource
            clip.open(audioIn); //open audio clip
            clip.start();   //play the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public static void playMusic(String musicName) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.Resource.loadMusic(musicName)); //open an audio input stream and load music file from resources
            Clip clip = AudioSystem.getClip();  //get a new sound clip resource
            clip.open(audioIn); //open audio clip
            clip.start();   //play the music
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public static void printSupportedFileTypes() {
        //prints all the audio file types this system supports
        System.out.println("This sound system supports the following audio types:");
        for (Type t : AudioSystem.getAudioFileTypes()) {
            System.out.println("   ." + t.getExtension());
        }
    }
}
