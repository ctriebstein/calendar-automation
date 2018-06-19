package de.ect.home.automation.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.ect.home.automation.music.util.Player;
import de.ect.home.automation.music.util.Speaker;

@Component
public class StreamingServiceImpl implements StreamingService {

	@Value("${music.streaming.speakers}")
	private String[] speakers;
	
	@Value("${music.streaming.port}")
	private int port;
	
	@Value("${music.streaming.webradios}")
	private String[] webradios;
	
	@Value("${music.streaming.local.folders}")
	private String[] localFolders;
	
	private List<Player> players = new ArrayList<>();

	@Override
	public List<Speaker> getSpeakers() {
		if (speakers == null || speakers.length == 0) {
			return new ArrayList<>();
		}
		List<Speaker> list = new ArrayList<>();
		for (String speaker : speakers) {
			list.add(new Speaker(speaker));
		}
		
		return list;
	}

	@Override
	@Async
	public void playSongs(File song, List<Speaker> speakers) {
		for (Speaker speaker : speakers) {
			Player player = new Player();
			player.play(song, speaker, port);
			this.players.add(player);
		}
		
		// TODO: stop player if it is already playing on given speaker
	}

	@Override
	public void stopPlaying(Speaker speaker) {
		// TODO Auto-generated method stub
		
	}
}
