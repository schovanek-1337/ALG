package cz.plesioEngine.gameLogic.physicsEngine;

import cz.plesioEngine.ECS.components.CAABB;
import cz.plesioEngine.ECS.systemComponents.EndPoint;
import java.util.Arrays;

/**
 * TODO - PART OF THE ECS, IGNORE FOR NOW
 *
 * @author plesio
 */
public class SweepAndPrune {

	public CAABB[] boxes;

	public EndPoint[] endPointsX;
	public EndPoint[] endPointsY;
	public EndPoint[] endPointsZ;

	public PairManager pm;

	public SweepAndPrune() {
		boxes = new CAABB[1];
		endPointsX = new EndPoint[1];
		endPointsY = new EndPoint[1];
		endPointsZ = new EndPoint[1];
	}

	public void addObject(CAABB box) {

	}

	public void updateObject(CAABB b) {

		for (int i = 0; i < endPointsX.length; i++) {
			EndPoint current = endPointsX[i];
			if (current.owner.equals(b) && !current.isMin) {
				sort(i, 0, b, endPointsX);
			}
		}

		for (int i = 0; i < endPointsX.length; i++) {
			EndPoint current = endPointsX[i];
			if (current.owner.equals(b) && current.isMin) {
				sort(i, 0, b, endPointsX);
			}
		}

		for (int i = 0; i < endPointsY.length; i++) {
			EndPoint current = endPointsY[i];
			if (current.owner.equals(b) && !current.isMin) {
				sort(i, 1, b, endPointsY);
			}
		}

		for (int i = 0; i < endPointsY.length; i++) {
			EndPoint current = endPointsY[i];
			if (current.owner.equals(b) && current.isMin) {
				sort(i, 1, b, endPointsY);
			}
		}

		for (int i = 0; i < endPointsZ.length; i++) {
			EndPoint current = endPointsZ[i];
			if (current.owner.equals(b) && !current.isMin) {
				sort(i, 2, b, endPointsZ);
			}
		}

		for (int i = 0; i < endPointsZ.length; i++) {
			EndPoint current = endPointsZ[i];
			if (current.owner.equals(b) && current.isMin) {
				sort(i, 2, b, endPointsZ);
			}
		}

		System.out.println(Arrays.toString(endPointsX));
	}

	void sort(int i, int axis, CAABB b, EndPoint[] array) {
		int j = i;
		int k = i;
		if (b.min[axis].value < array[j - 1].value) {
			while (j > 0 && array[k].value < array[--j].value) {

				if (overlapTest1D(array[k].owner, array[j].owner, axis)
					&& Collision.boundingBoxIntersection(array[k].owner, array[j].owner)) {
					pm.addPair(array[k].owner, array[j].owner);
				}

				swapEndPoints(array[k], array[j]);
				k--;
			}
		} else {
			while (j < (array.length - 1)
				&& array[k].value > array[++j].value) {

				if (overlapTest1D(array[k].owner, array[j].owner, axis)
					&& Collision.boundingBoxIntersection(array[k].owner, array[j].owner)) {
					pm.addPair(array[k].owner, array[j].owner);
				}

				swapEndPoints(array[k], array[j]);
				k++;
			}
		}
	}

	public void removeObject() {

	}

	private void swapEndPoints(EndPoint a, EndPoint b) {
		EndPoint temp = new EndPoint(a);

		a.isMin = b.isMin;
		a.value = b.value;
		a.owner = b.owner;

		b.isMin = temp.isMin;
		b.value = temp.value;
		b.owner = temp.owner;
	}

	private boolean overlapTest1D(CAABB box1, CAABB box2, int axis) {
		return box1.max[axis].value > box2.min[axis].value;
	}

	private void expandArrays() {

		{
			int currentSize = boxes.length;
			int newSize = boxes.length * 2;

			CAABB[] newArray = new CAABB[newSize];

			System.arraycopy(boxes, 0, newArray, 0, currentSize);

			boxes = newArray;
		}
		
		{
			int currentSize = endPointsX.length;
			int newSize = endPointsX.length * 2;

			EndPoint[] newArray = new EndPoint[newSize];

			System.arraycopy(endPointsX, 0, newArray, 0, currentSize);

			endPointsX = newArray;
		}
		
		{
			int currentSize = endPointsY.length;
			int newSize = endPointsY.length * 2;

			EndPoint[] newArray = new EndPoint[newSize];

			System.arraycopy(endPointsY, 0, newArray, 0, currentSize);

			endPointsY = newArray;
		}
		
		{
			int currentSize = endPointsZ.length;
			int newSize = endPointsZ.length * 2;

			EndPoint[] newArray = new EndPoint[newSize];

			System.arraycopy(endPointsZ, 0, newArray, 0, currentSize);

			endPointsZ = newArray;
		}

	}
	
