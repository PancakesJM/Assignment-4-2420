package assign05;

import java.util.ArrayList;

public class QuickSorter <E extends Comparable<? super E>> implements Sorter<E> {
	private PivotChooser<E> pivot;
	public QuickSorter(PivotChooser<E> chooser) {
		this.pivot = chooser;
	}
    /**
     * Driver method that calls Quick Sort on provided array
     * @param - Generic array to sort
     */
	@Override
	public void sort(ArrayList<E> list) {
		int max = list.size() - 1;
		sortRecursive(list, 0, max);
	}
	
	private void sortRecursive(ArrayList<E> list, int min, int max) {
		//Partitions into two parts along a pivot
		int pivot = this.pivot.getPivotIndex(list, min, max);
		int value = partition(pivot, list, min, max);
		//Recursively sorts the first segment of array
		if(min < value)
			sortRecursive(list, min, value - 1);
		//recursively sorts second segment of array
		if(min < max - 1)
			sortRecursive(list, pivot + 1, max);
	}
	/**
	 * Helper method called by sortRecursive for sorting array around a pivot point
	 * @param pivot
	 * @param list
	 * @param minIndex
	 * @param maxIndex
	 */
	private int partition(int pivot, ArrayList<E> list, int minIndex, int maxIndex) {
		//Places pivot at the last index
		E temp = list.get(maxIndex);
		E pivotValue = list.get(pivot);
		list.set(maxIndex, pivotValue);
		list.set(pivot, temp);
		int pivotIndex = maxIndex;
		if(minIndex != maxIndex)
			maxIndex--;
		//Compares values in portion of array and swaps until no elements are left
		while(minIndex < maxIndex) {
			//If both indices are out of order, swap with each other
			if(list.get(minIndex).compareTo(pivotValue) > 0 
					&& list.get(maxIndex).compareTo(pivotValue) < 0) {
				temp = list.get(maxIndex);
				list.set(maxIndex, list.get(minIndex));
				list.set(minIndex, temp);
				minIndex++;
				maxIndex--;
			}
			//if only one is out of order, keep that index and move the other
			else {
				if(list.get(minIndex).compareTo(pivotValue) < 0)
					minIndex++;
				if(list.get(maxIndex).compareTo(pivotValue) > 0)
					maxIndex--;
			}
		}
		//Swaps the pivot at the end with the appropriate index
		list.set(pivotIndex, list.get(minIndex));
		list.set(minIndex, pivotValue);
		// returns the pivot point to be used for future partitions
		return minIndex;		
	}
}