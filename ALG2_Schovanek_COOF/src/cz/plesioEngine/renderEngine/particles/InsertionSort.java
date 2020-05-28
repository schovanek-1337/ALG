package cz.plesioEngine.renderEngine.particles;

import java.util.List;

/**
 *
 * @author plesio
 */
public class InsertionSort {

	/**
	 * Sorts a list of particles so that the particles with the highest
	 * distance from the camera are first, and the particles with the
	 * shortest distance are last.
	 *
	 * @param list - the list of particles needing sorting.
	 */
	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle item = list.get(i);
			if (item.getDistance() > list.get(i - 1).getDistance()) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Particle> list, int i) {
		Particle item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

	public static void insertionSort(float array[]) {
		int n = array.length;
		for (int j = 1; j < n; j++) {
			float key = array[j];
			int i = j - 1;
			while ((i > -1) && (array[i] > key)) {
				array[i + 1] = array[i];
				i--;
			}
			array[i + 1] = key;
		}
	}

}
