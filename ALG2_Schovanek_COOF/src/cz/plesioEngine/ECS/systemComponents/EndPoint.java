package cz.plesioEngine.ECS.systemComponents;

import cz.plesioEngine.ECS.components.CAABB;

/**
 * An endpoint used for sweep-and-prune.
 *
 * @author plesio
 */
public class EndPoint {

	public CAABB owner;
	public float value;
	public boolean isMin;

	public EndPoint(CAABB owner, float value, boolean isMin) {
		this.owner = owner;
		this.value = value;
		this.isMin = isMin;
	}

	public EndPoint(EndPoint e) {
		this.owner = e.owner;
		this.value = e.value;
		this.isMin = e.isMin;
	}

	@Override
	public String toString() {
		return "EndPoint{" + "owner=" + owner + ", value=" + value + ", isMin="
			+ isMin + '}';
	}

}
