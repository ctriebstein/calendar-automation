package de.ect.home.streamingapp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import de.ect.home.commons.AudioFormatDTO;

@Component
public class SpeakerServiceConnector implements MessageHandler, Callable<Object> {
	
	private List<Byte> song = new ArrayList<>();
	
	private AudioFormatDTO format = null;
	private boolean isPlaying = false;
	private ExecutorService es = Executors.newCachedThreadPool();
	private PipedInputStream pin = new PipedInputStream();
	private PipedOutputStream out = new PipedOutputStream();
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		byte[] bytes = (byte[]) message.getPayload();
		
		Object obj = getDeserializedObject(bytes);
		if (obj != null && obj instanceof AudioFormatDTO) {
			this.format = (AudioFormatDTO) obj;
		}
		else {
//			String str = new String(bytes);
//			if (str.equalsIgnoreCase("eof")) {
//				playSong();
//			} else {
//				for (byte b : bytes) {
//					song.add(b);
//				}
//			}
			try {
				if (!isPlaying) {
					es.submit(this);
					out.connect(pin);
				}
			
				out.write(bytes);
				out.flush();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private Object getDeserializedObject(byte[] bytes) {
		try {
			return SerializationUtils.deserialize(bytes);
		}
		catch (Exception e) {
			return null;
		}
	}

	@Async
	private void playSong() {
		try {
			isPlaying = true;
			Byte[] bytes = song.toArray(new Byte[song.size()]);

		    AudioInputStream stream = new AudioInputStream(
		        pin, //new ByteArrayInputStream(ArrayUtils.toPrimitive(bytes)), 
		        new AudioFormat(
		        		AudioFormat.Encoding.PCM_SIGNED, 
		        		this.format.getSampleRate(),
		        		this.format.getSampleSizeInBits(),
		        		this.format.getChannels(),
		        		this.format.getFrameSize(),
		        		this.format.getFrameRate(),
		        		this.format.isBigEndian()),
		        bytes.length
		    );
		    
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void playStream() {
		AudioFormat format = new AudioFormat(
        		AudioFormat.Encoding.PCM_SIGNED, 
        		this.format.getSampleRate(),
        		this.format.getSampleSizeInBits(),
        		this.format.getChannels(),
        		this.format.getFrameSize(),
        		this.format.getFrameRate(),
        		this.format.isBigEndian());
		AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        try {
            
            // Get an input stream on the byte array
            // containing the data
            audioInputStream = new AudioInputStream(pin, format, AudioSystem.NOT_SPECIFIED);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[1024];
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }// end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
	}

	@Override
	public Object call() throws Exception {
		isPlaying = true;
		Thread.sleep(1000);
		playStream();
		return null;
	}
}
