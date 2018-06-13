package de.ect.home.streamingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ConnectionListenerConfiguration {
	
	@Autowired
	private SpeakerServiceConnector messageHandler;

	@Bean
    public TcpNetServerConnectionFactory cf() {
        return new TcpNetServerConnectionFactory(3489);
    }

    @Bean
    public TcpReceivingChannelAdapter inbound(AbstractServerConnectionFactory cf) {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(cf);
        adapter.setOutputChannel(tcpIn());
        return adapter;
    }

    @Bean
    public MessageChannel tcpIn() {
    	PublishSubscribeChannel channel = new PublishSubscribeChannel();
    	channel.subscribe(messageHandler);
 
    	return channel;
    }
}
