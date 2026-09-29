package assign05;

import java.util.ArrayList;

public class MergeSorter<E extends Comparable<? super E>> implements Sorter<E> {

	private int threshold;

	public MergeSorter(int threshold) {
		if(threshold <= 0)
			throw new IllegalArgumentException();

		this.threshold = threshold;
	}

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