package de.ect.home.commons;

import java.io.Serializable;

import javax.sound.sampled.AudioFormat;

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
	
	public AudioFormat toAudioFormat() {
		return new AudioFormat(
        		AudioFormat.Encoding.PCM_SIGNED, 
        		this.getSampleRate(),
        		this.getSampleSizeInBits(),
        		this.getChannels(),
        		this.getFrameSize(),
        		this.getFrameRate(),
        		this.isBigEndian());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AudioFormatDTO)) {
			return false;
		}
		
		AudioFormatDTO format = (AudioFormatDTO) obj;
		
		boolean sameSampleRateAndSize = Math.round(format.getSampleRate()) == Math.round(this.getSampleRate()) && 
				format.getSampleSizeInBits() == this.getSampleSizeInBits();
		boolean sameFrameRateAndSize = format.getFrameSize() == this.getFrameSize() &&
				Math.round(format.getFrameRate()) == Math.round(this.getFrameRate());
		
		return sameSampleRateAndSize && sameFrameRateAndSize &&
				format.getChannels() == this.getChannels() &&
				format.isBigEndian() == this.isBigEndian();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
