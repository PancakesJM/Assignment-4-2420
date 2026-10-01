package assign05;

import java.util.ArrayList;

/**
 * Sorts an ArrayList using merge sort and switches to insertion sort
 * when the list portion reaches the threshold size.
 *
 * @param <E> the type of elements in the list
 * @author Ava Murphy & Yujia Zhao
 * @version September 30, 2026
 */
public class MergeSorter<E extends Comparable<? super E>> implements Sorter<E> {

	private int threshold;

	/**
	 * Constructs a merge sorter with the specified insertion sort threshold.
	 * @param threshold- list size at which insertion sort is used
	 * @throws IllegalArgumentException if threshold is less than or equal to zero
	 */
	public MergeSorter(int threshold) {
		if(threshold <= 0)
			throw new IllegalArgumentException();

		this.threshold = threshold;
	}

	/**
	 * Sorts the provided list according to natural ordering.
	 * @param list the list to sort
	 */
	@Override
	public void sort(ArrayList<E> list) {
		if(list.size() <= 1)
			return;

		int currentThreshold = threshold;

		if(list.size() < currentThreshold)
			currentThreshold = list.size();

		// Create the auxiliary list only once.
		ArrayList<E> temp = new ArrayList<E>();

		for(int i = 0; i < list.size(); i++)
			temp.add(null);

		mergeSort(list, temp, 0, list.size() - 1, currentThreshold);
	}

	/**
	 * Recursively sorts the portion of the list between the given indices.
	 * Uses insertion sort when the portion reaches the threshold size.
	 *
	 * @param list the list being sorted
	 * @param temp the auxiliary list used during merging
	 * @param leftIndex the first index of the portion to sort
	 * @param rightIndex the last index of the portion to sort
	 * @param currentThreshold the threshold where insertion sort is used
	 */
	private void mergeSort(ArrayList<E> list, ArrayList<E> temp,
			int leftIndex, int rightIndex, int currentThreshold) {

		int size = rightIndex - leftIndex + 1;

		// Switch to insertion sort when the threshold is reached.
		if(size <= currentThreshold) {
			insertionSort(list, leftIndex, rightIndex);
			return;
		}

		int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;
		mergeSort(list, temp, leftIndex, middleIndex, currentThreshold);
		mergeSort(list, temp, middleIndex + 1, rightIndex, currentThreshold);
		merge(list, temp, leftIndex, middleIndex, rightIndex);
	}

	/**
	 * Merges two adjacent sorted portions of a list into one sorted portion.
	 *
	 * @param list the list containing the portions to merge
	 * @param temp the auxiliary list used to store merged elements
	 * @param leftIndex the first index of the left portion
	 * @param middleIndex the last index of the left portion
	 * @param rightIndex the last index of the right portion
	 */
	private void merge(ArrayList<E> list, ArrayList<E> temp,
			int leftIndex, int middleIndex, int rightIndex) {

		int left = leftIndex;
		int right = middleIndex + 1;
		int tempIndex = leftIndex;

		while(left <= middleIndex && right <= rightIndex) {
			if(list.get(left).compareTo(list.get(right)) <= 0) {
				temp.set(tempIndex, list.get(left));
				left++;
			}
			else {
				temp.set(tempIndex, list.get(right));
				right++;
			}

			tempIndex++;
		}

		while(left <= middleIndex) {
			temp.set(tempIndex, list.get(left));
			left++;
			tempIndex++;
		}

		while(right <= rightIndex) {
			temp.set(tempIndex, list.get(right));
			right++;
			tempIndex++;
		}

		for(int i = leftIndex; i <= rightIndex; i++)
			list.set(i, temp.get(i));
	}

	/**
	 * Sorts a part of the list using insertion sort.
	 * @param list the list containing the portion to sort
	 * @param leftIndex the first index of the portion to sort
	 * @param rightIndex the last index of the portion to sort
	 */
	private void insertionSort(ArrayList<E> list,
			int leftIndex, int rightIndex) {

		for(int i = leftIndex + 1; i <= rightIndex; i++) {
			E current = list.get(i);
			int j = i - 1;

			while(j >= leftIndex &&
					list.get(j).compareTo(current) > 0) {

				list.set(j + 1, list.get(j));
				j--;
			}

			list.set(j + 1, current);
		}
	}
}
