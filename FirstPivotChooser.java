package assign05;

import java.util.ArrayList;

/**
 * Chooses the first element of a list as the pivot.
 *
 * @param <E> the type of elements in the list
 * @author Ava Murphy & Yujia Zhao
 * @version September 30, 2026
 */
public class FirstPivotChooser<E extends Comparable<? super E>>
		implements PivotChooser<E> {
	/**
	 * Returns the first index equal to the specified minimum value.
	 *
	 * @param list the list from which to choose a pivot
	 * @param leftIndex the first index of the range
	 * @param rightIndex the last index of the range
	 * @return leftIndex, the index of the first element in the range
	 */
	@Override
	public int getPivotIndex(ArrayList<E> list, int leftIndex, int rightIndex) {
		return leftIndex;
	}
}
