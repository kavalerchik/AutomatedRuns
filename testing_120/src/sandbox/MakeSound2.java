package sandbox;

import java.io.*;
import java.util.Scanner;

import javax.sound.sampled.*;

public class MakeSound2 {
	 public static void main(String args[]) throws Exception
	 {
	//Create an AudioInputStream from a given sound file
	 File f = new File("C:\\Users\\Yair\\Downloads\\Flawless-victory.wav");
	 AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);
	 
	 //Create an AudioInputStream from a given sound file
	 AudioFormat format = audioStream.getFormat();
	 DataLine.Info info = new DataLine.Info(Clip.class, format);
	 
	 //Obtain the Clip: 
	 Clip audioClip = (Clip) AudioSystem.getLine(info);
	 
	 //Open the AudioInputStream and start playing: 
	 audioClip.open(audioStream);
	 audioClip.start();
	 
	 //Close and release resources acquired:
	 audioClip.close();
	 audioStream.close();
	 
	
	 }	
	

}
