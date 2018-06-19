package de.ect.home.automation.music.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.SerializationUtils;

import de.ect.home.commons.AudioFormatDTO;

/**
 * player class for streaming music to a speaker
 * 
 * @author ctr
 */
public class Player {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

	@Async
	public void play(File file, Speaker speaker, int port) {
		AudioInputStream rawInput = null;
		AudioInputStream decodedInput = null;
		
		try (InputStream in = new FileInputStream(file)) {
			Socket socket = SocketFactory.getDefault().createSocket(speaker.getIp(), port);
			
			// TODO: loop through folder and only change format if need be...
			rawInput = AudioSystem.getAudioInputStream(in);
			
			AudioFormat baseFormat = rawInput.getFormat();
			AudioFormatDTO afDto = new AudioFormatDTO(baseFormat.getSampleRate(), 
					16, baseFormat.getChannels(), baseFormat.getChannels() * 2, 
					baseFormat.getSampleRate(), false);
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
					baseFormat.getSampleRate(), // sample rate (same as base format)
					16, // sample size in bits
					baseFormat.getChannels(), // # of Channels
					baseFormat.getChannels() * 2, // Frame Size
					baseFormat.getSampleRate(), // Frame Rate
					false // Big Endian
			);
			decodedInput = AudioSystem.getAudioInputStream(decodedFormat, rawInput);
		
			OutputStream out = socket.getOutputStream();
			
			// write decoded format so that the player on the other end knows how to handle the stream
			out.write(SerializationUtils.serialize(afDto));
			out.write("\r\n".getBytes());
			
			int count;
			byte[] buffer = new byte[1024];
			while ((count = decodedInput.read(buffer)) > 0)
			{
			  out.write(buffer, 0, count);
			  out.write("\r\n".getBytes());
			}
			out.write("eof\r\n".getBytes());
			socket.close();
		}
		catch (IOException | UnsupportedAudioFileException e) {
			LOGGER.error("Unable to stream data to speaker {}", speaker.getName(), e);
		}
		finally {
			try {
				if (rawInput != null) {
					rawInput.close();
				}
				if (decodedInput != null) {
					decodedInput.close();
				}
			}
			catch (IOException e) {
				// nothing to do here
				LOGGER.error("Unable to close streams", e);
			}
		}
	}
	
	// TODO: make it stoppable
}
