package de.ect.home.automation.music;

import java.io.File;
import java.util.List;

import de.ect.home.automation.music.util.Speaker;

public interface StreamingService {

	/**
	 * lists all speakers configured for this app. is needed to determine where
	 * to play music
	 * 
	 * @return all speakers as defined in application.properties
	 */
	List<Speaker> getSpeakers();

	/**
	 * plays the given song on the given speakers
	 * 
	 * @param song
	 *            song might be a file or a directory. if it is a directory, the
	 *            first playable song in the folder will be played. the player
	 *            will play all playable files in the folder by named order and
	 *            then stop.
	 * @param speakers
	 *            all the speakers the given song(s) should be played on
	 */
	void playSongs(File song, List<Speaker> speakers);

	/**
	 * stops streaming to the given speaker
	 * 
	 * @param speaker
	 */
	void stopPlaying(Speaker speaker);
	
	// TODO: get webradios and folders
	// TODO: play webradio
}
