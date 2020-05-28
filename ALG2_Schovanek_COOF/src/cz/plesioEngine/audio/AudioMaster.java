package cz.plesioEngine.audio;

import cz.plesioEngine.toolbox.Maths;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

/**
 * Manages audio.
 *
 * @author plesio
 */
public class AudioMaster {

	/**
	 * Context of OpenAL
	 */
	private static long context;

	/**
	 * Used Device
	 */
	private static long device;

	/**
	 * OpenAL Buffers, used for later cleaning
	 */
	private static List<Integer> buffers = new ArrayList<>();

	/**
	 * AudioMaster must me initialized, if you don't do it it doesn't load
	 * OpenAL and you will crash.
	 */
	public static void init() {
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (alCapabilities.OpenAL10) {
			//OpenAL 1.0 is supported
		}
		
		AL10.alListenerf(AL11.AL_GAIN, 1.0f);

		AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);
	}

	/**
	 * Loads a sound file (only .ogg files) and places it into an OpenAL
	 * buffer.
	 *
	 * @param file sound file, .ogg only
	 * @return ID of the OpenAL buffer
	 */
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);

		//Push the memory stack
		MemoryStack.stackPush();
		IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

		//Loads the audio file into the buffer
		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(file,
			channelsBuffer, sampleRateBuffer);

		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		MemoryStack.stackPop();
		MemoryStack.stackPop();

		int format = -1;
		if (channels == 1) {
			format = AL10.AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL10.AL_FORMAT_STEREO16;
		}

		AL10.alBufferData(buffer, format, rawAudioBuffer, sampleRate);

		LibCStdlib.free(rawAudioBuffer);

		return buffer;
	}

	/**
	 * Listener data used for further audio processing.
	 *
	 * @param position Position of the listener, used for attenuation.
	 * @param velocity Velocity of the listener, used for Doppler effect.
	 */
	public static void setListenerData(Vector3f position, Vector3f velocity) {
		AL10.alListener3f(AL10.AL_POSITION, position.x(), position.y(), position.z());
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.x(), velocity.y(), velocity.z());

		Matrix4f viewMatrix = Maths.createViewMatrix();
		Vector3f at = new Vector3f();
		viewMatrix.positiveZ(at);
		at.negate();
		Vector3f up = new Vector3f();
		viewMatrix.positiveY(up);
		
		float[] data = new float[6];
		data[0] = at.x();
		data[1] = at.y();
		data[2] = at.z();

		data[3] = up.x();
		data[4] = up.y();
		data[5] = up.z();
		
		AL10.alListenerfv(AL10.AL_ORIENTATION, data);
	}

	/**
	 * Properly closes the device and cleans up all the buffers.
	 */
	public static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		alcDestroyContext(context);
		alcCloseDevice(device);
	}

}
