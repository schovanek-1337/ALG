package cz.plesioEngine.entities;

import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.staticEntities.MultimeshEntity;
import cz.plesioEngine.entities.weapons.WeaponEntity;
import cz.plesioEngine.gameLogic.physicsEngine.AABB;
import cz.plesioEngine.renderEngine.MasterRenderer;
import static cz.plesioEngine.renderEngine.MasterRenderer.disableCulling;
import static cz.plesioEngine.renderEngine.MasterRenderer.enableCulling;
import cz.plesioEngine.renderEngine.particles.HealthPickup;
import cz.plesioEngine.renderEngine.shaders.BoundingBoxShader;
import cz.plesioEngine.renderEngine.shaders.StaticShader;
import cz.plesioEngine.toolbox.Maths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.javatuples.Pair;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author plesio
 */
public final class EntityRenderer {

	private static StaticShader entityShader;
	private static BoundingBoxShader boundingBoxShader;

	private static boolean renderBoundingBoxes = false;

	private static final Map<Pair<Integer, Integer>, List<Entity>> ENTITY_MAP
		= new HashMap();

	public static void clear() {
		ENTITY_MAP.clear();
	}

	private EntityRenderer() {

	}

	public static void init(StaticShader eShader,
		BoundingBoxShader bShader) {
		entityShader = eShader;
		entityShader.start();
		entityShader.loadProjectionMatrix();
		entityShader.loadStaticLights();
		entityShader.stop();

		boundingBoxShader = bShader;
		boundingBoxShader.start();
		boundingBoxShader.loadProjectionMatrix();
		boundingBoxShader.stop();
	}

	private static void renderBoundingBoxes() {
		for (Map.Entry pair : ENTITY_MAP.entrySet()) {
			List<Entity> entitiesToRender = (List<Entity>) pair.getValue();

			boundingBoxShader.start();
			boundingBoxShader.loadViewMatrix();

			for (Entity e : entitiesToRender) {

				AABB box = e.getBoundingBox();

				GL30.glBindVertexArray(box.getVaoID());
				GL20.glEnableVertexAttribArray(0);

				Matrix4f transformationMatrix = Maths.createTransformationMatrix(
					e.getPosition(), 0, 0, 0, 1);
				boundingBoxShader.loadTransformationMatrix(transformationMatrix);

				GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 36);
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);

			}

			boundingBoxShader.stop();
		}
	}

	public static void render() {
		Iterator it = ENTITY_MAP.entrySet().iterator();
		int vboID = 0;
		int textureID = 0;
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();

			if (vboID != (int) ((Pair) pair.getKey()).getValue0()) {
				vboID = (int) ((Pair) pair.getKey()).getValue0();
				GL30.glBindVertexArray(vboID);
			}

			if (textureID != (int) ((Pair) pair.getKey()).getValue1()) {
				textureID = (int) ((Pair) pair.getKey()).getValue1();
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			}

			List<Entity> entitiesToRender = (List<Entity>) pair.getValue();

			entityShader.start();
			entityShader.loadViewMatrix();
			entityShader.loadDynamicLights();
			entityShader.loadSkyColor(MasterRenderer.getSkyColor().x(),
				MasterRenderer.getSkyColor().y(),
				MasterRenderer.getSkyColor().z());

			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);

			for (Entity entity : entitiesToRender) {

				if (entity.isDisableRender()) {
					continue;
				}
				
				if (entity instanceof HealthPickup){
					entityShader.loadGlow(true);
				}else{
					entityShader.loadGlow(false);
				}

				entityShader.loadIgnoreViewMatrix(entity instanceof WeaponEntity);

				if (entity.getTexturedModel().getModelTexture().isTransparent()) {
					disableCulling();
				}
				entityShader.loadUseFakeLighting(entity.getTexturedModel().
					getModelTexture().hasFakeLighting());
				entityShader.loadShineVariables(
					entity.getTexturedModel().getModelTexture()
						.getReflectivity(),
					entity.getTexturedModel().getModelTexture()
						.getShineDamper());

				entityShader.loadNumberOfRows(entity.getTexturedModel().
					getModelTexture().getNumberOfRows());
				entityShader.loadOffset(new Vector2f(entity.getTextureXOffset(),
					entity.getTextureYOffset()));
				entityShader.loadTextureTileSize(
					entity.getTexturedModel().getModelTexture().getTileSize());

				Matrix4f transformationMatrix
					= Maths.createTransformationMatrix(
						entity.getPosition(), entity.getRotX(),
						entity.getRotY(), entity.getRotZ(),
						entity.getScale());
				entityShader.loadTransformationMatrix(transformationMatrix);

				GL11.glDrawElements(GL11.GL_TRIANGLES,
					entity.getTexturedModel().getMesh().indices
						.capacity(), GL11.GL_UNSIGNED_INT, 0);
			}

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);

			entityShader.stop();
		}
		enableCulling();

		if (isSetRenderBoundingBoxes()) {
			renderBoundingBoxes();
		}
	}

	/**
	 * Add entity for rendering.
	 * @param entity 
	 */
	public static void processEntity(Entity entity) {
		Pair vaoTexturePair = new Pair(entity.getTexturedModel().getMesh().vaoID,
			entity.getTexturedModel().getModelTexture().getTextureID());

		if (!(ENTITY_MAP.containsKey(vaoTexturePair))) {
			List<Entity> entityList = new ArrayList<>();
			entityList.add(entity);
			ENTITY_MAP.put(vaoTexturePair,
				entityList);
		} else {
			ENTITY_MAP.get(vaoTexturePair).add(entity);
		}
	}

	/**
	 * Add multi-mesh entity for rendering.
	 * @param mme 
	 */
	public static void processMultimeshEntity(MultimeshEntity mme) {
		for (Entity entity : mme.getEntitiesArray()) {
			processEntity(entity);
		}
	}

	/**
	 * Batch add entities for rendering.
	 * @param batch 
	 */
	public static void processEntities(List<Entity> batch) {
		//TODO: potential performance improvements
		for (Entity e : batch) {
			processEntity(e);
		}
	}

	/**
	 * Stop entity from rendering.
	 * @param e 
	 */
	public static void removeEntity(Entity e) {
		Pair vaoTexturePair = new Pair(e.getTexturedModel().getMesh().vaoID,
			e.getTexturedModel().getModelTexture().getTextureID());
		
		if(ENTITY_MAP.containsKey(vaoTexturePair)){
			ENTITY_MAP.get(vaoTexturePair).remove(e);
		}
	}

	public static boolean isSetRenderBoundingBoxes() {
		return renderBoundingBoxes;
	}

	public static void setRenderBoundingBoxes() {
		renderBoundingBoxes = !renderBoundingBoxes;
	}

}
