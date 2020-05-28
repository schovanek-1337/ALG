package cz.plesioEngine.renderEngine;

import cz.plesioEngine.ECS.systems.SRender;
import cz.plesioEngine.debugConsole.ConsoleInput;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.guis.fontRendering.FontRenderer;
import cz.plesioEngine.guis.fontRendering.FontShader;
import cz.plesioEngine.guis.fontRendering.TextMaster;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.guis.GuiRenderer;
import cz.plesioEngine.guis.GuiShader;
import cz.plesioEngine.renderEngine.particles.ParticleMaster;
import cz.plesioEngine.renderEngine.particles.ParticleShader;
import cz.plesioEngine.renderEngine.shaders.BoundingBoxShader;
import cz.plesioEngine.renderEngine.shaders.StaticShader;
import cz.plesioEngine.renderEngine.skyBox.SkyBoxRenderer;
import cz.plesioEngine.renderEngine.skyBox.SkyBoxShader;
import cz.plesioEngine.toolbox.Maths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author plesio
 */
public final class MasterRenderer {

	//==========================================================================
	// Constants
	//========================================================================== 
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;

	private static float FOG_RED = 0.05f;
	private static float FOG_GREEN = 0.2f;
	private static float FOG_BLUE = 0.3f;

	//==========================================================================
	// Shaders
	//========================================================================== 
	private static final StaticShader STATIC_SHADER = new StaticShader();
	private static final BoundingBoxShader BOUNDING_BOX_SHADER
		= new BoundingBoxShader();
	private static final GuiShader GUI_SHADER = new GuiShader();
	private static final SkyBoxShader SKYBOX_SHADER = new SkyBoxShader();
	private static final FontShader FONT_SHADER = new FontShader();
	private static final ParticleShader PARTICLE_SHADER = new ParticleShader();

	//==========================================================================
	// Other
	//========================================================================== 
	private static final Matrix4f projectionMatrix = Maths.createProjectionMatrix();

	public static void init() {

		EntityRenderer.init(STATIC_SHADER, BOUNDING_BOX_SHADER);
		SkyBoxRenderer.init(SKYBOX_SHADER);
		FontRenderer.init(FONT_SHADER);
		GuiRenderer.init(GUI_SHADER);

		SRender.init(STATIC_SHADER);

		ParticleMaster.init(PARTICLE_SHADER);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		enableCulling();

		registerConsoleCommands();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public static void updateStaticLightSources() {
		STATIC_SHADER.start();
		STATIC_SHADER.loadStaticLights();
		STATIC_SHADER.stop();
	}

	private MasterRenderer() {
	}

	private static void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(FOG_RED, FOG_GREEN, FOG_BLUE, 1);
	}

	public static void renderScene() {
		prepare();

		updateSkyColor();

		EntityRenderer.render();
		SRender.render();
		SkyBoxRenderer.render();
		ParticleMaster.renderParticles();
		GuiRenderer.render();
		TextMaster.render();

	}

	public static void cleanUp() {
		STATIC_SHADER.cleanUp();
		BOUNDING_BOX_SHADER.cleanUp();
		GUI_SHADER.cleanUp();
		SKYBOX_SHADER.cleanUp();
		FONT_SHADER.cleanUp();
		PARTICLE_SHADER.cleanUp();
	}

	public static Vector3f getSkyColor() {
		return new Vector3f(FOG_RED, FOG_GREEN, FOG_BLUE);
	}

	public static void setSkyColor(float r, float g, float b) {
		FOG_RED = r;
		FOG_GREEN = g;
		FOG_BLUE = b;
	}

	public static void setSkyColor(Vector3f color) {
		FOG_RED = color.x();
		FOG_GREEN = color.y();
		FOG_BLUE = color.z();
	}

	public static Matrix4f getProjectionMatrix() {
		return new Matrix4f(projectionMatrix);
	}

	private static void updateSkyColor() {

		switch (GameStateMaster.getCurrentGameState()) {
			case GAME_OVER:
			case FINISHED_LEVEL:
			case MAIN_MENU:
				FOG_RED = 0.1f;
				FOG_GREEN = 0.05f;
				FOG_BLUE = 0.05f;
				break;
			case PAUSED:
				FOG_RED = 0.1f;
				FOG_GREEN = 0.03f;
				FOG_BLUE = 0.03f;
				break;
			case PLAYING:
				FOG_RED = 0.05f;
				FOG_GREEN = 0.2f;
				FOG_BLUE = 0.3f;
				break;
		}

		
	}

	private static void registerConsoleCommands() {
		try {
			ConsoleInput.registerMethod(SkyBoxRenderer.class,
				SkyBoxRenderer.class.getMethod("setBlendFactor", Float.TYPE),
				"sb_blend");
			ConsoleInput.registerMethod(SkyBoxShader.class,
				SkyBoxShader.class.getMethod("setRotationSpeed", Float.TYPE),
				"sb_rotation");
			ConsoleInput.registerMethod(MasterRenderer.class,
				MasterRenderer.class.getMethod("setSkyColor",
					Float.TYPE, Float.TYPE, Float.TYPE), "s_sc");
		} catch (NoSuchMethodException | SecurityException ex) {
			Logger.getLogger(MasterRenderer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
