package assign05;

import java.util.ArrayList;
import java.util.Random;

/**
 * Chooses a random pivot index from a specified portion of a list.
 *
 * @param <E> the type of elements in the list
 * @author Ava Murphy & Yujia Zhao
 * @version September 30, 2026
 */
public class RandomPivotChooser <E extends Comparable<? super E>> implements PivotChooser<E> {
	
	/**
	 * Returns a randomly selected pivot index within the specified range.
	 *
	 * @param list the list from which to choose a pivot
	 * @param leftIndex the first index of the range
	 * @param rightIndex the last index of the range
	 * @return a random index from leftIndex through rightIndex, inclusive
	 */
	@Override
	public int getPivotIndex(ArrayList<E> list, int leftIndex, int rightIndex) {
		Random random = new Random();
		int index = random.nextInt(leftIndex, rightIndex + 1);
		return index;
	}

}
