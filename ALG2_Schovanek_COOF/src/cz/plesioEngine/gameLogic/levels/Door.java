package cz.plesioEngine.gameLogic.levels;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class Door extends Entity {

	private Vector3f upPosition;
	private Vector3f downPosition;

	private boolean lastPos = true;

	private Source audioSource = new Source();
	private int audioEffectBuffer = AudioMaster.loadSound("res/sound/gate.ogg");

	public Door(TexturedMesh texturedMesh, Vector3f position,
		float rotX, float rotY, float rotZ, float scale,
		Vector3f upPosition, Vector3f downPosition) {
		super(texturedMesh, position, rotX, rotY, rotZ, scale);
		this.upPosition = upPosition;
		this.downPosition = downPosition;
		audioSource.setVolume(500);

	}

	/**
	 * Slides the door (instantly)
	 * @param doorUp 
	 */
	public void activateDoor(boolean doorUp) {
		if (doorUp == lastPos) {
			return;
		}
		lastPos = doorUp;
		if (doorUp) {
			this.setPosition(upPosition);
			audioSource.setPosition(this.getPosition());
			audioSource.play(audioEffectBuffer);
		} else {
			this.setPosition(downPosition);
			audioSource.setPosition(this.getPosition());
			audioSource.play(audioEffectBuffer);
		}
	}

}
