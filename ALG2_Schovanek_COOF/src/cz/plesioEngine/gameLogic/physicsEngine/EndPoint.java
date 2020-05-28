package cz.plesioEngine.gameLogic.physicsEngine;

/**
 * PART OF SAP - TODO, IGNORE FOR NOW
 * @author plesio
 */
public class EndPoint {

	private AABB owner;
	private float value;
	private boolean isMin;

	public EndPoint(AABB owner, float value, boolean isMin) {
		this.owner = owner;
		this.value = value;
		this.isMin = isMin;
	}

	public EndPoint(EndPoint e) {
		this.owner = e.getOwner();
		this.value = e.getValue();
		this.isMin = e.isIsMin();
	}

	@Override
	public String toString() {
		return "EndPoint{" + "owner=" + owner + ", value=" + value + ", isMin="
			+ isMin + '}';
	}

	public AABB getOwner() {
		return owner;
	}

	public void setOwner(AABB owner) {
		this.owner = owner;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public boolean isIsMin() {
		return isMin;
	}

	public void setIsMin(boolean isMin) {
		this.isMin = isMin;
	}

}
