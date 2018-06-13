package de.ect.home.commons;

import java.io.Serializable;

public class AudioFormatDTO implements Serializable {

	private static final long serialVersionUID = 4174376569579103450L;
	private float sampleRate;
	private int sampleSizeInBits;
	private int channels;
	private int frameSize;
	private float frameRate;
	private boolean bigEndian;
	
	public AudioFormatDTO(float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian) {
		this.sampleRate = sampleRate;
		this.sampleSizeInBits = sampleSizeInBits;
		this.channels = channels;
		this.frameSize = frameSize;
		this.frameRate = frameRate;
		this.bigEndian = bigEndian;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public int getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	public float getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
	}
}
