package de.ect.home.streamingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamingAppApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StreamingAppApplication.class, args);
		
		/*StreamingClient client = new StreamingClient() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public String getClientIp() {
				return "localhost";
			}
			
			@Override
			public String getClientId() {
				return "soundbox 1";
			}
		};
		
		Socket socket = SocketFactory.getDefault().createSocket("localhost", 3489);
		ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
		oout.writeObject(client);
		oout.flush();*/
        
		//socket.close();
	}
}
