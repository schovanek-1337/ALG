package cz.plesioEngine.ECS.systems;

import cz.plesioEngine.ECS.EntityManager;
import cz.plesioEngine.ECS.components.CRenderFlags;
import cz.plesioEngine.ECS.components.CRenderFlagsEnum;
import cz.plesioEngine.ECS.components.CTransform;
import cz.plesioEngine.ECS.components.CTexturedMesh;
import cz.plesioEngine.renderEngine.MasterRenderer;
import cz.plesioEngine.renderEngine.shaders.StaticShader;
import cz.plesioEngine.renderEngine.textures.Texture;
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
 * Rendering component system. Renders everything and anything.
 *
 * @author plesio
 */
public class SRender implements IComponentSystem {

	private static StaticShader entityShader;
	private static HashMap<Pair<CTexturedMesh, CRenderFlags>, List<CTransform>> renderables = new HashMap<>();

	public static void init(StaticShader eShader) {
		entityShader = eShader;
		entityShader.start();
		entityShader.loadProjectionMatrix();
		entityShader.loadStaticLights();
		entityShader.stop();
	}

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public void update() {

	}

	/**
	 * Pulls component lists from entity manager, sorts by VAO id and puts them into
	 * a HashMap, then prepares them for rendering
	 */
	public static void processLists() {
		List<CTransform> entityTransforms
			= EntityManager.getComponentList(CTransform.class);
		List<CTexturedMesh> texturedMeshes
			= EntityManager.getComponentList(CTexturedMesh.class);
		List<CRenderFlags> renderFlags
			= EntityManager.getComponentList(CRenderFlags.class);

		for (CTexturedMesh ctm : texturedMeshes) {

			CRenderFlags associatedFlags = null;
			if (renderFlags != null) {
				associatedFlags = renderFlags.stream()
					.filter(rFlags -> rFlags.entityID == ctm.entityID)
					.findFirst()
					.orElse(null);
			}

			CTexturedMesh sameVAOMesh = texturedMeshes.stream()
				.filter(tMesh -> tMesh.mesh.vaoID == ctm.mesh.vaoID)
				.findFirst()
				.orElse(null);

			if (sameVAOMesh != null) {
				Pair<CTexturedMesh, CRenderFlags> pair
					= Pair.with(sameVAOMesh, associatedFlags);
				if (renderables.containsKey(pair)) {

					CTransform transformToAdd = entityTransforms.stream()
						.filter(transform -> transform.entityID == ctm.entityID)
						.findFirst()
						.orElse(null);

					renderables.get(pair).add(transformToAdd);
					continue;
				}
			}

			Pair<CTexturedMesh, CRenderFlags> texMeshFlagsPair
				= Pair.with(ctm, associatedFlags);

			if (renderables.containsKey(texMeshFlagsPair)) {

				CTransform transformToAdd = entityTransforms.stream()
					.filter(transform -> transform.entityID == ctm.entityID)
					.findFirst()
					.orElse(null);

				if (transformToAdd == null) {
					continue;
				}

				renderables.get(texMeshFlagsPair).add(transformToAdd);

			} else {

				CTransform transformToAdd = entityTransforms.stream()
					.filter(transform -> transform.entityID == ctm.entityID)
					.findFirst()
					.orElse(null);

				if (transformToAdd == null) {
					continue;
				}

				List<CTransform> transformList = new ArrayList<>();
				transformList.add(transformToAdd);

				renderables.put(texMeshFlagsPair, transformList);

			}

		}

	}

	/**
	 * Draw components on screen.
	 */
	public static void render() {
		Iterator it = renderables.entrySet().iterator();
		int vaoID = 0;
		int textureID = 0;
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();

			Pair tmTexFlagsPair = (Pair) pair.getKey();
			CTexturedMesh texMesh = (CTexturedMesh) tmTexFlagsPair.getValue0();
			CRenderFlags renderFlags = (CRenderFlags) tmTexFlagsPair.getValue1();

			if (vaoID != texMesh.mesh.vaoID) {
				vaoID = texMesh.mesh.vaoID;
				GL30.glBindVertexArray(vaoID);
			}

			if (textureID != texMesh.texture.getTextureID()) {
				textureID = texMesh.texture.getTextureID();
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			}

			List<CTransform> transformsToRenderAt
				= (List<CTransform>) pair.getValue();

			entityShader.start();
			entityShader.loadViewMatrix();
			entityShader.loadDynamicLights();
			entityShader.loadSkyColor(MasterRenderer.getSkyColor().x(),
				MasterRenderer.getSkyColor().y(),
				MasterRenderer.getSkyColor().z());

			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);

			for (CTransform transform : transformsToRenderAt) {
				parseRenderFlags(renderFlags);
				entityShader.loadUseFakeLighting(
					texMesh.texture.hasFakeLighting());
				entityShader.loadShineVariables(
					texMesh.texture.getReflectivity(),
					texMesh.texture.getShineDamper());
				entityShader.loadNumberOfRows(texMesh.texture.getNumberOfRows());
				entityShader.loadOffset(calculateTextureOffsets(
					texMesh.textureAtlasIndex,
					texMesh.texture));
				entityShader.loadTextureTileSize(texMesh.texture.getTileSize());
				Matrix4f transformationMatrix
					= Maths.createTransformationMatrix(
						transform.position,
						transform.rotX,
						transform.rotY,
						transform.rotZ,
						transform.scale);
				entityShader.loadTransformationMatrix(transformationMatrix);

				GL11.glDrawElements(GL11.GL_TRIANGLES,
					texMesh.mesh.indices.capacity(),
					GL11.GL_UNSIGNED_INT, 0);
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);

		entityShader.stop();
	}

	private static void parseRenderFlags(CRenderFlags flags) {
		if (flags == null) {
			return;
		}
		for (CRenderFlagsEnum flag : flags.flags) {
			switch (flag) {
				case NO_VIEW_MATRIX:
					entityShader.loadIgnoreViewMatrix(true);
			}
		}
	}

	private static Vector2f calculateTextureOffsets(int texAtlasIndex,
		Texture tex) {

		int column = texAtlasIndex % tex.getNumberOfRows();
		int row = texAtlasIndex / tex.getNumberOfRows();
		return new Vector2f(column, row);
	}

}
