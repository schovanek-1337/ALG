package cz.plesioEngine.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

/**
 *
 * @author plesio
 */
public class Source {

	/**
	 * ID of the source inside of OpenAL
	 */
	private int sourceID;

	/**
	 * Default source, generates its buffer on creation, still requires
	 * further setup
	 */
	public Source() {
		sourceID = AL10.alGenSources();
	}

	/**
	 * Velocity used for Doppler effect.
	 *
	 * @param velocity Velocity of the source.
	 */
	public void velocity(Vector3f velocity) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, velocity.x, velocity.y,
			velocity.z);
	}

	/**
	 * Pause the source, can be resumed
	 */
	public void pause() {
		AL10.alSourcePause(sourceID);
	}

	/**
	 * Resume / start playing
	 */
	public void continuePlaying() {
		AL10.alSourcePlay(sourceID);
	}

	/**
	 * Stop the source, can't resume playing, just repeats from the start
	 */
	public void stop() {
		AL10.alSourceStop(sourceID);
	}

	/**
	 * Allow looping
	 *
	 * @param loop fruit loops.
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING,
			loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	/**
	 * Returns the play state.
	 *
	 * @return TRUE = playing, FALSE = stopped
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE)
			== AL10.AL_PLAYING;
	}

	/**
	 * Play a buffer.
	 *
	 * @param buffer buffer.
	 */
	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		continuePlaying();
	}

	/**
	 * Set volume
	 *
	 * @param volume 1 = 100%, over 1 distorts
	 */
	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}

	/**
	 * Just pitch.
	 *
	 * @param pitch pitch.
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}

	/**
	 * Position for attenuation.
	 *
	 * @param position position.
	 */
	public void setPosition(Vector3f position) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, position.x, position.y,
			position.z);
	}

	/**
	 * For cleaning.
	 */
	public void delete() {
		stop();
		AL10.alDeleteSources(sourceID);
	}

}
