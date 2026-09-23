package assign04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class contains methods for generating arrays of various sizes and orderings.
 * 
 * @author CS 2420 course staff and ***Yujia Zhao***
 * @version ***23-09-2026***
 */
public class ArrayGenerator {
	private static final Random rng = new Random();

	/**
	 * Generates an array with problemSize random integers in nearly ascending order.
	 *
	 * @implNote calls generateAscendingArray and then swaps a small number of
	 *           random pairs of nearby elements
	 * @param problemSize - size of the array
	 */
	public static Integer[] generateNearlyAscendingArray(int problemSize) {
		Integer[] array = generateAscendingArray(problemSize);
		slightlyShuffleArray(array);
		return array;
	}

	/**
	 * Generates an array with problemSize random integers in a permuted order.
	 *
	 * @implNote calls generateAscendingArray and then shuffles the contents of the
	 *           array
	 * @param problemSize - size of the array
	 */
	public static Integer[] generatePermutedArray(int problemSize) {
		Integer[] array = generateAscendingArray(problemSize);
		shuffleArray(array);
		return array;
	}

	/**
	 * Generates an array with problemSize random integers in descending order.
	 *
	 * @implNote calls generateAscendingArray and then reverses the contents of the
	 *           array
	 * @param problemSize - size of the array
	 */
	public static Integer[] generateDescendingArray(int problemSize) {
	    Integer[] array = generateAscendingArray(problemSize);

	    for(int i = 0; i < array.length / 2; i++) {
	        swapArrayElements(array, i, array.length - 1 - i);
	    }

	    return array;
	}
	
	/**
	 * Generates an array with problemSize random integers in ascending order.
	 *
	 * @implNote integers are bounded between 0 and 20 + 10 * problemSize
	 * @param problemSize - size of the array
	 */
	private static Integer[] generateAscendingArray(int problemSize) {
		Integer[] array = new Integer[problemSize];
		int currentElement = rng.nextInt(20);
		for(int i = 0; i < problemSize; i++) {
			array[i] = currentElement;
			currentElement += rng.nextInt(10);
		}
		return array;
	}

	/**
	 * Slightly shuffles the contents of the given array, such that it is in nearly
	 * ascending order, by swapping a small number of random pairs of nearby
	 * elements. The number of swaps is small, assuming the array contains a large
	 * number elements.
	 * 
	 * @param array to be shuffled slightly
	 */
	private static void slightlyShuffleArray(Integer[] array) {
		// Choose a random number of pairs to swap, between 5 and 19
		int swapCount = 5 + rng.nextInt(15);
		for(int i = 0; i < swapCount; i++) {
			// Choose a random index, excluding the final 11 indices
			int idx1 = rng.nextInt(array.length - 11);
			// Choose an index between 1 and 10 to the right of idx1
			int idx2 = idx1 + 1 + rng.nextInt(10);
			// Swap the entries at those two indices
			swapArrayElements(array, idx1, idx2);
		}
	}

	/**
	 * Shuffles the contents of the given array.
	 * 
	 * @param array to be shuffled
	 */
	private static void shuffleArray(Integer[] array) {
		ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(array));
		Collections.shuffle(arrayList);
		arrayList.toArray(array);
	}

	/**
	 * Swaps two elements in the given array.
	 *
	 * @param array       with elements to be swapped
	 * @param firstIndex  to swap
	 * @param secondIndex to swap
	 * @throws IndexOutOfBoundsException if either index is out of bounds
	 */
	private static void swapArrayElements(Integer[] array, int firstIndex, int secondIndex) {
		if(firstIndex < 0 || firstIndex >= array.length || secondIndex < 0 || secondIndex >= array.length) {
			throw new IndexOutOfBoundsException();
		}
		int temp = array[firstIndex];
		array[firstIndex] = array[secondIndex];
		array[secondIndex] = temp;
	}
}