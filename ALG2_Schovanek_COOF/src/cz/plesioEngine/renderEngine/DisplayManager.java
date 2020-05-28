package cz.plesioEngine.renderEngine;

import cz.plesioEngine.gameLogic.LogicMaster;
import cz.plesioEngine.gameLogic.SettingMaster;
import java.io.File;
import java.io.IOException;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class DisplayManager {

	private static int WIDTH = 1920;
	private static int HEIGHT = 1080;
	private static boolean fullscreen = true;

	private static int OGLMajVer = 4;
	private static int OGLMinVer = 6;
	private static int antialias = 8;
	
	public static int anisotropyLevel = 16;

	private static long windowHandle;

	private static double lastLoopTime;
	private static float delta;
	private static float timeCount;
	private static int fps;
	private static int fpsCount;
	private static int ups;
	private static int upsCount;

	private static double oldXPos;
	private static double oldYPos;

	private DisplayManager() {
	}

	@SuppressWarnings("null")
	public static void createDisplay() {

		loadConfig();

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_SAMPLES, antialias);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, OGLMajVer);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, OGLMinVer);
		glfwWindowHint(GLFW_DEPTH_BITS, 24);
		
		// Create the window
		if (fullscreen) {
			windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "COOF",
				glfwGetPrimaryMonitor(), NULL);
		} else {
			windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "COOF",
				NULL, NULL);
		}

		if (windowHandle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(windowHandle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				windowHandle,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		glfwMakeContextCurrent(windowHandle);
		glfwSwapInterval(1);

		glfwShowWindow(windowHandle);

		glfwSetInputMode(windowHandle, GLFW_STICKY_KEYS, GLFW_TRUE);
		glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

		GL.createCapabilities();
	}

	private static void loadConfig() {

		String configPath = "config.ini";

		try {
			File scoreFile = new File(configPath);

			Files.lines(scoreFile.toPath()).forEach((t) -> {

				String[] elements = t.split("=");

				switch (elements[0]) {
					case "bFull Screen":
						int fsint = Integer.parseInt(elements[1]);
						fullscreen = fsint != 0;
						break;
					case "iSize H":
						int height = Integer.parseInt(elements[1]);
						HEIGHT = height;
						break;
					case "iSize W":
						int width = Integer.parseInt(elements[1]);
						WIDTH = width;
						break;
					case "iOGLMaj":
						int oglMaj = Integer.parseInt(elements[1]);
						OGLMajVer = oglMaj;
						break;
					case "iOGLMin":
						int oglMin = Integer.parseInt(elements[1]);
						OGLMinVer = oglMin;
						break;
					case "iAntiliasing":
						int aliasing = Integer.parseInt(elements[1]);
						antialias = aliasing;
						break;
					case "iAnisotropy":
						int anisotropicFiltering = Integer.parseInt(elements[1]);
						anisotropyLevel = anisotropicFiltering;
						break;
					case "bGoreDraw":
						int bGoreDraw = Integer.parseInt(elements[1]);
						SettingMaster.enableGore = bGoreDraw != 0;
						break;
					case "bMusic":
						int bMusic = Integer.parseInt(elements[1]);
						SettingMaster.enableMusic = bMusic != 0;
						break;
				}

			});

		} catch (IOException ex) {
			Logger.getLogger(DisplayManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static Vector2f getCurrentMousePos() {
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(8);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(8);

		glfwGetCursorPos(windowHandle, xpos, ypos);

		return new Vector2f((float) xpos.get(0), (float) ypos.get(0));
	}

	public static Vector2f getMouseDelta() {
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(8);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(8);

		glfwGetCursorPos(windowHandle, xpos, ypos);

		return new Vector2f((float) ((oldXPos - xpos.get(0)) * getDelta()),
			(float) ((oldYPos - ypos.get(0)) * getDelta()));
	}

	public static double getTime() {
		return glfwGetTime();
	}

	public static int getFPS() {
		return fps;
	}

	public static int getUPS() {
		return ups;
	}

	public static void updateFPS() {
		fpsCount++;
	}

	public static void updateUPS() {
		upsCount++;
	}

	public static void updateDisplay() {
		oldXPos = getCurrentMousePos().x;
		oldYPos = getCurrentMousePos().y;

		double currentTime = getTime();
		delta = (float) (currentTime - lastLoopTime);
		timeCount += delta;
		lastLoopTime = getTime();

		glfwSwapBuffers(windowHandle);
		glfwPollEvents();

		if (timeCount > 1f) {
			fps = fpsCount;
			fpsCount = 0;

			ups = upsCount;
			upsCount = 0;

			timeCount -= 1f;

			LogicMaster.tick(); //i know it doesn't make much sense in the DM
		}
	}

	public static float getDelta() {
		return delta;
	}

	public static long getWindowHandle() {
		return windowHandle;
	}

	@SuppressWarnings("null")
	public static void closeDisplay() {
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public static Vector2i getWindowDimensions() {
		IntBuffer h = BufferUtils.createIntBuffer(4);
		IntBuffer w = BufferUtils.createIntBuffer(4);

		glfwGetWindowSize(DisplayManager.getWindowHandle(), w, h);

		int width = w.get(0);
		int height = h.get(0);

		return new Vector2i(height, width);
	}

	public static float getAspectRatio() {
		IntBuffer h = BufferUtils.createIntBuffer(4);
		IntBuffer w = BufferUtils.createIntBuffer(4);

		glfwGetWindowSize(DisplayManager.getWindowHandle(), w, h);

		int width = w.get(0);
		int height = h.get(0);

		float aspectRatio = (float) width / height;
		return aspectRatio;
	}

}
