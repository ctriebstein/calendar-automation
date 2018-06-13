package de.ect.home.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ect.home.commons.AudioFormatDTO;

@SpringBootApplication
@Configuration
public class AutomationApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AutomationApplication.class, args);
		
		Socket socket = SocketFactory.getDefault().createSocket("localhost", 3489);
		File file = new File("d:\\monuments.mp3");
		
		try {
			InputStream in = new FileInputStream(file);
			AudioInputStream rawInput = AudioSystem.getAudioInputStream(in);
			
			AudioFormat baseFormat = rawInput.getFormat();
			AudioFormatDTO afDto = new AudioFormatDTO(baseFormat.getSampleRate(), 
					16, baseFormat.getChannels(), baseFormat.getChannels() * 2, 
					baseFormat.getSampleRate(), false);
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
					baseFormat.getSampleRate(), // sample rate (same as base
												// format)
					16, // sample size in bits (thx to Javazoom)
					baseFormat.getChannels(), // # of Channels
					baseFormat.getChannels() * 2, // Frame Size
					baseFormat.getSampleRate(), // Frame Rate
					false // Big Endian
			);
			AudioInputStream decodedInput = AudioSystem.getAudioInputStream(decodedFormat, rawInput);
			
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
			in.close();
			//socket.getOutputStream().flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
		/*final FileChannel channel = new FileInputStream(file).getChannel();
		byte[] barray = new byte[(int) file.length()];
	    ByteBuffer bb = ByteBuffer.wrap(barray);
	    bb.order(ByteOrder.LITTLE_ENDIAN);
	    channel.read(bb);

		// when finished
		channel.close();
		
		byte[] chunk = new byte[1024];
		int i = 0;
		for (byte b : bb.array()) {
			chunk[i++] = b;
			
			if (i == 1024) {
				socket.getOutputStream().write(chunk);		
				socket.getOutputStream().write("\r\n".getBytes());
				socket.getOutputStream().flush();
				
				chunk = new byte[1024];
				i = 0;
				break;
			}
		}
	    
		socket.getOutputStream().write("eof\r\n".getBytes());
		socket.getOutputStream().flush();
		
		channel.close();*/
		socket.close();
	}
	
	@Bean
	public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		// the alphavantage api apparently has some trouble with ssl - this rest template
		// is configured to ignore ssl
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
	                    .loadTrustMaterial(null, acceptingTrustStrategy)
	                    .build();

	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

	    CloseableHttpClient httpClient = HttpClients.custom()
	                    .setSSLSocketFactory(csf)
	                    .build();

	    HttpComponentsClientHttpRequestFactory requestFactory =
	                    new HttpComponentsClientHttpRequestFactory();

	    requestFactory.setHttpClient(httpClient);
		
		RestTemplate rest = new RestTemplate(requestFactory);
		rest.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
		
		return rest;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getObjectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		return mapper;
	}
}