	public static void main(String[] args) {

		CAABB caabb1 = new CAABB();
		caabb1.min[0] = new EndPoint(caabb1, 1, true);
		caabb1.min[1] = new EndPoint(caabb1, 1, true);
		caabb1.min[2] = new EndPoint(caabb1, 1, true);
		caabb1.max[0] = new EndPoint(caabb1, 2, false);
		caabb1.max[1] = new EndPoint(caabb1, 2, false);
		caabb1.max[2] = new EndPoint(caabb1, 2, false);

		CAABB caabb2 = new CAABB();
		caabb2.min[0] = new EndPoint(caabb2, 3, true);
		caabb2.min[1] = new EndPoint(caabb2, 3, true);
		caabb2.min[2] = new EndPoint(caabb2, 3, true);
		caabb2.max[0] = new EndPoint(caabb2, 4, false);
		caabb2.max[1] = new EndPoint(caabb2, 4, false);
		caabb2.max[2] = new EndPoint(caabb2, 4, false);

		CAABB caabb3 = new CAABB();
		caabb3.min[0] = new EndPoint(caabb3, 5, true);
		caabb3.min[1] = new EndPoint(caabb3, 5, true);
		caabb3.min[2] = new EndPoint(caabb3, 5, true);
		caabb3.max[0] = new EndPoint(caabb3, 6, false);
		caabb3.max[1] = new EndPoint(caabb3, 6, false);
		caabb3.max[2] = new EndPoint(caabb3, 6, false);

		CAABB caabb4 = new CAABB();
		caabb4.min[0] = new EndPoint(caabb4, 7, true);
		caabb4.min[1] = new EndPoint(caabb4, 7, true);
		caabb4.min[2] = new EndPoint(caabb4, 7, true);
		caabb4.max[0] = new EndPoint(caabb4, 8, false);
		caabb4.max[1] = new EndPoint(caabb4, 8, false);
		caabb4.max[2] = new EndPoint(caabb4, 8, false);

		CAABB caabb5 = new CAABB();
		caabb5.min[0] = new EndPoint(caabb5, 9, true);
		caabb5.min[1] = new EndPoint(caabb5, 9, true);
		caabb5.min[2] = new EndPoint(caabb5, 9, true);
		caabb5.max[0] = new EndPoint(caabb5, 10, false);
		caabb5.max[1] = new EndPoint(caabb5, 10, false);
		caabb5.max[2] = new EndPoint(caabb5, 10, false);

		SweepAndPrune sap = new SweepAndPrune();
		sap.endPointsX = new EndPoint[10];
		sap.endPointsY = new EndPoint[10];
		sap.endPointsZ = new EndPoint[10];

		sap.endPointsX[0] = caabb1.min[0];
		sap.endPointsX[2] = caabb2.min[0];
		sap.endPointsX[4] = caabb3.min[0];
		sap.endPointsX[6] = caabb4.min[0];
		sap.endPointsX[8] = caabb5.min[0];

		sap.endPointsX[1] = caabb1.max[0];
		sap.endPointsX[3] = caabb2.max[0];
		sap.endPointsX[5] = caabb3.max[0];
		sap.endPointsX[7] = caabb4.max[0];
		sap.endPointsX[9] = caabb5.max[0];

		sap.endPointsY[0] = caabb1.min[1];
		sap.endPointsY[2] = caabb2.min[1];
		sap.endPointsY[4] = caabb3.min[1];
		sap.endPointsY[6] = caabb4.min[1];
		sap.endPointsY[8] = caabb5.min[1];

		sap.endPointsY[1] = caabb1.max[1];
		sap.endPointsY[3] = caabb2.max[1];
		sap.endPointsY[5] = caabb3.max[1];
		sap.endPointsY[7] = caabb4.max[1];
		sap.endPointsY[9] = caabb5.max[1];

		sap.endPointsZ[0] = caabb1.min[2];
		sap.endPointsZ[2] = caabb2.min[2];
		sap.endPointsZ[4] = caabb3.min[2];
		sap.endPointsZ[6] = caabb4.min[2];
		sap.endPointsZ[8] = caabb5.min[2];

		sap.endPointsZ[1] = caabb1.max[2];
		sap.endPointsZ[3] = caabb2.max[2];
		sap.endPointsZ[5] = caabb3.max[2];
		sap.endPointsZ[7] = caabb4.max[2];
		sap.endPointsZ[9] = caabb5.max[2];

		caabb4.min[0].value = 30;
		caabb4.max[0].value = 35;

		sap.updateObject(caabb4);

	}

}
