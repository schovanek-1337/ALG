package cz.plesioEngine.toolbox;

import java.nio.FloatBuffer;
import java.util.List;
import org.lwjgl.BufferUtils;

/**
 * Methods for converting stuff to other stuff.
 * @author plesio
 */
public class Converter {

	public static FloatBuffer arrayToBuffer(float[] arrayToConvert) {
		FloatBuffer newBuffer
			= BufferUtils.createFloatBuffer(arrayToConvert.length);
		newBuffer.put(arrayToConvert);
		newBuffer.flip();
		return newBuffer;
	}

	public static float[] floatListToFloatArray(List<Float> list) {
		float[] floatArray = new float[list.size()];
		int i = 0;

		for (Float f : list) {
			floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
		}

		return floatArray;
	}

	public static int[] intListToIntArray(List<Integer> list) {
		int[] floatArray = new int[list.size()];
		int i = 0;

		for (Integer f : list) {
			floatArray[i++] = f;
		}

		return floatArray;
	}

}
