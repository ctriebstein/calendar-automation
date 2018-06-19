package de.ect.home.automation.music.util;

import org.springframework.util.StringUtils;

/**
 * dto representing a speaker as it might have been defined in the properties file
 * includes ip address and a specified name of the speaker
 * 
 * @author ctr
 */
public class Speaker {

	private String ip;
	private String name;
	
	public Speaker(String ipAndName) {
		if (StringUtils.isEmpty(ipAndName)) {
			this.ip = this.name = "unknown";
		}
		
		if (!ipAndName.contains(" ")) {
			this.ip = this.name = ipAndName;
		}
		else {
			String[] splitted = ipAndName.split(" ");
			this.ip = splitted[0];
			this.name = splitted[1];
		}
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getName() {
		return name;
	}
}
