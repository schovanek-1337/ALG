package cz.plesioEngine.engineTester;

import cz.plesioEngine.ECS.Entity;
import cz.plesioEngine.ECS.EntityManager;
import cz.plesioEngine.ECS.components.CTexturedMesh;
import cz.plesioEngine.ECS.components.CTransform;
import cz.plesioEngine.ECS.systems.SRender;
import cz.plesioEngine.controls.ControlManager;
import cz.plesioEngine.controls.MouseManager;
import cz.plesioEngine.debugConsole.Console;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.staticEntities.EntityCreator;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.gameLogic.GameState;
import cz.plesioEngine.gameLogic.GameStateMaster;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import cz.plesioEngine.toolbox.MousePicker;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author plesio
 */
public class ECSTester {

	public static void main(String[] args) {
		long windowHandle;

		DisplayManager.createDisplay();
		windowHandle = DisplayManager.getWindowHandle();

		Camera.init();

		ControlManager.init();
		MasterRenderer.init();
		Console.init();

		MousePicker.init();

		GameStateMaster.setGameState(GameState.PLAYING);

		cz.plesioEngine.entities.staticEntities.Entity plane
			= EntityCreator.createEntity("plane", "levelAssets/textures/floor");

		plane.getTexturedModel().getModelTexture().setTileSize(30);
		plane.setPosition(new Vector3f(0, -12, 0));
		plane.setScale(100);

		EntityRenderer.processEntity(plane);
		PhysicsEngine.processEntity(plane);

		Mesh shotgunMesh = MeshMaster.requestMesh("weapons/meshes/doubleBarrelShotgun");
		Texture shotgunTexture
			= TextureMaster.requestTexture("weapons/textures/doubleBarrelShotgun");

		Entity shotgunEntity = EntityManager.createEntity(true);
		shotgunEntity.addComponents(
			new CTransform(new Vector3f(0, 0, 0), 0, 0, 0, 1),
			new CTexturedMesh(shotgunMesh, shotgunTexture));

		SRender.processLists();

		while (!GLFW.glfwWindowShouldClose(windowHandle)) {

			DisplayManager.updateFPS();

			Camera.move();

			MouseManager.updateFrameSensitiveEvents();

			PhysicsEngine.update();

			MasterRenderer.renderScene();

			Console.update();

			DisplayManager.updateUPS();

			DisplayManager.updateDisplay();

		}

		DisplayManager.closeDisplay();

		MasterRenderer.cleanUp();
	}

}
