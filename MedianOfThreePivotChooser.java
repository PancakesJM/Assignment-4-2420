package assign05;

import java.util.ArrayList;

/**
 * Chooses a quicksort pivot by finding the median value from the first,
 * middle, and last elements of a portion of the list.
 *
 * @param <E> the type of elements in the list
 * @author Ava Murphy & Yujia Zhao
 * @version September 30, 2026
 */
public class MedianOfThreePivotChooser<E extends Comparable<? super E>>
		implements PivotChooser<E> {

	/**
	 * Returns the median of three index values of the provided list.
	 *
	 * @param list the list from which to choose a pivot
	 * @param leftIndex the first index
	 * @param rightIndex the last index
	 * @return the index of the median pivot
	 */
	@Override
	public int getPivotIndex(ArrayList<E> list,
			int leftIndex, int rightIndex) {

		int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;

		E left = list.get(leftIndex);
		E middle = list.get(middleIndex);
		E right = list.get(rightIndex);

		if(left.compareTo(middle) <= 0) {
			if(middle.compareTo(right) <= 0)
				return middleIndex;
			else if(left.compareTo(right) <= 0)
				return rightIndex;
			else
				return leftIndex;
		}
		else {
			if(left.compareTo(right) <= 0)
				return leftIndex;
			else if(middle.compareTo(right) <= 0)
				return rightIndex;
			else
				return middleIndex;
		}
	}
}
