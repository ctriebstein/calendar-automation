package de.ect.home.streamingapp;

import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import de.ect.home.commons.AudioFormatDTO;

@Component
public class SpeakerServiceConnector implements MessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpeakerServiceConnector.class);
	
	private ExecutorService es = Executors.newCachedThreadPool();
	private PipedOutputStream out = null;
	private Player player = null;
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		byte[] bytes = (byte[]) message.getPayload();
		
		Object obj = getDeserializedObject(bytes);
		// if a new audio format is sent shut down old player and start a new one
		if (obj != null && obj instanceof AudioFormatDTO) {
			player = new Player((AudioFormatDTO) obj);
		}
		else {
			try {
				// if the player is currently not playing yet, submit a new thread and connect the streams to start playing
				if (player != null && !player.isPlaying()) {
					es.submit(player);
					out = new PipedOutputStream(player.getInputStream());
				}
			
				// write the incoming bytes to the piped output stream
				out.write(bytes);
				out.flush();
			}
			catch (Exception e) {
				LOGGER.error("Unable to play stream", e);
			}
		}
	}

	private Object getDeserializedObject(byte[] bytes) {
		try {
			return SerializationUtils.deserialize(bytes);
		}
		catch (Exception e) {
			LOGGER.debug("Unable to deserialize object", e);
			return null;
		}
	}
}
