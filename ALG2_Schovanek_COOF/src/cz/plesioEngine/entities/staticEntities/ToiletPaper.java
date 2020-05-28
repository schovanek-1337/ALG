package cz.plesioEngine.entities.staticEntities;

import cz.plesioEngine.renderEngine.models.TexturedMesh;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class ToiletPaper extends Entity {

	public ToiletPaper(TexturedMesh texturedMesh, Vector3f position,
		float rotX, float rotY, float rotZ, float scale) {
		super(texturedMesh, position, rotX, rotY, rotZ, scale);
	}

}
