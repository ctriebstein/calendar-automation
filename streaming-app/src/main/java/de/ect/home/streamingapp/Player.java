package de.ect.home.streamingapp;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.Callable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ect.home.commons.AudioFormatDTO;

/**
 * the actual player of the app. reads all data that is being put into the stream. therefore the stream needs to be 
 * connected to a pipedOutputStream. Also indicates if it is still playing
 * 
 * @author ctr
 */
public class Player implements Callable<Object>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);
	
	private PipedInputStream pin = new PipedInputStream();
	private boolean isPlaying = false;
	private AudioFormatDTO format = null;
	
	public Player(AudioFormatDTO format) {
		this.format = format;
	}
	
	public PipedInputStream getInputStream() {
		return this.pin;
	}
	
	public boolean isPlaying() {
		return this.isPlaying;
	}
	
	public AudioFormatDTO getFormat() {
		return this.format;
	}
	
	@Override
	public Object call() throws Exception {
		isPlaying = true;
		AudioFormat format = this.format.toAudioFormat();

		SourceDataLine sourceDataLine = null;
		
		// Get an input stream on the byte array containing the data
        try (AudioInputStream audioInputStream = new AudioInputStream(pin, format, AudioSystem.NOT_SPECIFIED)) {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();

            playStream(audioInputStream, sourceDataLine);

            // Block and wait for internal buffer of the data line to empty.
            sourceDataLine.drain();
            sourceDataLine.close();
        } 
        catch (IOException e) {
        	LOGGER.error("Unable to read stream", e);
        }
        catch (LineUnavailableException e) {
            LOGGER.error("Line unavailable", e);
        }
        finally {
        	if (sourceDataLine != null) {
        		sourceDataLine.close();
        	}
        }
        isPlaying = false;
        
		return null;
	}
	
	private void playStream(AudioInputStream audioInputStream, SourceDataLine sourceDataLine) {
		int cnt;
		byte[] tempBuffer = new byte[1024];
		try {
		    while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
		        if (cnt > 0) {
		            // Write data to the internal buffer of the data line where it will be delivered to the speaker.
		            sourceDataLine.write(tempBuffer, 0, cnt);
		        }
		    }
		} 
		catch (IOException e) {
			// this exception occurs after this player is "abandoned" by the connector. it will terminate this player
		    LOGGER.debug("Playing stream interrupted", e);
		}
	}
}
